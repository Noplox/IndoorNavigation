package com.project.dp130634.indoornavigation.location;

public interface LocationProvider {
    Location getLocation();
    void addLocationChangeListener(LocationChangeListener l);
}
