package org.example;

import org.example.message.server_message.connection_status_message.ConnectedMessage;
import org.example.message.server_message.connection_status_message.DisconnectedMessage;
import org.example.message.server_message.connection_status_message.InGameMessage;
import org.example.message.server_message.connection_status_message.InQueueMessage;
import org.example.message.server_message.game_status_message.GameEndedMessage;
import org.example.message.server_message.game_status_message.GameStartedMessage;
import org.example.message.server_message.game_status_message.GameStateMessage;
import org.example.message.JsonMessage;
import org.example.socket_server.*;
import org.example.socket_wrapper.SocketWrapper;
import org.example.survi.Game;
import org.example.two_layer_game_server.QueueManager;
import org.example.two_layer_game_server.SocketEventListener;
import org.json.JSONObject;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameApplication {
    private final SocketServer socketServer;
    private final QueueManager queueManager;
    private BlockingQueue<JsonMessage> inputMessageQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<JsonMessage> outputMessageQueue = new LinkedBlockingQueue<>();
    private final GameFactory gameFactory = new GameFactory();

    // application life cycle variables.
    private volatile boolean applicationStopped = false;
    private volatile long applicationStartTime = -1;
    private volatile long applicationEndTime = -1;

    // game lifecycle variables.
    private volatile long lastGameEndTime = -1;
    private volatile long currentGameStartTime = -1;
    private final long gameCoolDown = (long)2e4; // 20 seconds
    private Game game;

    // internal game processing variable.s
    private final long timeStep = 50;
    private volatile long lastGameProcessingTime = -1;



    // queue management
    private final HashMap<SocketWrapper, GameSocketListener> gameSocketWrapperListenerHashMap = new HashMap<>();
    private final HashMap<SocketWrapper, QueueSocketListener> queueSocketWrapperListenerHashMap = new HashMap<>();

    private final HashMap<SocketWrapper, GameGameListener> gameGameListenerHashMap = new HashMap<>();
    private final HashMap<SocketWrapper, QueueGameListener> queueGameListenerHashMap = new HashMap<>();

    private void addToQueue(SocketWrapper socketWrapper) throws KeyAlreadyExistsException {
        queueSocketWrapperListenerHashMap.put(socketWrapper, new QueueSocketListener());
        queueGameListenerHashMap.put(socketWrapper, new QueueGameListener(socketWrapper));
    }

    private void addToGame(SocketWrapper socketWrapper) throws KeyAlreadyExistsException {
        gameSocketWrapperListenerHashMap.put(socketWrapper, new GameSocketListener(inputMessageQueue));
        gameGameListenerHashMap.put(socketWrapper, new GameGameListener(socketWrapper));
    }

    private void removeFromGame(SocketWrapper socketWrapper) throws NoSuchElementException {
        gameSocketWrapperListenerHashMap.remove(socketWrapper);
        gameGameListenerHashMap.remove(socketWrapper);
    }

    private void removeFromQueue(SocketWrapper socketWrapper) throws NoSuchElementException {
        queueSocketWrapperListenerHashMap.remove(socketWrapper);
        queueGameListenerHashMap.remove(socketWrapper);
    }

    public GameApplication() throws IOException {
        this.queueManager = new QueueManager();
        queueManager.setOnAdded(new SocketEventListener() {
            @Override
            public void run(SocketWrapper socket) {
                socket.sendMessage(new InQueueMessage(System.currentTimeMillis(), "You have been added to queue"));
                addToQueue(socket);
            }
        });

        queueManager.setOnPromoted(new SocketEventListener() {
            @Override
            public void run(SocketWrapper socket) {
                socket.sendMessage(new InGameMessage(System.currentTimeMillis(), "You have been moved to game"));
                removeFromQueue(socket);
                addToGame(socket);
            }
        });

        queueManager.setOnDemoted(new SocketEventListener() {
            @Override
            public void run(SocketWrapper socket) {
                socket.sendMessage(new InQueueMessage(System.currentTimeMillis(), "You have been moved back to queue"));
                removeFromGame(socket);
                addToQueue(socket);
            }
        });

        queueManager.setOnRemoved(new SocketEventListener() {
            @Override
            public void run(SocketWrapper socket) {
                socket.sendMessage(new DisconnectedMessage(System.currentTimeMillis(), "You have been disconnected"));
                if(queueManager.isInGame(socket)) {
                    removeFromGame(socket);
                }
                removeFromQueue(socket);
            }
        });

        this.socketServer = new SocketServer(8080,
                new SocketServerListener() {
                    @Override
                    public void onAcceptConnection(SocketWrapper socketWrapper) {
                        socketWrapper.sendMessage(new ConnectedMessage(System.currentTimeMillis(), "You have been connected"));
                        queueManager.add(socketWrapper);
                    }
                });
    }

    // application life cycle
    public void start() throws InterruptedException {
        socketServer.startAcceptingConnections(); // start the socket server

        startSequentiallyProcessing(); // start infinite while loop to handle events
        applicationStartTime = System.currentTimeMillis();
    }

    public void end() {
        socketServer.stopAcceptingConnections(); // stop the socket server

        applicationStopped = true; // stop the infinite while loop.
        applicationEndTime = System.currentTimeMillis();
    }

    // essentially the infinite while loop
    // game = null
    // pre-starting game: push connections from queue to game
    // start game
    // post starting game: send message to listeners
    // while game is started and not ended
    // pre game ended:  send message to listeners
    // end game
    // set game = null
    private void startSequentiallyProcessing() throws InterruptedException {
        while(!applicationStopped) {
            long currentTime = System.currentTimeMillis();
            if(game == null) {
                if (lastGameEndTime == -1) {
                    startNewGame(); // this consists of all the pre and post starting the game steps
                }
                else if(currentTime >= lastGameEndTime + gameCoolDown) {
                    startNewGame();
                }
            }
            else if(game.hasEnded()){
                endCurrentGame();
            }
            else if(currentTime >= lastGameProcessingTime + timeStep){ // the game is still in process & a timestep has passed
                processGame(currentTime);
            }
        }
    }

    // game lifecycle
    private void startNewGame() {
        long currentTime= System.currentTimeMillis();
        currentGameStartTime = currentTime;

        Collection<SocketWrapper> inQueueSockets = queueManager.getAllQueueSockets();
        for(SocketWrapper socketWrapper: inQueueSockets){
            if (!socketWrapper.checkDisconnected()) {
                addToQueue(socketWrapper);
            }
        }

        game = gameFactory.newGame();
        game.start();

        final GameStartedMessage gameStartMessage= new GameStartedMessage(currentTime, "Game has started");
        Collection<SocketWrapper> inGameSockets = queueManager.getAllGameSockets();
        for(SocketWrapper socketWrapper: inGameSockets) {
            socketWrapper.sendMessage(gameStartMessage);
        }
    }

    private void endCurrentGame() {
        long currentTime = System.currentTimeMillis();
        final GameEndedMessage gameEndMessage = new GameEndedMessage(currentTime, "Game has ended");

        Collection<SocketWrapper> inGameSockets = queueManager.getAllGameSockets();
        for(SocketWrapper socketWrapper: inGameSockets) {
            socketWrapper.sendMessage(gameEndMessage);
        }

//        game.end();
        game = null;

        // remove disconnected users
        for(SocketWrapper socketWrapper: inGameSockets) {
            if(socketWrapper.checkDisconnected()) {
                queueManager.remove(socketWrapper);
            }
        }

        lastGameEndTime = currentTime;
    }

    // a process cycle: take all messages, process, and then return the game state.

    private void processGame(long processingStartTime) throws InterruptedException {
        while(inputMessageQueue.size() > 0 && inputMessageQueue.peek().getTimeStamp() <= processingStartTime) {
            JsonMessage inputMessage = inputMessageQueue.poll(); // does not wait
            game.update(inputMessage);
        }

        JSONObject gameState = game.getLastState();
        try {
            outputMessageQueue.put(new GameStateMessage(processingStartTime, gameState));
        } catch (InterruptedException e) {
            throw e;
        }
    }
}
