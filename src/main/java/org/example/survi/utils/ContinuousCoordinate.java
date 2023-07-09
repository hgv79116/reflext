package org.example.survi.utils;

public class ContinuousCoordinate {
    private double x, y;

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
