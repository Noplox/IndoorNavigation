package com.project.dp130634.indoornavigation.model.map;


import java.io.Serializable;

public class Staircase extends MapElement implements Serializable {
    private Point2d Location;
    private Level upperLevel;
    private Level lowerLevel;

    public Staircase(Point2d Location, Level upperLevel, Level lowerLevel) {
        this.Location = Location;
        this.upperLevel = upperLevel;
        this.lowerLevel = lowerLevel;
    }

    public Staircase(Point2d Location) {
        this.Location = Location;
    }

    public Point2d getLocation() {
        return Location;
    }

    public void setLocation(Point2d Location) {
        this.Location = Location;
    }

    public Level getUpperLevel() {
        return upperLevel;
    }

    public void setUpperLevel(Level upperLevel) {
        this.upperLevel = upperLevel;
    }

    public Level getLowerLevel() {
        return lowerLevel;
    }

    public void setLowerLevel(Level lowerLevel) {
        this.lowerLevel = lowerLevel;
    }

    @Override
    public String toString() {
        return "Staircase{" + "upperLevel=" + upperLevel + ", lowerLevel=" + lowerLevel + '}';
    }
}
