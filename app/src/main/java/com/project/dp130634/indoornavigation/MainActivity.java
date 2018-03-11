package com.project.dp130634.indoornavigation;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.dp130634.indoornavigation.location.bluetooth.BluetoothLocationScanner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BluetoothLocationScanner.LocationScanListener {

    private static final int REQUEST_ENABLE_BT = 1;

    private ArrayAdapter<String> scanResultAdapter;
    private List<String> scanResultList;
    private BluetoothLocationScanner bleScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView scanResultListView = (ListView) findViewById(R.id.scanListView);
        scanResultList = new ArrayList<>();
        scanResultAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scanResultList);
        scanResultListView.setAdapter(scanResultAdapter);

        if(!bluetoothIsOn()) {
            enableBluetooth();
        } else {
            constructBleScanner();
        }
    }

    @Override
    public void onScanResult(ScanResult scanResult) {
        String newResult = "TxPower: " + scanResult.getScanRecord().getTxPowerLevel()
                + "RSSI : " + scanResult.getRssi();


        int test = scanResult.getScanRecord().getBytes().length;
        
        scanResultList.add(newResult);
        scanResultAdapter.notifyDataSetChanged();
    }

    private void constructBleScanner() {
        bleScanner = new BluetoothLocationScanner(this, this);
        bleScanner.scanLeDevices();
    }

    private boolean bluetoothIsOn() {
        return false;
    }

    private void enableBluetooth() {
        if(!bluetoothIsOn()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            constructBleScanner();
        }
    }
}
