package org.example.survi.utils;

import java.util.Random;

public class ContinuousCoordinate {
    private static Random RANDOM = new Random();
    private double x, y;

    public static ContinuousCoordinate randomInstance(double minX, double maxX, double minY, double maxY) {
        double _x = RANDOM.nextDouble() * (maxX - minX) + minX;
        double _y = RANDOM.nextDouble() * (maxY - minY) + minY;
        return new ContinuousCoordinate(_x, _y);
    }

    public ContinuousCoordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void translate(ContinuousCoordinate o) {
        this.x += o.x;
        this.y += o.y;
    }
}
