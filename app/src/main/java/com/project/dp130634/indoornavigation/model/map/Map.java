package com.project.dp130634.indoornavigation.model.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Map implements Serializable {
    private final List<Level> levels;
    private final List<Route> routes;
    private final List<Elevator> elevators;
    private String name;
    private int compassRotation;

    public Map(String name) {
        this.levels = new ArrayList<>();
        this.routes = new ArrayList<>();
        this.elevators = new ArrayList<>();
        this.name = name;
    }

    public Map(List<Level> levels, List<Route> routes, List<Elevator> elevators) {
        this.levels = levels;
        this.routes = routes;
        this.elevators = elevators;
    }

    public List<Level> getLevels() {
        return levels;
    }
    
    public void addLevel(Level level) {
        this.levels.add(level);
    }

    public List<Route> getRoutes() {
        return routes;
    }
    
    public void addRoute(Route route) {
        this.routes.add(route);
    }
    
    public List<Elevator> getElevators() {
        return elevators;
    }
    
    public void addElevator(Elevator elevator) {
        this.elevators.add(elevator);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompassRotation() {
        return compassRotation;
    }

    public void setCompassRotation(int compassRotation) {
        this.compassRotation = compassRotation;
    }
}
