package occupants;

import model.TileGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import view.QuarantineLogger;
import view.Tile;

import java.awt.*;
import java.util.Collections;

public abstract class Occupant {
    public Color getBaseColor() {
        return baseColor;
    }

    public Image getDrawing() {
        return drawing;
    }

    protected Tile currentTile;
    protected Color baseColor;
    protected Image drawing;
    private TileGraph tileGraph;
    private Graph<Tile, DefaultWeightedEdge> graph;

    protected Occupant(TileGraph tileGraph, Tile currentTile) {
        this.currentTile = currentTile;
        this.tileGraph = tileGraph;
        this.graph = tileGraph.getGraph();
    }

    protected Tile getRandomNeighbour() {
        java.util.List<org.jgrapht.graph.DefaultWeightedEdge> list = new java.util.ArrayList<>(tileGraph.getGraph().edgesOf(currentTile));
        Collections.shuffle(list);
        DefaultWeightedEdge edge = list.get(0);
        if(tileGraph.getGraph().getEdgeSource(edge).equals(currentTile)) return  tileGraph.getGraph().getEdgeTarget(edge);
        return tileGraph.getGraph().getEdgeSource(edge);
    }

    private Tile next;
    private int remaining = 0;

    private void moveSelf(Tile to) {
        currentTile.unregisterOccupant(this);
        to.registerOccupant(this);
        currentTile = to;
    }

    public void step() {
        if(next == null) return;
        if(remaining == 1) {
            moveSelf(next);
            next = null;
        }
        else --remaining;
    }

    public void setNext(Tile next) {
        if(graph.containsEdge(currentTile, next)) {
            this.next = next;
            remaining = (int) graph.getEdgeWeight(graph.getEdge(currentTile, next));
        }
        else {
            QuarantineLogger.log("Tile is not adjacent to current one! " + currentTile + " -> " + next);
        }
    }

    protected boolean hasGoal() {
        return next != null;
    }
}
