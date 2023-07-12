package org.example.survi.circle;

import org.example.message.client_message.ClientMessage;
import org.example.survi.GameStateBase;
import org.example.survi.IndexedComponent;
import org.example.survi.utils.ContinuousCoordinate;
import org.json.JSONObject;

import java.util.Random;

public class Circle extends GameStateBase implements IndexedComponent {

    private static final int MIN_SPAWN_X = -1;
    private static final int MAX_SPAWN_X = 1;
    private static final int MIN_SPAWN_Y = -1;
    private static final int MAX_SPAWN_Y = 1;
    private static final long LIFE_TIME = 2000;

    private static final Random RANDOM = new Random();


    int id;
    private boolean alive;
    private final double radius;
    private final ContinuousCoordinate coordinate;
    private final int category;

    public static Circle randomInstance(int id, int numCategory) {
        double _radius = RANDOM.nextDouble();
        ContinuousCoordinate _coordinate= ContinuousCoordinate.randomInstance(-1 + _radius, -1 + _radius, 1 - _radius, 1 - _radius);
        return new Circle(
                id,
                _radius,
                _coordinate,
                RANDOM.nextInt(numCategory)
        );
    }

    public Circle(int id,
                  double radius,
                  ContinuousCoordinate coordinate,
                  int category) {
        this.id = id;
        this.radius = radius;
        this.coordinate = coordinate;
        this.category = category;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getLifetime() {
        return LIFE_TIME;
    }

    public boolean isAlive() {
        return alive;
    }

    public double getRadius() {
        return radius;
    }

    public ContinuousCoordinate getCoordinate() {
        return coordinate;
    }

    public int getCategory() {
        return category;
    }

    @Override
    public void update(long newTimeStamp) {
        if(newTimeStamp > startTime + LIFE_TIME) {
            alive = false;
        }
        super.update(newTimeStamp);
    }

    @Override
    public void update(ClientMessage clientMessage) {
        super.update(clientMessage);
    }

    @Override
    public JSONObject getLastState() {
        JSONObject JSONContent = new JSONObject();
        JSONContent.put("alive", alive);
        JSONContent.put("lifetime", LIFE_TIME);
        JSONContent.put("timeLeft", alive? lastTimeStamp - startTime: 0);
        return JSONContent;
    }

    public long getExpectedEndTime() {
        return startTime + LIFE_TIME;
    }

    public void hit(long timeStamp) {
        update(timeStamp);
        end();
    }
}
