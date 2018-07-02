package com.project.dp130634.indoornavigation.location;

import com.project.dp130634.indoornavigation.model.map.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationAggregator implements LocationProvider {
    private static final long locationTimeToLiveMs = 2000;
    private static final double accuracyThresholdHorizontal = 10;
    private static final double accuracyThresholdVertical = 10;
    private static final double viewingAccuracyThreshold = 30;

    @Override
    public Location getLocation() {
        return aggregate();
    }

    @Override
    public void addLocationChangeListener(LocationChangeListener l) {
        listeners.add(l);
    }

    public void addLocation(Location l) {
        locations.add(l);
        long currentTime = System.currentTimeMillis();
        for(Location cur : locations) {
            if(currentTime - cur.getTimestamp() > locationTimeToLiveMs) {
                locations.remove(cur);
            }
        }
        Location aggregate = aggregate();
        for(LocationChangeListener cur : listeners) {
            cur.onLocationChange(aggregate);
        }
    }

    private Location aggregate() {
        double x = 0, y = 0, z = 0, accuracyX = 0, accuracyY = 0, accuracyZ = 0;
        double viewingAngle = 0, viewingAccuracy = 0;
        int numX = 0, numY = 0, numZ = 0, numAngle = 0;
        for(Location cur : locations) {
            if(cur.getAccuracyX() < accuracyThresholdHorizontal) {
                numX += cur.getWeight();
                x += cur.getX() * cur.getWeight();
                accuracyX += cur.getAccuracyX() * cur.getWeight();
            }
            if(cur.getAccuracyY() < accuracyThresholdHorizontal) {
                numY += cur.getWeight();
                y += cur.getY() * cur.getWeight();
                accuracyY += cur.getAccuracyY() * cur.getWeight();
            }
            if(cur.getAccuracyZ() < accuracyThresholdVertical) {
                numZ += cur.getWeight();
                z += cur.getZ() * cur.getWeight();
                accuracyZ += cur.getAccuracyZ() * cur.getWeight();
            }
            if(cur.getViewingAccuracy() < viewingAccuracyThreshold) {
                numAngle += cur.getWeight();
                viewingAngle += cur.getViewingAngle() * cur.getWeight();
                viewingAccuracy += cur.getViewingAccuracy() * cur.getWeight();
            }
        }
        x /= numX;
        y /= numY;
        z /= numZ;
        viewingAngle /= numAngle;
        accuracyX /= numX;
        accuracyY /= numY;
        accuracyZ /= numZ;
        viewingAccuracy /= numAngle;

        return new Location(x, y, z, accuracyX, accuracyY, accuracyZ, viewingAngle, viewingAccuracy, 1);
    }

    private List<Location> locations = new ArrayList<>();
    private List<LocationChangeListener> listeners = new ArrayList<>();
}
