package com.project.dp130634.indoornavigation.location;

public class IndoorLocation {

    public IndoorLocation(double x, double y, double z, double accuracy, double viewingAngle, double viewingAccuracy) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.accuracy = accuracy;
        this.viewingAngle = viewingAngle;
        this.viewingAccuracy = viewingAccuracy;
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

    private double x, y, z;
    private double accuracy;
    private double viewingAngle;
    private double viewingAccuracy;
}
