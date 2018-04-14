package com.project.dp130634.indoornavigation.map;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class Staircase implements Serializable {
    private Point2d Location;
    private @Nullable Level upperLevel;
    private @Nullable Level lowerLevel;
}
