package com.project.dp130634.indoornavigation.map;

import com.project.dp130634.indoornavigation.location.bluetooth.BluetoothBeacon;

import java.io.Serializable;
import java.util.List;

public class Level implements Serializable {
    private double floorHeight; //y coordinate of the floor
    private List<Obstacle> obstacles;
    private List<PointOfInterest> pointsOfInterest;
    private List<Staircase> stairs;
    private List<Elevator> elevators;
    private List<BluetoothBeacon> bluetoothBeacons;
}
