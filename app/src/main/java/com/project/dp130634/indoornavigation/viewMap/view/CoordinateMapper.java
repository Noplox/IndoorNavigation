package com.project.dp130634.indoornavigation.viewMap.view;


import android.graphics.Point;

import com.project.dp130634.indoornavigation.model.map.Point2d;

public class CoordinateMapper {
    private static final double PIXEL_COORDINATE_RATIO = 100;  //100px = 1m
    
    private static CoordinateMapper instance = null;
    
    private double zoomPercent = 100;
    private final Point translation = new Point(0, 0);
    private int rotation = 0;
    private Point canvasSize;
    
    /**
     * Maps point on canvas to real model coordinates
     * @param point on canvas
     * @return The real position of the canvas point in the model
     */
    public Point2d mapPoint(Point point) {
        Point2d retVal = new Point2d();
        double zoomRatio = zoomPercent / 100;
        point = unrotatePoint(point);
        retVal.setX((point.x - translation.x) / (zoomRatio * PIXEL_COORDINATE_RATIO));
        retVal.setY((point.y - translation.y) / (zoomRatio * PIXEL_COORDINATE_RATIO));
        return retVal;
    }
    
    /**
     * Maps real model coordinates to screen pixels
     * @param coordinates the real coordinates of the object
     * @return Point on canvas
     */
    public Point mapCoordinates(Point2d coordinates) {
        Point retVal = new Point();
        double zoomRatio = zoomPercent / 100;
        //translation
        retVal.x = (int) ((coordinates.getX() * zoomRatio * PIXEL_COORDINATE_RATIO) + translation.x);
        retVal.y = (int) ((coordinates.getY() * zoomRatio * PIXEL_COORDINATE_RATIO) + translation.y);
        //rotation
        
        retVal = rotatePoint(retVal);
        return retVal;
    }
    
    /**
     * Maps real model coordinates to screen pixels without applying rotation.
     * Used for drawing images with native g2d rotation
     * @param coordinates the real coordinates of the object
     * @return Point on canvas
     */
    public Point mapCoordinatesNoRotation(Point2d coordinates) {
        Point retVal = new Point();
        double zoomRatio = zoomPercent / 100;
        //translation
        retVal.x = (int) ((coordinates.getX() * zoomRatio * PIXEL_COORDINATE_RATIO) + translation.x);
        retVal.y = (int) ((coordinates.getY() * zoomRatio * PIXEL_COORDINATE_RATIO) + translation.y);

        return retVal;
    }
    
    public void reset() {
        rotation = 0;
        translation.x = 0;
        translation.y = 0;
        zoomPercent = 100;
    }
    
    public int getRotation() {
        return rotation;
    }
    
    public Point scaleLength(Point2d realLength) {
        Point retVal = new Point();
        double zoomRatio = zoomPercent / 100;
        retVal.x = (int) (realLength.getX() * zoomRatio * PIXEL_COORDINATE_RATIO);
        retVal.y = (int) (realLength.getY() * zoomRatio * PIXEL_COORDINATE_RATIO);
        return retVal;
    }

    public void zoomOut(int percent) {
        zoomPercent -= percent;
        if(zoomPercent < 10) {
            zoomPercent = 10;
        }
    }

    public void zoomIn(int percent) {
        zoomPercent += percent;
    }
    
    public void translate(Point translation) {
        translation = rotateTranslation(translation);
        this.translation.x += translation.x;
        this.translation.y += translation.y;
    }
    
    public void rotate(int degrees) {
        // [-180, 180]
        rotation += degrees;
        if(rotation >= 180) {
            rotation = -180;
        }
        if(rotation <= -180 && degrees < 0) {
            rotation = 180;
        }
    }
    
    private CoordinateMapper() {}
    
    public static CoordinateMapper getInstance() {
        if (instance == null) {
            instance = new CoordinateMapper();
        }
        return instance;
    }

    public void setCanvasSize(Point size) {
        canvasSize = size;
    }

    private Point rotatePoint(Point toRotate) {
        if(canvasSize != null) {
            Point canvasCenter = new Point(canvasSize.x/2, canvasSize.y/2);
            //System.out.println("canvas center: (" + canvasCenter.x +", " + canvasCenter.y + ")");
            Point retVal = new Point();
            double d = pointDistance(canvasCenter, toRotate);
            if(d == 0 || Double.isNaN(d)) {
                return toRotate;
            }
            double theta = Math.toRadians(rotation);
            int xa = toRotate.x - canvasCenter.x;
            int ya = toRotate.y - canvasCenter.y;
            
            int xb = (int)(xa*Math.cos(theta) - ya*Math.sin(theta));
            int yb = (int)(xa*Math.sin(theta) + ya*Math.cos(theta));
            
            retVal.x = xb + canvasCenter.x;
            retVal.y = yb + canvasCenter.y;

            return retVal;
        } else {
            return toRotate;
        }
    }
    
    private Point rotateTranslation(Point toRotate) {
        Point retVal = new Point();
        double d = pointDistance(toRotate, new Point(0, 0));
        if(d == 0 || Double.isNaN(d)) {
                return toRotate;
        }
        double theta = Math.toRadians(-rotation);
        retVal.x = (int)Math.round(toRotate.x*Math.cos(theta) - toRotate.y*Math.sin(theta));
        retVal.y = (int)Math.round(toRotate.x*Math.sin(theta) + toRotate.y*Math.cos(theta));
        
        return retVal;
    }
    
    private Point unrotatePoint(Point toRotate) {
        if(canvasSize != null) {
            Point canvasCenter = new Point(canvasSize.x/2, canvasSize.y/2);
            //System.out.println("canvas center: (" + canvasCenter.x +", " + canvasCenter.y + ")");
            Point retVal = new Point();
            double d = pointDistance(canvasCenter, toRotate);
            if(d == 0 || Double.isNaN(d)) {
                System.out.println("d=" + d);
                return toRotate;
            }
            double theta = Math.toRadians(-rotation);
            int xa = toRotate.x - canvasCenter.x;
            int ya = toRotate.y - canvasCenter.y;
            
            int xb = (int)(xa*Math.cos(theta) - ya*Math.sin(theta));
            int yb = (int)(xa*Math.sin(theta) + ya*Math.cos(theta));
            
            retVal.x = xb + canvasCenter.x;
            retVal.y = yb + canvasCenter.y;

            return retVal;
        } else {
            return toRotate;
        }
    }
    
    private int findRotatedQuadrant(double angleRad) {
        double angle = Math.toDegrees(angleRad);
        if(angle < 0) {
            angle = 360 + angle;
        }
        if(angle > 360) {
            angle -= 360;
        }
        if(angle > 0 && angle <= 90) {
            return 1;
        }
        if(angle > 90 && angle <= 180) {
            return 2;
        }
        if(angle > 180 && angle <= 270) {
            return 3;
        }
        if(angle > 170 && angle <= 360) {
            return 4;
        }
        throw new IllegalArgumentException();
    }

    public void setZoomPercent(double zoomPercent) {
        this.zoomPercent = zoomPercent;
    }

    public double getZoomPercent() {
        return zoomPercent;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public static double pointDistance(Point a, Point b){
        return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
    }
    
    public static double pointDistance(Point2d a, Point2d b){
        return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
    }
}
