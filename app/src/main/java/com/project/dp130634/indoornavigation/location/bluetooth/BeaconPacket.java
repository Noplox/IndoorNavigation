package com.project.dp130634.indoornavigation.location.bluetooth;

public class BeaconPacket {
    private BluetoothBeacon beacon;
    private long timestamp;
    private int rssi;

    public BeaconPacket(BluetoothBeacon beacon, int rssi) {
            this.beacon = beacon;
            this.rssi = rssi;
            this.timestamp = System.currentTimeMillis();
    }

    public BeaconPacket(BluetoothBeacon beacon, int rssi, long timestamp) {
        this.beacon = beacon;
        this.timestamp = timestamp;
        this.rssi = rssi;
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

    public double getDistance() {
        double ratio = rssi * 1.0 / beacon.getTxPower();
        if(ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double distacne = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return distacne;
        }
    }
}
