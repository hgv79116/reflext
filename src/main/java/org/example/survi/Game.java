package org.example.survi;

// a hard design choice is whether I should include information about time
// in the components.
//

import org.example.message.JsonMessage;
import org.example.survi.category.Category;
import org.example.survi.category.CategoryFactory;
import org.example.survi.circle.Circle;
import org.example.survi.circle.CircleFactory;
import org.json.JSONObject;

public class Game extends GameStateBase{
    private final int numCategory;
    private HashMapIndexedComponentStorage<Category> categories = new HashMapIndexedComponentStorage<Category>();
    private HashMapIndexedComponentStorage<Circle> circles = new HashMapIndexedComponentStorage<>();
    private final CategoryFactory categoryFactory = new CategoryFactory();
    private final CircleFactory circleFactory;
    private double circleSpawnRate = 1;

    public Game(int numCategory) {
        this.numCategory = numCategory;
        for(int i = 0; i < numCategory; i++) {
            Category category = categoryFactory.getInstance();
            categories.addInstance(category);
        }

        this.circleFactory = new CircleFactory(
                2,
                2,
                -1,
                -1,
                1,
                1,
            new CategoryGenerator(categories));
    }

    @Override
    public void start() {
        super.start();
        for(Category category: categories) {
            category.start();
        }
    }

    @Override
    public void end() {
        super.end();
        for(Category category: categories) {
            category.end();
        }
        for(Circle circle: circles) {
            circle.end();
        }
    }

    @Override
    public void update(long newTimeStamp) {
        super.update(newTimeStamp);
        for(Category category: categories) {
            category.update(newTimeStamp);
        }
        for(Circle circle: circles) {
            circle.update(newTimeStamp);
        }
    }

    @Override
    public void update(JsonMessage jsonMessage) {

    }

    @Override
    public JSONObject getLastState() {
        JSONObject JSONCategories = new JSONObject();
        JSONObject JSONCircles = new JSONObject();
        JSONObject JSONContent = new JSONObject();

        for(Category category: categories) {
            JSONCategories.put(String.valueOf(category.getId()), category.getLastState());
        }
        for(Circle circle: circles) {
            JSONCircles.put(String.valueOf(circle.getId()), circle.getLastState());
        }

        JSONContent.put("categories", JSONCategories);
        JSONContent.put("circles", JSONCircles);
        return JSONContent;
    }
}
