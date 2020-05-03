package model;

import view.Tile;

import java.util.ArrayList;

public abstract class Premise {
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    private ArrayList<Tile> tiles;

    public Premise() {
        tiles = new ArrayList<>();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }


}
