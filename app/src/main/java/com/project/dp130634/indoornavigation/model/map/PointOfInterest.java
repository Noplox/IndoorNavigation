package com.project.dp130634.indoornavigation.model.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PointOfInterest extends MapElement implements Serializable {
    private Point2d location;
    private String name;
    private String contentURL;

    public PointOfInterest() {
    }

    public PointOfInterest(Point2d location, String name, String contentURL) {
        this.location = location;
        this.name = name;
        this.contentURL = contentURL;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentURL() {
        return contentURL;
    }

    public void setContentURL(String contentURL) {
        this.contentURL = contentURL;
    }

    @Override
    public String toString() {
        return "Point of interest \"" + name + '\"';
    }
}
