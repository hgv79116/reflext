package org.example.survi.circle;

import org.example.message.JsonMessage;
import org.example.survi.Game;
import org.example.survi.GameStateBase;
import org.example.survi.IndexedComponent;
import org.example.survi.category.Category;
import org.example.survi.utils.ContinuousCoordinate;
import org.json.JSONObject;

public class Circle extends GameStateBase implements IndexedComponent {
    int id;
    private final long lifetime;
    private long age;
    private boolean alive;
    private final double radius;
    private final ContinuousCoordinate coordinate;
    private final Category category;

    public Circle(int id,
                  long lifetime,
                  boolean alive,
                  double radius,
                  ContinuousCoordinate coordinate,
                  Category category) {
        this.id = id;
        this.lifetime = lifetime;
        this.alive = alive;
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
        return lifetime;
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

    public Category getCategory() {
        return category;
    }

    @Override
    public void update(long newTimeStamp) {
        if(newTimeStamp > startTime + lifetime) {
            alive = false;
        }
        super.update(newTimeStamp);
    }

    @Override
    public void update(JsonMessage jsonMessage) {

    }

    @Override
    public JSONObject getLastState() {
        JSONObject JSONContent = new JSONObject();
        JSONContent.put("alive", alive);
        JSONContent.put("lifetime", lifetime);
        JSONContent.put("timeLeft", alive? lastTimeStamp - startTime: 0);
        return JSONContent;
    }
}
