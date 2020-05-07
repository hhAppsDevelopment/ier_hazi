package environment.occupants;

import environment.model.TileGraph;
import environment.view.Tile;

import java.awt.*;

public abstract class Agent extends Occupant {
    public Agent(TileGraph tileGraph, Tile currentTile) {
        super(tileGraph, currentTile);
        baseColor = Color.WHITE;
    }

    @Override
    public void step() {

    }
}
