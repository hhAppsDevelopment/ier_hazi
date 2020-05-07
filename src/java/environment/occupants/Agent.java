package environment.occupants;

import environment.view.PlayField;
import environment.view.Tile;

import java.awt.*;

public abstract class Agent extends Occupant {
    public Agent(PlayField field, Tile currentTile) {
        super(field.getTileGraph(), currentTile);
        baseColor = Color.WHITE;
        field.addAgent(this);
    }

//    @Override
//    public void step() {
//
//    }
}
