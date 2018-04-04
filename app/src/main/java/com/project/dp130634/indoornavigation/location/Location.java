package com.project.dp130634.indoornavigation.location;

public class Location {

    public Location(double x, double y, double z, double accuracyX, double accuracyY, double accuracyZ, double viewingAngle, double viewingAccuracy, double weight) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.accuracyX = accuracyX;
        this.accuracyY = accuracyY;
        this.accuracyZ = accuracyZ;
        this.viewingAngle = viewingAngle;
        this.viewingAccuracy = viewingAccuracy;
        this.weight = weight;
        this.timestamp = System.currentTimeMillis();
    }

    public Location() {
        this.accuracyX = 100;
        this.accuracyY = 100;
        this.accuracyZ = 100;
        this.viewingAccuracy = 360;
        this.weight = 1;
        this.timestamp = System.currentTimeMillis();
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

    public double getAccuracyX() {
        return accuracyX;
    }

    public void setAccuracyX(double accuracyX) {
        this.accuracyX = accuracyX;
    }

    public double getAccuracyY() {
        return accuracyY;
    }

    public void setAccuracyY(double accuracyY) {
        this.accuracyY = accuracyY;
    }

    public double getAccuracyZ() {
        return accuracyZ;
    }

    public void setAccuracyZ(double accuracyZ) {
        this.accuracyZ = accuracyZ;
    }

    public double getViewingAngle() {
        return viewingAngle;
    }

    public void setViewingAngle(double viewingAngle) {
        this.viewingAngle = viewingAngle;
    }

    public double getViewingAccuracy() {
        return viewingAccuracy;
    }

    public void setViewingAccuracy(double viewingAccuracy) {
        this.viewingAccuracy = viewingAccuracy;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private double x, y, z;
    private double accuracyX, accuracyY, accuracyZ;
    private double viewingAngle;
    private double viewingAccuracy;
    private double weight;
    private long timestamp;
}
