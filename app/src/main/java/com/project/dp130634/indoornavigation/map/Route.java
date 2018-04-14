package com.project.dp130634.indoornavigation.map;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable {
    public enum CheckpointMissBehaviour {ENFORCE_RETURN, HOP_TO_NEXT};
    private List<Checkpoint> checkpoints;
    private String name;
    private CheckpointMissBehaviour checkpointMissBehaviour;

}
