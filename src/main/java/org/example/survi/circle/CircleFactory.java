package org.example.survi.circle;

import org.example.survi.CategoryGenerator;
import org.example.survi.IndexedComponentFactory;
import org.example.survi.category.CategoryFactory;
import org.example.survi.utils.ContinuousCoordinate;

public class CircleFactory implements IndexedComponentFactory<Circle> {
    private final int defaultLifetime;
    private final double defaultRadius;
    private final double minSpawnX;
    private final double maxSpawnX;
    private final double minSpawnY;
    private final double maxSpawnY;
    private CategoryGenerator categoryGenerator;

    private int idCounter = 0;

    public CircleFactory(int defaultLifetime,
                         double defaultRadius,
                         double minSpawnX,
                         double minSpawnY,
                         double maxSpawnX,
                         double maxSpawnY,
                         CategoryGenerator categoryGenerator) {
        this.defaultLifetime = defaultLifetime;
        this.defaultRadius = defaultRadius;
        this.minSpawnX = minSpawnX;
        this.minSpawnY = minSpawnY;
        this.maxSpawnX = maxSpawnX;
        this.maxSpawnY = maxSpawnY;
        this.categoryGenerator = categoryGenerator;
    }

    @Override
    public Circle getInstance() {
        return new Circle(
                idCounter++,
                defaultLifetime,
                false,
                defaultRadius,
                new ContinuousCoordinate(
                        Math.random() * (maxSpawnX - minSpawnX) + minSpawnX,
                        Math.random() * (maxSpawnY - minSpawnY) + minSpawnY),
                categoryGenerator.getRandomInstance()
                );
    }
}