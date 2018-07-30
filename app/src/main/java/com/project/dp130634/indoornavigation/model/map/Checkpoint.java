package com.project.dp130634.indoornavigation.model.map;

import java.io.Serializable;

public class Checkpoint extends MapElement implements Serializable {
    private static double checkpointTriggerRange;

    private Level level;
    private Point2d location;

    public Checkpoint() {
    }

    public Checkpoint(Level level, Point2d location) {
        this.level = level;
        this.location = location;
    }

    public static double getCheckpointTriggerRange() {
        return checkpointTriggerRange;
    }

    public static void setCheckpointTriggerRange(double checkpointTriggerRange) {
        Checkpoint.checkpointTriggerRange = checkpointTriggerRange;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }
}
