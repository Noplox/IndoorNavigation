package com.project.dp130634.indoornavigation.location.bluetooth;

import com.project.dp130634.indoornavigation.model.map.BluetoothBeacon;

public class BeaconPacket {
    private BluetoothBeacon beacon;
    private long timestamp;
    private int rssi;
    private int txPower;

    public BeaconPacket(BluetoothBeacon beacon, int rssi, int txPower) {
            this.beacon = beacon;
            this.rssi = rssi;
            this.timestamp = System.currentTimeMillis();
            this.txPower = txPower;
    }

    public BeaconPacket(BluetoothBeacon beacon, int rssi, int txPower, long timestamp) {
        this.beacon = beacon;
        this.timestamp = timestamp;
        this.rssi = rssi;
        this.txPower = txPower;
    }

    public BluetoothBeacon getBeacon() {
        return beacon;
    }

    public void setBeacon(BluetoothBeacon beacon) {
        this.beacon = beacon;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public double getDistance() {
        final double ENVIRONMENTAL_FACTOR = 2.0;

        return Math.pow(10, (txPower - (double)rssi) / (10d * ENVIRONMENTAL_FACTOR));
        /*
        double ratio = rssi * 1.0 / beacon.getTxPower();
        if(ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
        */
    }
}
