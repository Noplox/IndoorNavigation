package com.project.dp130634.indoornavigation.map;

import java.io.Serializable;

public class Point2d implements Serializable {
    public double x, y;

    public Point2d(){}

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
