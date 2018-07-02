package com.project.dp130634.indoornavigation.model.map;

import java.io.Serializable;
import java.util.List;

public class Elevator extends MapElement implements Serializable {
    private Point2d location;
    private List<Level> levels;

    public Elevator() {
    }

    public Elevator(Point2d location, List<Level> levels) {
        this.location = location;
        this.levels = levels;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        return "Elevator{" + "location=" + location + ", levels=" + levels + '}';
    }
    
    
}
