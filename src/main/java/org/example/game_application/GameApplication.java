package org.example.game_application;

import org.example.message.JsonMessage;
import org.example.survi.Game;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;

public class GameApplication {
    final BlockingQueue<JsonMessage> inputMessageQueue;
    final BlockingQueue<JsonMessage> outputMessageQueue;
    final Game game;
    final long timeStep = 50;
    volatile boolean stopped = false;
    volatile long lastProcessingTime = -1;
    volatile long startTime = -1;
    volatile long endTime = -1;

    public GameApplication(BlockingQueue<JsonMessage> inputMessageQueue,
                           BlockingQueue<JsonMessage> outputMessageQueue,
                           Game game,
                           long timeStep) {
        this.inputMessageQueue = inputMessageQueue;
        this.outputMessageQueue = outputMessageQueue;
        this.game = game;
    }

    private void process(long processingStartTime) throws InterruptedException {
        while(inputMessageQueue.size() > 0 && inputMessageQueue.peek().getTimeStamp() <= processingStartTime) {
            JsonMessage inputMessage = inputMessageQueue.poll(); // does not wait
            game.update(inputMessage);
        }

        JSONObject gameState = game.getLastState();
        long currentTime = System.currentTimeMillis();
        try {
            outputMessageQueue.put(new JsonMessage(currentTime, gameState));
        } catch (InterruptedException e) {
            throw e;
        }
    }

    public void start() {
        startTime = System.currentTimeMillis();
        lastProcessingTime = startTime;
        while(!stopped) {
            long currentTime = System.currentTimeMillis();
            if(currentTime - lastProcessingTime >= timeStep) {
                try {
                    process(currentTime);
                    lastProcessingTime = currentTime;
                } catch (InterruptedException e) {
                    System.out.println("Game application interrupted!!" + e);
                }
            }
        }
    }

    public void end() {
        stopped = true;
        endTime = System.currentTimeMillis();
        lastProcessingTime = endTime;
    }
}
