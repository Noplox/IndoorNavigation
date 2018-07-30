package com.project.dp130634.indoornavigation;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.project.dp130634.indoornavigation.location.LocationChangeListener;
import com.project.dp130634.indoornavigation.location.bluetooth.BluetoothLocationProvider;
import com.project.dp130634.indoornavigation.mapDownload.QrScanActivity;
import com.project.dp130634.indoornavigation.model.map.Location;
import com.project.dp130634.indoornavigation.viewMap.view.MapActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements LocationChangeListener {

    private static final int REQUEST_QR_SCAN = 3;
    private static final int REQUEST_CHOOSE_MAP = 2;
    private static final int REQUEST_ENABLE_BT = 1;

    public static final String KEY_MAP_URI = "com.project.dp130634.indoornavigation.MAP_URI";

    private ArrayAdapter<String> scanResultAdapter;
    private List<String> scanResultList;
    private BluetoothLocationProvider locationProvider;

    private int numOfReads = 0;
    private double avg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView scanResultListView = (ListView) findViewById(R.id.scanListView);
        scanResultList = new ArrayList<>();
        scanResultAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scanResultList);
        scanResultListView.setAdapter(scanResultAdapter);

        if(!(getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))) {
            Toast.makeText(this, "No BLE", Toast.LENGTH_LONG);
        }

        enableBluetooth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openMap:
            {
                Intent intent = new Intent(this, QrScanActivity.class);

                startActivityForResult(intent, REQUEST_QR_SCAN);

                //File chooser intent below
//                Intent intent = new Intent()
//                        .setType("*/*")
//                        .setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(intent, "Select a map file"), REQUEST_CHOOSE_MAP);
//                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void enableBluetooth() {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT && resultCode != Activity.RESULT_OK) {
            enableBluetooth();
        }
        if(requestCode == REQUEST_CHOOSE_MAP && resultCode == Activity.RESULT_OK) {
            Uri selectedFileUri = data.getData();
            //File mapFile = new File(selectedFileUri.getPath());
            Intent mapIntent = new Intent(this, MapActivity.class)
                    .putExtra(KEY_MAP_URI, selectedFileUri);

            startActivity(mapIntent);
        }
    }

    @Override
    public void onLocationChange(Location l) {
        DecimalFormat decFormat = new DecimalFormat("###.###");

        avg = ((avg * numOfReads) + l.getAccuracyX()) / (++numOfReads);

        String newResult = "Location: (" + decFormat.format(l.getX()) + ", " + decFormat.format(l.getY()) + ", " + decFormat.format(l.getZ()) + ")\n" +
                "Accuracy: (" + decFormat.format(l.getAccuracyX()) + ", " + decFormat.format(l.getAccuracyY()) + ", " +  decFormat.format(l.getAccuracyZ()) + ")\n" +
                "Avg: " + decFormat.format(avg);

        scanResultList.add(newResult);
        scanResultAdapter.notifyDataSetChanged();

    }
}
