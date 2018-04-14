package com.project.dp130634.indoornavigation.map;

import java.io.Serializable;
import java.util.List;

/**
 * Class that defines obstacles as a polygon lying on the plane of the floor it's on.
 * */
public class Obstacle implements Serializable {
    private List<Point2d> points;
}
