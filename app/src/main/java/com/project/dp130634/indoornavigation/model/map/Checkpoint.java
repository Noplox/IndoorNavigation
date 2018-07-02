package com.project.dp130634.indoornavigation.model.map;

import java.io.Serializable;

public class Checkpoint extends MapElement implements Serializable {
    private static double CHECKPOINT_TRIGGER_RANGE;

    private Level level;
    private Point2d location;

    public Checkpoint() {
    }

    public Checkpoint(Level level, Point2d location) {
        this.level = level;
        this.location = location;
    }

    public static double getCHECKPOINT_TRIGGER_RANGE() {
        return CHECKPOINT_TRIGGER_RANGE;
    }

    public static void setCHECKPOINT_TRIGGER_RANGE(double CHECKPOINT_TRIGGER_RANGE) {
        Checkpoint.CHECKPOINT_TRIGGER_RANGE = CHECKPOINT_TRIGGER_RANGE;
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
