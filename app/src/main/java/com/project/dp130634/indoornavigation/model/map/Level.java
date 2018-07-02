package com.project.dp130634.indoornavigation.model.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Level implements Serializable {
    static final long serialVersionUID =-4032048629470738669L;
    
    public class ImageContainer implements Serializable {
        static final long serialVersionUID =6602207808935057987L;
        public transient Bitmap image;
        public Point2d firstCoordinate, lastCoordinate;
        
        public ImageContainer(){}

        public ImageContainer(Bitmap image, Point2d firstCoordinate, Point2d lastCoordinate) {
            this.image = image;
            this.firstCoordinate = firstCoordinate;
            this.lastCoordinate = lastCoordinate;
        }
        
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            if(image != null) {
                out.writeInt(1);
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
            }
        }
        
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            final int imageCnt = in.readInt();
            if(imageCnt == 1) {
                image = BitmapFactory.decodeStream(in);
            }
        }
    }
    
    private double floorHeight; //y coordinate of the floor
    private List<Obstacle> obstacles;
    private List<PointOfInterest> pointsOfInterest;
    private List<Staircase> stairs;
    private List<BluetoothBeacon> bluetoothBeacons;
    private String name;
    //variable to store floorplan in jpg/png
    private ImageContainer image;
/*
    public Level(String name) {
        this.name = name;
        obstacles = new ArrayList<>();
        pointsOfInterest = new ArrayList<>();
        stairs = new ArrayList<>();
        bluetoothBeacons = new ArrayList<>();
    }
*/
    public Level(String name, double floorHeight) {
        this.name = name;
        this.floorHeight = floorHeight;
        obstacles = new ArrayList<>();
        pointsOfInterest = new ArrayList<>();
        stairs = new ArrayList<>();
        bluetoothBeacons = new ArrayList<>();
    }
    
    public double getFloorHeight() {
        return floorHeight;
    }

    public void setFloorHeight(double floorHeight) {
        this.floorHeight = floorHeight;
    }
    
    public void addObstacle(Obstacle o) {
        obstacles.add(o);
    }
    
    public List<Obstacle> getObstacles() {
        return obstacles;
    }
    
    public void addPointOfInterest(PointOfInterest p) {
        pointsOfInterest.add(p);
    }
    
    public List<PointOfInterest> getPointsOfInterest() {
        return pointsOfInterest;
    }
    
    public void addStaircase(Staircase s) {
        stairs.add(s);
    }
    
    public List<Staircase> getStaircases() {
        return stairs;
    }
    
    public void addBluetoothBeacon(BluetoothBeacon b) {
        bluetoothBeacons.add(b);
    }
    
    public List<BluetoothBeacon> getBluetoothBeacons() {
        return bluetoothBeacons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addImage(Bitmap levelImage, Point2d firstCoordinate, Point2d lastCoordinate) {
        image = new ImageContainer(levelImage, firstCoordinate, lastCoordinate);
    }
    
    public ImageContainer getImage() {
        return image;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
