package com.project.dp130634.indoornavigation.map;

import java.io.Serializable;

public class Checkpoint implements Serializable {
    private static double CHECKPOINT_TRIGGER_RANGE;

    private Level level;
    private Point2d location;
}
