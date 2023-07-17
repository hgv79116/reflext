package org.example;

import org.example.message.MessageListener;
import org.example.message.client_message.ClientMessage;
import org.example.message.server_message.ServerMessage;
import org.example.message.server_message.connection_status_message.ConnectedMessage;
import org.example.message.server_message.connection_status_message.DisconnectedMessage;
import org.example.message.server_message.connection_status_message.InGameMessage;
import org.example.message.server_message.connection_status_message.InQueueMessage;
import org.example.message.server_message.game_status_message.GameEndedMessage;
import org.example.message.server_message.game_status_message.GameStartedMessage;
import org.example.message.server_message.game_status_message.GameStateMessage;
import org.example.socket_server.*;
import org.example.socket_wrapper.SocketWrapper;
import org.example.survi.Game;
import org.example.survi.PlayerRemovedListener;
import org.example.two_layer_game_server.QueueManager;
import org.example.two_layer_game_server.SocketEventListener;
import org.json.JSONObject;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameApplication implements AutoCloseable {
    private final long GAME_COOLDOWN = (long)2e4;
    private final long TIMESTEP = 50;
    private final SocketServer socketServer;
    private final QueueManager queueManager;
    private BlockingQueue<ClientMessage> inputMessageQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<ServerMessage> outputMessageQueue = new LinkedBlockingQueue<>();

    // application life cycle variables.
    private volatile boolean applicationStopped = false;
    private volatile long applicationStartTime = -1;
    private volatile long applicationEndTime = -1;

    // game lifecycle variables.
    private volatile long lastGameEndTime = -1;
    private Game game;
    private HashMap<SocketWrapper, Integer> playerIdHashMap = new HashMap<>(); // map from socket to the game's player id
    private HashMap<Integer, SocketWrapper> socketWrapperHashMap = new HashMap<>(); // map from player's id to socket wrapper

    // internal game processing variable.s
    private final HashMap<SocketWrapper, JSONObject> userInfo = new HashMap<>();
    // queue management
    private final HashMap<SocketWrapper, MessageListener<ClientMessage>> // map from socket wrappers in the game to listener (client -> queue)
            gameSocketWrapperListenerHashMap = new HashMap<>();
    private final HashMap<SocketWrapper, MessageListener<ClientMessage>> // map from socket wrappers in the queue to listener (client -> game)
            queueSocketWrapperListenerHashMap = new HashMap<>();

    private final HashMap<SocketWrapper, MessageListener<ServerMessage>> // map from socket wrappers in the game to listeners (game -> client)
            gameGameListenerHashMap = new HashMap<>();
    private final HashMap<SocketWrapper, MessageListener<ServerMessage>> // map from socket wrappers in the queue to listeners (game -> client)
            queueGameListenerHashMap = new HashMap<>();

    // listen from game.
    private final Ditributor ditributor = new Ditributor();

    abstract class BaseClientListener implements MessageListener<ClientMessage> {

        protected SocketWrapper socketWrapper;

        public BaseClientListener(SocketWrapper socketWrapper) {
            this.socketWrapper = socketWrapper;
            socketWrapper.addListener(this);
            System.out.println("Testing");
        }

        boolean isConnectionAction(ClientMessage message) {
            return message.getHeader().getString("clientMessageCategory").equals("CONNECTION_ACTION");
        }

        void remove() {
            socketWrapper.removeListener(this);
            socketWrapper = null;
        }

        @Override
        public void onMessage(ClientMessage message) throws ImplementationFailsException {
            System.out.println("Received message from client: " + message);
            if(isConnectionAction(message)) {
                System.out.println("connection action");
                String connectionAction = message.getHeader().getString("connectionAction");
                if(connectionAction.equals("DISCONNECT")) {
                    System.out.println("filtered as disconnect action");
                    try {
                        GameApplication.this.remove(socketWrapper);
                    } catch (IOException e) {
                        throw new ImplementationFailsException(e.getMessage(), e.getCause());
                    }
                }
                else if(connectionAction.equals("CONNECT")) {
                    System.out.println("Reply: " + (new ConnectedMessage(System.currentTimeMillis(), "Connected")));
                    JSONObject userInfo = message.getBody().getJSONObject("userInfo");
                    GameApplication.this.userInfo.put(socketWrapper, userInfo);
                    socketWrapper.sendMessage(new ConnectedMessage(System.currentTimeMillis(), "Connected"));
                }
                else {
                    assert (false);
                }
            }
            System.out.flush();
        }
    }

    class QueueClientListener extends BaseClientListener {

        public QueueClientListener(SocketWrapper socketWrapper) {
            super(socketWrapper);
        }
        @Override
        public void onMessage(ClientMessage message) throws ImplementationFailsException {
            super.onMessage(message);

            System.out.println("Message Ignored");
        }
    }


    class GameClientListener extends BaseClientListener {
        public GameClientListener(SocketWrapper socketWrapper) {
            super(socketWrapper);
        }
        @Override
        public void onMessage(ClientMessage message) throws ImplementationFailsException {
            super.onMessage(message);
            System.out.flush();;
            System.out.println("game listener called");
            System.out.flush();
            if(!isConnectionAction(message)) {
                try {
                    inputMessageQueue.put(message);
                } catch (Exception e) {
                    throw new ImplementationFailsException(e.getMessage(), e.getCause());
                }
            }
        }
    }

    abstract class GameListener implements MessageListener<ServerMessage> {
        protected SocketWrapper socketWrapper;
        public GameListener(SocketWrapper socketWrapper) {
            this.socketWrapper = socketWrapper;
            ditributor.addListener(this);
        }
        void remove() {
            socketWrapper = null;
            ditributor.removeListener(this);
        }
    }

    class GameGameListener extends GameListener {
        public GameGameListener(SocketWrapper socketWrapper) {
            super(socketWrapper);
        }

        @Override
        public void onMessage(ServerMessage message) throws ImplementationFailsException {
            socketWrapper.sendMessage(message);
        }
    }

    class QueueGameListener extends GameListener {
        public QueueGameListener(SocketWrapper socketWrapper) {
            super(socketWrapper);
        }

        @Override
        public void onMessage(ServerMessage message) throws ImplementationFailsException {
            socketWrapper.sendMessage(message);
        }
    }

    class Ditributor {
        private volatile boolean stopped = false;
        private HashSet<MessageListener<ServerMessage>> listeners = new HashSet<>();
        private Thread listeningThread;
        public Ditributor() {
        }

        public void start()  {
            stopped = false;

            listeningThread = new Thread(() -> {
                while(!Thread.interrupted() && !stopped) {
                    if(!outputMessageQueue.isEmpty()) {
                        ServerMessage message = outputMessageQueue.poll();
                        for (MessageListener listener : listeners) {
                            try {
                                listener.onMessage(message);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                }
            });

            listeningThread.start();
        }

        public void stop() {
            stopped = true;
            listeningThread.interrupt();
        }

        public void addListener(MessageListener<ServerMessage> listener) {
            listeners.add(listener);
        }

        public void removeListener(MessageListener<ServerMessage> listener) {
            listeners.remove(listener);
        }
    }

    public GameApplication() throws IOException {
        this.queueManager = new QueueManager();

        this.socketServer = new SocketServer(8080,
                new SocketServerListener() {
                    @Override
                    public void onAcceptConnection(SocketWrapper socketWrapper) {
                        try {
                            socketWrapper.startListening();
                            add(socketWrapper);
                        } catch (Exception e) {
                            System.out.println("Error listening");
                            socketWrapper.sendMessage(new DisconnectedMessage(System.currentTimeMillis(),
                                    "Server error(?): cannot listen from this socket"));
                        }
                    }
                });
    }

    @Override
    public void close() throws IOException {
        queueManager.close();

        socketServer.close();
    }

    // application life cycle
    public void start() throws InterruptedException, IOException {
        socketServer.startAcceptingConnections(); // start the socket server
        startSequentiallyProcessing(); // start infinite while loop to handle events
        applicationStartTime = System.currentTimeMillis();
    }

    public void end() {
        applicationStopped = true; // stop the infinite while loop.
        applicationEndTime = System.currentTimeMillis();
        socketServer.stopAcceptingConnections(); // stop the socket server
    }

    public long getApplicationStartTime() {
        return applicationStartTime;
    }

    public long getApplicationEndTime() {
        return applicationEndTime;
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
    private void startSequentiallyProcessing() throws InterruptedException, IOException {
        long lastGameProcessingTime = System.currentTimeMillis();
        long processingCount = 0;
        while(!applicationStopped) {
            long currentTime = System.currentTimeMillis();
//            System.out.println("This step" + lastGameProcessingTime + " -> " + currentTime);
            if(!gameInitialized()) {
                initializeNewGame();
            }
            else if(!gameHasStarted()) {
                if (canStartGame()) {
                    startGame();
                }
            }
            else if(gameHasEnded()) {
                System.out.println("preparing to dispose game");
                disposeGame();
            }
            else { // game has not ended & has started
                if(currentTime >= lastGameProcessingTime + TIMESTEP) { // the game is still in process & a timestep has passed
                    processGame(currentTime);
                    lastGameProcessingTime = currentTime;
                    // why put it here, but not in processGame()? because this function make use
                    // of lastGameProcessingTime, so I thought all the related manipulations
                    // should be put here.

                    // mistake when debugging: assuming that I can read fast enough to detect anomalies in the output.
                }
            }
//            System.out.println("Next step");
        }
    }

    private boolean gameInitialized() {
        return game != null;
    }

    private void initializeNewGame() {
        game = new Game();
    }

    private boolean gameHasStarted() {
        if(!gameInitialized()) {
            return false;
        }
        if(!game.hasStarted()) {
            return false;
        }
        return true;
    }

    private boolean canStartGame() {
        Collection<SocketWrapper> inQueueSockets = queueManager.getAllQueueSockets().stream().toList();
        for(SocketWrapper socketWrapper: inQueueSockets){
            if (userInfo.containsKey(socketWrapper) && !socketWrapper.checkDisconnected() && game.canAddPlayer()) {
//                System.out.println("user info: " + userInfo.get(socketWrapper));
                int playerId = game.addPlayer(userInfo.get(socketWrapper));
                playerIdHashMap.put(socketWrapper, playerId);
                socketWrapperHashMap.put(playerId, socketWrapper);

                promoteToGame(socketWrapper);
            }
        }

//        System.out.println("Game can start: " + ((lastGameEndTime == -1 || System.currentTimeMillis() - lastGameEndTime >= GAME_COOLDOWN) && game.canStart()));

        return (lastGameEndTime == -1 || System.currentTimeMillis() - lastGameEndTime >= GAME_COOLDOWN) && game.canStart();
    }

    private synchronized void startGame() {
        System.out.println("Started game");
        long currentTime= System.currentTimeMillis();

        game.start(currentTime);

        final GameStartedMessage gameStartMessage= new GameStartedMessage(currentTime, "Game has started");

        for(SocketWrapper socketWrapper: queueManager.getAllGameSockets().stream().toList()) {
            socketWrapper.sendMessage(gameStartMessage);
        }
        System.out.println("Game has started");

        ditributor.start();
    }

    private boolean gameHasEnded() {
        assert(game != null);
        return game.hasEnded();
    }

    private synchronized void disposeGame() throws IOException {
        long currentTime = System.currentTimeMillis();

        ditributor.stop();

        final GameEndedMessage gameEndMessage = new GameEndedMessage(currentTime, "Game has ended");

        Collection<SocketWrapper> inGameSockets = queueManager.getAllGameSockets();
        for(SocketWrapper socketWrapper: inGameSockets) {
            socketWrapper.sendMessage(gameEndMessage);
        }

        game = null;
        lastGameEndTime = currentTime;
        playerIdHashMap.clear();
        socketWrapperHashMap.clear();

        inputMessageQueue.clear();
        outputMessageQueue.clear();

        for(SocketWrapper socketWrapper: inGameSockets) {
            if(socketWrapper.checkDisconnected()) {
                remove(socketWrapper);
            }
        }
    }

    // game lifecycle

    // a process cycle: take all messages, process, and then return the game state.
    private synchronized void processGame(long processingStartTime) throws InterruptedException {
//        System.out.println("Processing game");
        while(!inputMessageQueue.isEmpty() && inputMessageQueue.peek().getTimeStamp() <= processingStartTime) {
            ClientMessage inputMessage = inputMessageQueue.poll();
            game.update(inputMessage);
        }

        JSONObject gameState = game.updateAndGetState(processingStartTime);

        try {
            System.out.println("Game state: " + gameState);
//            System.out.println("outputted game state at " + processingStartTime);
            outputMessageQueue.put(new GameStateMessage(processingStartTime, gameState));
//            System.out.println("Done processing game");
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    public synchronized void add(SocketWrapper socketWrapper) {  // same level as queue manager api
        addToQueue(socketWrapper);
        queueManager.add(socketWrapper);
//        System.out.println("new Connection from " + socketWrapper.getClientAddress() + socketWrapper.getClientPort());
        socketWrapper.sendMessage(new InQueueMessage(System.currentTimeMillis(), "You have been added to queue"));
    }

    public synchronized void promoteToGame(SocketWrapper socketWrapper) { // same level as queue manager api
        removeFromQueue(socketWrapper);
        addToGame(socketWrapper);
        queueManager.promote(socketWrapper);
        socketWrapper.sendMessage(new InGameMessage(System.currentTimeMillis(), "You have been added to game"));
    }

    public synchronized void remove(SocketWrapper socketWrapper) throws IOException { //  same level as queue manager api
        if(queueManager.isInGame(socketWrapper)) {
            removeFromGame(socketWrapper);
        }
        else {
            removeFromQueue(socketWrapper);
        }

        socketWrapper.sendMessage(new DisconnectedMessage(System.currentTimeMillis(), "You have been disconnected"));
//        System.out.println("Messaged sent back: " + new DisconnectedMessage(System.currentTimeMillis(), "You have been disconnected"));

        try {
            queueManager.remove(socketWrapper); // queue manager is the owner of the socket wrapper. only queue manager can call close on it.
//            System.out.println("removed from queue manager");
        } catch (Exception e) {
            System.out.println("removing from queue manager met with exception " + e);
        }
    }

    private void addToQueue(SocketWrapper socketWrapper) throws KeyAlreadyExistsException {
        queueSocketWrapperListenerHashMap.put(socketWrapper, new QueueClientListener(socketWrapper));
        queueGameListenerHashMap.put(socketWrapper, new QueueGameListener(socketWrapper));
    }

    private void addToGame(SocketWrapper socketWrapper) throws KeyAlreadyExistsException {
        gameSocketWrapperListenerHashMap.put(socketWrapper, new GameClientListener(socketWrapper));
        gameGameListenerHashMap.put(socketWrapper, new GameGameListener(socketWrapper));
    }

    private void removeFromGame(SocketWrapper socketWrapper) throws NoSuchElementException {
        try {
            GameClientListener gameClientListener = (GameClientListener) gameSocketWrapperListenerHashMap.remove(socketWrapper);
            gameClientListener.remove();
            GameGameListener gameGameListener = (GameGameListener) gameGameListenerHashMap.remove(socketWrapper);
            gameGameListener.remove();
        } catch (Exception e) {
            System.out.println("Met when removing from hashmap: e");
        }
    }

    private void removeFromQueue(SocketWrapper socketWrapper) throws NoSuchElementException {
        System.out.println("this still works");
        QueueClientListener queueClientListener = (QueueClientListener) queueSocketWrapperListenerHashMap.remove(socketWrapper);
        queueClientListener.remove();
        QueueGameListener queueGameListener = (QueueGameListener) queueGameListenerHashMap.remove(socketWrapper);
        queueGameListener.remove();
    }
}
