package com.project.dp130634.indoornavigation.location.bluetooth;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.project.dp130634.indoornavigation.location.Location;
import com.project.dp130634.indoornavigation.location.LocationChangeListener;
import com.project.dp130634.indoornavigation.location.LocationProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BluetoothLocationProvider extends Service implements LocationProvider, BluetoothLocationScanner.LocationScanListener {

    private BluetoothLocationScanner bluetoothLocationScanner;
    private Map<UUID, BluetoothBeacon> bluetoothBeacons;
    private Map<UUID, BeaconPacket> scanRecords;
    private List<LocationChangeListener> locationChangeListeners;
    private Location currentLocation;

    public BluetoothLocationProvider() {
        bluetoothLocationScanner = new BluetoothLocationScanner(this, this);
        bluetoothBeacons = new HashMap<>();
        scanRecords = new HashMap<>();
        locationChangeListeners = new LinkedList<>();
        currentLocation = new Location();
        currentLocation.setAccuracyX(100);
        currentLocation.setAccuracyY(100);
        currentLocation.setAccuracyZ(100);
        currentLocation.setWeight(0);
    }

    public void addBeacon(Location location, UUID id, int txPower) {
        bluetoothBeacons.put(id, new BluetoothBeacon(location, id, txPower));
    }

    @Override
    public Location getLocation() {
        return currentLocation;
    }

    @Override
    public void addLocationChangeListener(LocationChangeListener l) {
        locationChangeListeners.add(l);
    }

    /**
     * Called when a beacon packet is received. Adds the scan result into records, and adjusts location.
     * */
    @Override
    public void onScanResult(ScanResult scanResult) {
        //Find beacon which sent the packet (by id)
        UUID beaconUuid = decodeUuid(scanResult.getScanRecord());

        //Add new scan record
        int rssi = scanResult.getRssi();
        long timestamp = scanResult.getTimestampNanos() / 1000;
        BluetoothBeacon packetSender = bluetoothBeacons.get(beaconUuid);
        if(packetSender == null) {
            return; //Happens if beacon that sent the packet is not added into the list of beacons
        }
        scanRecords.put(beaconUuid, new BeaconPacket(packetSender, rssi, timestamp));

        //Adjust projected location
        calculateLocation();

        //Broadcast new location to LocationChangeListeners
        for(LocationChangeListener cur : locationChangeListeners) {
            cur.onLocationChange(currentLocation);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bluetoothLocationScanner.scanLeDevices();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        bluetoothLocationScanner.stopScan();
    }

    @NonNull
    private UUID decodeUuid(ScanRecord scanRecord) {
        byte[] values = scanRecord.getBytes();
        long mostSigBits = (values[2] << 56) + (values[3] << 48) + (values[4] << 40) + (values[5] << 32)
                + (values[6] << 24) + (values[7] << 16) + (values[8] << 8) + (values[9]);
        long leastSigBits = (values[10] << 56) + (values[11] << 48) + (values[12] << 40) + (values[13] << 32)
                + (values[14] << 24) + (values[15] << 16) + (values[16] << 8) + (values[17]);
        return new UUID(mostSigBits, leastSigBits);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void calculateLocation() {

    }
}
