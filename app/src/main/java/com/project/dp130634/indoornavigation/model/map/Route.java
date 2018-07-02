package com.project.dp130634.indoornavigation.model.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Route extends MapElement implements Serializable {
    public enum CheckpointMissBehaviour {ENFORCE_RETURN, HOP_TO_NEXT};
    private List<Checkpoint> checkpoints;
    private String name;
    private CheckpointMissBehaviour checkpointMissBehaviour;

    public Route() {
        checkpoints = new ArrayList<>();
        checkpointMissBehaviour = CheckpointMissBehaviour.HOP_TO_NEXT;
    }

    public Route(String name) {
        this.name = name;
        checkpoints = new ArrayList<>();
        checkpointMissBehaviour = CheckpointMissBehaviour.HOP_TO_NEXT;
    }

    public Route(String name, CheckpointMissBehaviour checkpointMissBehaviour) {
        this.name = name;
        this.checkpointMissBehaviour = checkpointMissBehaviour;
        checkpoints = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CheckpointMissBehaviour getCheckpointMissBehaviour() {
        return checkpointMissBehaviour;
    }

    public void setCheckpointMissBehaviour(CheckpointMissBehaviour checkpointMissBehaviour) {
        this.checkpointMissBehaviour = checkpointMissBehaviour;
    }
    
    public void addCheckpoint(Checkpoint p) {
        checkpoints.add(p);
    }

    public Checkpoint[] getCheckpoints() {
        Checkpoint[] retVal = new Checkpoint[checkpoints.size()];
        retVal = checkpoints.toArray(retVal);
        return retVal;
    }

    @Override
    public String toString() {
        return "Route \"" + name + '\"';
    }
    
    
}
