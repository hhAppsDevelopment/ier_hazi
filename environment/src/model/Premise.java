package model;

import view.Tile;

import java.util.ArrayList;

public abstract class Premise {
    private ArrayList<Tile> tiles;

    public Premise() {
        tiles = new ArrayList<>();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }


}
