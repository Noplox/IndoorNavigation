package com.project.dp130634.indoornavigation.location.bluetooth;

import android.util.Log;

import com.project.dp130634.indoornavigation.model.map.BluetoothBeacon;

public class BeaconPacket {
    final static double ENVIRONMENTAL_FACTOR = 2.0;

    private int rssi;
    private int txPower;
    private long timestamp;

    public BeaconPacket(int rssi, int txPower) {
            this.rssi = rssi;
            this.txPower = txPower;
            this.timestamp = System.currentTimeMillis();
    }

    public BeaconPacket(int rssi, int txPower, long timestamp) {
        this.rssi = rssi;
        this.txPower = txPower;
        this.timestamp = timestamp;
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

        return Math.pow(10, (txPower - (double)rssi) / (10d * ENVIRONMENTAL_FACTOR));
    }
}
