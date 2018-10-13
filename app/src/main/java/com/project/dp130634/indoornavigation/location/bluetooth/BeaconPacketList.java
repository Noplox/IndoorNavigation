package com.project.dp130634.indoornavigation.location.bluetooth;

import com.project.dp130634.indoornavigation.model.map.BluetoothBeacon;

import java.util.LinkedList;

public class BeaconPacketList{

    /**
     * Time window in which packets inside list are valid
     */
    private final long TIME_WINDOW = 3 * 1000;

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
        BeaconPacket bestRSSIPacket = null;

        for(int i = 0; i < packets.size(); i++) {
            BeaconPacket cur = packets.get(i);
            if(cur.getTimestamp() < currentTime - TIME_WINDOW) {
                packets.remove(i--);
                continue;
            } else {
                if(bestRSSIPacket == null || cur.getRssi() > bestRSSIPacket.getRssi())
                bestRSSIPacket = cur;
            }
        }

        if(bestRSSIPacket == null) {
            return Double.NaN;
        } else {
            return bestRSSIPacket.getDistance();
        }
    }

    public BluetoothBeacon getBeacon() {
        return beacon;
    }

    public void setBeacon(BluetoothBeacon beacon) {
        this.beacon = beacon;
    }
}
