package model;

import view.Tile;

import java.awt.*;
import java.util.ArrayList;

public abstract class Premise {
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public Tile getRandomTile() {
        int i = (int) (Math.random()*tiles.size());
        return tiles.get(i);
    }

    private ArrayList<Tile> tiles;
    private ArrayList<Tile> doors;

    public Premise() {
        tiles = new ArrayList<>();
        doors = new ArrayList<>();
    }

    public void addTile(Color color, Tile tile) {
        if(color.equals(new Color(150, 75, 0))) {
            doors.add(tile);
        }
        else {
            tiles.add(tile);
        }
    }
    public void setLocked(boolean locked) {
        for(Tile door : doors) {
            door.setLamp(locked);
        }
    }


}
