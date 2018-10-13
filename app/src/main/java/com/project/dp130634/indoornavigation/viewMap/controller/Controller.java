package com.project.dp130634.indoornavigation.viewMap.controller;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.project.dp130634.indoornavigation.location.LocationChangeListener;
import com.project.dp130634.indoornavigation.location.bluetooth.BluetoothLocationProvider;
import com.project.dp130634.indoornavigation.model.map.BluetoothBeacon;
import com.project.dp130634.indoornavigation.model.map.Level;
import com.project.dp130634.indoornavigation.model.map.Location;
import com.project.dp130634.indoornavigation.model.map.Map;
import com.project.dp130634.indoornavigation.viewMap.ViewInterface;
import com.project.dp130634.indoornavigation.viewMap.model.Model;

public class Controller extends Thread implements LocationChangeListener {

    public int TICK_RATE = 60;

    public Controller(AppCompatActivity activity, ViewInterface view) {
        this.activity = activity;
        this.view = view;
        this.model = new Model();
        this.locationProvider = new BluetoothLocationProvider(activity);
        this.locationProvider.addLocationChangeListener(this);
        start();

    }

    public void setMap(Map map) {
        model.setMap(map);

        locationProvider.clearBeacons();
        for(Level level : map.getLevels()) {
            for(BluetoothBeacon beacon : level.getBluetoothBeacons()) {
                locationProvider.addBeacon(beacon);
            }
        }
    }

    public Model getModel() {
        return model;
    }

    public void stopThread() {
        running = false;
    }

    @Override
    public void run() {
        while(running) {
            try {
                Thread.sleep(1000/TICK_RATE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activity.runOnUiThread(viewRefresher);
        }
    }

    @Override
    public void onLocationChange(Location l) {
        model.setMyLocation(l);
        Level cur = findLocationLevel(l);
        if(cur != null) {
            model.setCurrentLevel(cur);
        }
    }

    @Nullable
    private Level findLocationLevel(Location loc) {
        Level retVal = null;

        for(Level cur : model.getMap().getLevels()) {
            if(retVal == null) {
                if(loc.getZ() > cur.getFloorHeight()) {
                    retVal = cur;
                }
            } else {
                if(loc.getZ() > cur.getFloorHeight() && cur.getFloorHeight() < retVal.getFloorHeight()) {
                    retVal = cur;
                }
            }
        }

        //function returns null if calculated location is below every level
        return retVal;
    }


    private ViewInterface view;
    private Model model;
    private boolean running = true;
    private AppCompatActivity activity;
    private BluetoothLocationProvider locationProvider;
    private Runnable viewRefresher = new Runnable() {
        @Override
        public void run() {
            view.refresh(model);
        }
    };

}
