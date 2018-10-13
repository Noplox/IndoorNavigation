package com.project.dp130634.indoornavigation.location.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;

public class BluetoothLocationScanner extends ScanCallback {
    interface LocationScanListener {
        void onScanResult(ScanResult scanResult);
    }

    private BluetoothLeScanner bluetoothLeScanner;
    private LocationScanListener locationScanListener;

    public BluetoothLocationScanner(Context context, LocationScanListener locationScanListener) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            throw new IllegalStateException("Bluetooth must be enabled before constructing object");
        }
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        this.locationScanListener = locationScanListener;
    }

    public void scanLeDevices() {
        bluetoothLeScanner.startScan(this);
    }

    public void stopScan() {
        bluetoothLeScanner.stopScan(this);
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        locationScanListener.onScanResult(result);
    }
}
