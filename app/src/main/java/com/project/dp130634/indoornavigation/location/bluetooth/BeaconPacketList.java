package com.project.dp130634.indoornavigation.location.bluetooth;

import com.project.dp130634.indoornavigation.model.map.BluetoothBeacon;

import java.util.LinkedList;

public class BeaconPacketList{

    /**
     * Time window in which packets inside list are valid
     */
    private final long TIME_WINDOW = 3000;

    private LinkedList<BeaconPacket> packets;

    private BluetoothBeacon beacon;

    public BeaconPacketList(BluetoothBeacon beacon) {
        this.beacon = beacon;

        packets = new LinkedList<>();
    }

    public void addBeaconPacket(BeaconPacket packet) {
        packets.add(packet);
    }

    public double calculateDistance() {
        long currentTime = System.currentTimeMillis();
        double distance = 0;
        int n = 0;

        for(int i = 0; i < packets.size(); i++) {
            BeaconPacket cur = packets.get(i);
            if(cur.getTimestamp() < currentTime - TIME_WINDOW) {
                packets.remove(i--);
                continue;
            } else {
                distance += cur.getDistance();
                n++;
            }
        }

        return distance / n;
    }

    public BluetoothBeacon getBeacon() {
        return beacon;
    }

    public void setBeacon(BluetoothBeacon beacon) {
        this.beacon = beacon;
    }
}
