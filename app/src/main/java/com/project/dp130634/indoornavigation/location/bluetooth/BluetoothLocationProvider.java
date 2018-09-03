package com.project.dp130634.indoornavigation.location.bluetooth;

import android.app.Service;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.project.dp130634.indoornavigation.location.LocationChangeListener;
import com.project.dp130634.indoornavigation.location.LocationProvider;
import com.project.dp130634.indoornavigation.location.bluetooth.util.LocationCalculator;
import com.project.dp130634.indoornavigation.model.map.BluetoothBeacon;
import com.project.dp130634.indoornavigation.model.map.Location;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BluetoothLocationProvider extends Service implements LocationProvider, BluetoothLocationScanner.LocationScanListener {

    private final String LOG_TAG = "indoornavigation";

    private BluetoothLocationScanner bluetoothLocationScanner;
    private Map<BeaconId, BeaconPacketList> bluetoothBeacons;
    private List<LocationChangeListener> locationChangeListeners;
    private Location currentLocation;

    private class BeaconId {
        UUID uuid;
        int major;
        int minor;
        BeaconId(){}

        BeaconId(UUID uuid, int major, int minor) {
            this.uuid = uuid;
            this.major = major;
            this.minor = minor;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof BeaconId)) {
                return false;
            }
            BeaconId other = (BeaconId) obj;
            return (
                    other.uuid.equals(this.uuid) &&
                    other.major == this.major &&
                    other.minor == this.minor
            );
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid, major, minor);
        }

        @Override
        public String toString() {
            return "UUID="+uuid + " major="+major + " minor="+minor;
        }
    }

    public BluetoothLocationProvider(Context mainContext) {
        bluetoothLocationScanner = new BluetoothLocationScanner(mainContext, this);
        bluetoothLocationScanner.scanLeDevices();   //Start scanning
        bluetoothBeacons = new HashMap<>();
        locationChangeListeners = new LinkedList<>();
        currentLocation = new Location();
        currentLocation.setAccuracyX(100);
        currentLocation.setAccuracyY(100);
        currentLocation.setAccuracyZ(100);
        currentLocation.setWeight(0);
    }

    public void addBeacon(Location location, UUID id, int major, int minor, int txPower) {
        BeaconId beaconId = new BeaconId(id, major, minor);
        BeaconPacketList packetList = new BeaconPacketList(
                new BluetoothBeacon(location, id, major, minor, txPower));

        bluetoothBeacons.put(
                beaconId,
                packetList
        );
    }

    public void addBeacon(BluetoothBeacon beacon) {
        BeaconId beaconId = new BeaconId (beacon.getId(), beacon.getMajor(), beacon.getMinor());

        BeaconPacketList packetList = new BeaconPacketList(beacon);

        bluetoothBeacons.put(
                new BeaconId (
                    beacon.getId(),
                    beacon.getMajor(),
                    beacon.getMinor()
                ),
                packetList);
    }

    public void clearBeacons() {
        bluetoothBeacons.clear();
    }

    @Override
    public Location getLocation() {
        return currentLocation;
    }

    @Override
    public void addLocationChangeListener(LocationChangeListener locationChangeListener) {
        locationChangeListeners.add(locationChangeListener);
    }

    /**
     * Called when a beacon packet is received. Adds the scan result into records, and adjusts location.
     * */
    @Override
    public void onScanResult(ScanResult scanResult) {
        //Find beacon which sent the packet (by id)
        boolean success = decodeBeaconPacket(scanResult);
        if(!success) {
            return; //Scanned beacon is not in our map
        }

        //Adjust projected location
        calculateLocation();

//        Log.d(LOG_TAG, "rssi: " + rssi);
//        Log.d(LOG_TAG, "(" + currentLocation.getX() + ", " + currentLocation.getY() + ", " + currentLocation.getZ() + ")");
//        Log.d(LOG_TAG, "accuracy: (" + currentLocation.getAccuracyX() + ", " + currentLocation.getAccuracyY() + ", " + currentLocation.getAccuracyZ() + ")");

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


    private boolean decodeBeaconPacket(ScanResult scanResult) {
        try {

            BeaconId beaconId = new BeaconId();
            byte[] values = scanResult.getScanRecord().getBytes();

            int i = 1;
            for(; i < values.length; i++) {
                if(values[i - 1] == 0x02 && values[i] == 0x15) {
                    i++;
                    break;
                }
            }

            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.flip();
            long mostSigBits = buffer.getLong();

            buffer.clear();
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.put(values[i++]);
            buffer.flip();
            long leastSigBits = buffer.getLong();

            beaconId.uuid =  new UUID(mostSigBits, leastSigBits);
            int majorHigherByte = values[i++] & 0xFF;
            int majorLowerByte = values[i++] & 0xFF;
            beaconId.major = majorHigherByte * 256 + majorLowerByte;

            int minorHigherByte = values[i++] & 0xFF;
            int minorLowerByte = values[i++] & 0xFF;
            beaconId.minor = minorHigherByte * 256 + minorLowerByte;

            int txPower = values[i];

            //Add new scan record
            int rssi = scanResult.getRssi();
            BeaconPacketList beaconPacketList = bluetoothBeacons.get(beaconId);
            if(beaconPacketList == null) {
                return false; //Happens if beacon that sent the packet is not added into the list of beacons
            }

            beaconPacketList.addBeaconPacket(
                    new BeaconPacket(
                            rssi,
                            txPower
                    )
            );

            Log.d(LOG_TAG, "Major: " + beaconPacketList.getBeacon().getMajor() + " rssi: " + rssi);

            return true;
        } catch(ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void calculateLocation() {

        Location newLocation = LocationCalculator.calculateLocation(bluetoothBeacons.values().toArray(new BeaconPacketList[bluetoothBeacons.size()]));
        if(newLocation != null) {
            currentLocation = newLocation;
        }
    }
}
