package occupants;

import model.TileGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import view.QuarantineLogger;
import view.Tile;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

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
    protected TileGraph tileGraph;
    private Graph<Tile, DefaultWeightedEdge> graph;
    private DijkstraShortestPath<Tile, DefaultWeightedEdge> dijkstraShortestPath;

    protected Occupant(TileGraph tileGraph, Tile currentTile) {
        this.currentTile = currentTile;
        this.tileGraph = tileGraph;
        this.graph = tileGraph.getGraph();
        this.dijkstraShortestPath = tileGraph.getDijkstraShortestPath();
    }

    private Tile goal;
    private Tile next;
    private List<Tile> pathToGoal;
    private int remaining = 0;

    private void moveSelf(Tile to) {
        currentTile.unregisterOccupant(this);
        to.registerOccupant(this);
        currentTile = to;
    }

    public void step() {
        if(goal == null) return;
        if(remaining == 1) {
            moveSelf(next);
            pathToGoal.remove(currentTile);
            if(pathToGoal.size() == 0) {
                this.goal = null;
                next = null;
                pathToGoal = null;
                remaining = 0;
            }
            else {
                next = pathToGoal.get(0);
                remaining = (int) graph.getEdgeWeight(graph.getEdge(currentTile, next));
            }
        }
        else --remaining;
    }

    protected void setGoal(Tile goal) {
        setGoal(goal, tiles -> true);
    }

    protected void setGoal(Tile goal, Predicate<List<Tile>> predicate) {
        if(currentTile.equals(goal)) {
            this.goal = null;
            next = null;
            pathToGoal = null;
            remaining = 0;
        }
        else {
            this.goal = goal;
            GraphPath<Tile, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(currentTile, goal);
            pathToGoal = path.getVertexList();
            if(predicate.test(pathToGoal)) {
                pathToGoal.remove(currentTile);
                next = pathToGoal.get(0);
                remaining = (int) graph.getEdgeWeight(graph.getEdge(currentTile, next));
            } else {
                this.goal = null;
                next = null;
                pathToGoal = null;
                remaining = 0;
            }
        }
    }

    protected boolean hasGoal() {
        return goal != null;
    }
}
