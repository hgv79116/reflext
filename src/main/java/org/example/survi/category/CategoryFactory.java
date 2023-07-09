package org.example.survi.category;

import org.example.survi.HashMapIndexedComponentStorage;
import org.example.survi.IndexedComponentFactory;
import org.example.survi.IndexedComponentStorage;
import org.example.survi.score_controller.LinearSpeedScoreController;

import java.util.Random;

public class CategoryFactory implements IndexedComponentFactory<Category> {
    int idCounter = 0;
    IndexedComponentStorage<Category> generatedCategories
            = new HashMapIndexedComponentStorage<>();

    Random random = new Random();

    @Override
    public Category getInstance() {
        Category category = new Category(
                idCounter,
                idCounter,
                new LinearSpeedScoreController(
                        -1,
                        1,
                        0,
                        0.0003,
                        random.nextInt(0, 1) * 2 - 1));
        idCounter++;
        generatedCategories.addInstance(category);
        return category;
    }
}
