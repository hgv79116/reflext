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
        ContinuousCoordinate _coordinate= ContinuousCoordinate.randomInstance(MIN_SPAWN_X + _radius, MAX_SPAWN_X - _radius, MIN_SPAWN_Y + _radius, MAX_SPAWN_Y - _radius);
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

    public int getCategory() {
        return category;
    }

    @Override
    public void update(long newTimeStamp) {
        System.out.println("updateeee: " + newTimeStamp);
        long maxTimeStamp = getStartTime() + LIFE_TIME;
        if(newTimeStamp > maxTimeStamp) {
            newTimeStamp = maxTimeStamp;
        }
        super.update(newTimeStamp);

        if(newTimeStamp >= maxTimeStamp) {
            end(newTimeStamp);
        }
    }

    public void hit(long timeStamp) {
        update(timeStamp);
        end(timeStamp);
    }

    @Override
    public JSONObject getLastState() {
        JSONObject JSONContent = new JSONObject();
        JSONContent.put("lifetime", LIFE_TIME);
        JSONContent.put("timePassed", (lastTimeStamp - startTime));
        return JSONContent;
    }
}
