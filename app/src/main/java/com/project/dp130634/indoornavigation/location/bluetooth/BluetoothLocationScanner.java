package com.project.dp130634.indoornavigation.location.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;

public class BluetoothLocationScanner extends ScanCallback {
    public interface LocationScanListener {
        void onScanResult(ScanResult scanResult);
    }

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean scanning;
    private Handler handler;
    private LocationScanListener locationScanListener;

    private static final long SCAN_PERIOD = 10000;


    public BluetoothLocationScanner(Context context, LocationScanListener locationScanListener) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            throw new IllegalStateException("Bluetooth must be enabled before constructing object");
        }
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        handler = new Handler();
        this.locationScanListener = locationScanListener;
    }

    public void scanLeDevices() {
        bluetoothLeScanner.startScan(this);
        scanning = true;
    }

    public void stopScan() {
        bluetoothLeScanner.stopScan(this);
        scanning = false;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        locationScanListener.onScanResult(result);
    }
}
