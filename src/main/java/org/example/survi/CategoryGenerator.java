package org.example.survi;

import org.example.survi.category.Category;

import java.util.ArrayList;
import java.util.Random;

public class CategoryGenerator {
    private final ArrayList<Category> categories;
    private Random random = new Random();
    public CategoryGenerator(IndexedComponentStorage<Category> categories) {
        this.categories = new ArrayList<>();
        for(Category category: categories.getAllInstances()) {
            this.categories.add(category);
        }
    }
    public Category getRandomInstance() {
        int numCategory = categories.size();
        assert (numCategory > 0);

        return categories.get(random.nextInt(numCategory));
    }

}
