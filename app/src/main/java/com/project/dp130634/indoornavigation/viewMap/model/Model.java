package com.project.dp130634.indoornavigation.viewMap.model;

import com.project.dp130634.indoornavigation.model.map.Level;
import com.project.dp130634.indoornavigation.model.map.Location;
import com.project.dp130634.indoornavigation.model.map.Map;
import com.project.dp130634.indoornavigation.model.map.Route;

public class Model {

    public Model() {}

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
        currentLevel = map.getLevels().get(0);
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Route getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(Route selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    private Map map;
    private Level currentLevel;
    private Route selectedRoute;
    private Location myLocation;
}
