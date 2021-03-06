package environment.model;

import environment.view.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableList;

public abstract class Premise {

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public ArrayList<Tile> getDoors() {
        return doors;
    }
    
    public int indexOf(Tile tile) {
    	int index=tiles.indexOf(tile);
    	if(index >=0) return index;
    	return tiles.size()+doors.indexOf(tile);
    }
    
    public Tile getTile(int index) {
    	if(index<tiles.size()) {
    		return tiles.get(index);
    	} else {
    		return doors.get(index-tiles.size());
    	}
    }
        
    public Tile getRandomTile() {
        int i = (int) (Math.random()*tiles.size());
        return tiles.get(i);
    }

    private ArrayList<Tile> tiles;
    private ArrayList<Tile> doors;

    public Collection<Tile> getContagious() {
        return contagious;
    }

    private Set<Tile> contagious;

    public Premise() {
        tiles = new ArrayList<>();
        doors = new ArrayList<>();
        contagious = new HashSet<>();
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

    public void addContagious(Tile currentTile) {
        contagious.add(currentTile);
    }
}
