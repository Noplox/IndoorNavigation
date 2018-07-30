package com.project.dp130634.indoornavigation.model.map;

import java.io.Serializable;
import java.util.UUID;


public class BluetoothBeacon extends MapElement implements Serializable {
    static final long serialVersionUID =-7437707001096879816L;

    private Location location;
    private UUID id;
    private int major, minor;

    public BluetoothBeacon(Location location, UUID id, int major, int minor, int txPower) {
        this.location = location;
        this.id = id;
        this.major = major;
        this.minor = minor;
    }

    public BluetoothBeacon() {}

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    @Override
    public String toString() {
        return "BluetoothBeacon{" + "id=" + id + ", major=" + major + ", minor=" + minor + '}';
    }
    
    
}
