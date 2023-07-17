package org.example.survi.circle;

import org.example.survi.GameStateBase;
import org.example.survi.HashMapIndexedComponentStorage;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class CircleController extends GameStateBase {
    private final int MAX_NUM_CIRCLE = 20;
    private final int CIRCLE_SPAWN_COOLDOWN = 500;
    private final int NUM_CATEGORY;
    private final int lastSpawnTime = -1;
    private int circleCounter = 0;
    private final HashMapIndexedComponentStorage<Circle> circles = new HashMapIndexedComponentStorage<>();

    public CircleController(int numCategory) {
        this.NUM_CATEGORY = numCategory;
    }

    @Override
    public void end(long timeStamp) {
        super.end(timeStamp);

        for(Circle circle: circles) {
            circle.end(timeStamp);
        }
    }



    private void spawnNewCircle(long newTimeStamp) {

        Circle newCircle = Circle.randomInstance(circleCounter++, NUM_CATEGORY);
        circles.addInstance(newCircle);
        newCircle.start(newTimeStamp);
    }

    @Override
    public void update(long newTimeStamp) {
        super.update(newTimeStamp);

        if(lastSpawnTime == -1 || lastSpawnTime + CIRCLE_SPAWN_COOLDOWN <= newTimeStamp) {
            spawnNewCircle(newTimeStamp);
        }

        for(Circle circle: circles) {
            circle.update(newTimeStamp);
            if(circle.hasEnded()) {
                circles.removeInstanceById(circle.getId());
            }
        }
    }

    @Override
    public JSONObject getLastState() {
        JSONObject ret = new JSONObject();
        for(Circle circle: circles) {
            ret.put(String.valueOf(circle.getId()), circle.getLastState());
        }
        return ret;
    }

    public Circle getCircleById(int id) {
        return circles.getInstanceById(id);
    }
}
