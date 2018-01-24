package com.project.dp130634.indoornavigation.location;

/**
 * Created by John on 24-Jan-18.
 */

public class IndoorLocation {

    public IndoorLocation(double x, double y, double z, double accuracy) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.accuracy = accuracy;
    }

    public IndoorLocation() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    private double x, y, z;
    private double accuracy;
}
