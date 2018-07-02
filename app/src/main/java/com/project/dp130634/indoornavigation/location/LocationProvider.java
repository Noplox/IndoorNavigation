package com.project.dp130634.indoornavigation.location;

import com.project.dp130634.indoornavigation.model.map.Location;

public interface LocationProvider {
    Location getLocation();
    void addLocationChangeListener(LocationChangeListener l);
}
