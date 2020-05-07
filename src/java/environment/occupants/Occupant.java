package environment.occupants;

import environment.model.TileGraph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import environment.view.Tile;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

public abstract class Occupant {
    protected boolean contagious = false;
    private double contagionChance = 0.1;

    protected void recovered() {
        contagionChance *= 0.5;
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public abstract Image getDrawing();

    protected Tile currentTile;
    public Tile getCurrentTile() {
		return currentTile;
	}

	public void setCurrentTile(Tile currentTile) {
		this.currentTile = currentTile;
	}

	protected Color baseColor;
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
        for (Tile t : currentTile.getPremise().getTiles()) {
            for (Occupant o : t.getOccupants()) {
                o.notifyMoved(this);
            }
        }
        if (contagious) {
            for (Tile t : currentTile.getPremise().getTiles()) {
                for (Occupant o : t.getOccupants()) {
                    o.notifyContagious(currentTile);
                }
            }
            currentTile.getPremise().addContagious(currentTile);
        } else {
            for (Tile t : currentTile.getPremise().getContagious()) {
                notifyContagious(t);
            }
        }
    }


    protected void notifyContagious(Tile contTile) {
        int size = (int) dijkstraShortestPath.getPath(currentTile, contTile).getWeight();
        if (Math.random() <= Math.pow(contagionChance, size)) this.contagious = true;
    }

    public void step() {
        if (goal == null) return;
        if (remaining == 1) {
            moveSelf(next);
            pathToGoal.remove(currentTile);
            if (pathToGoal.size() == 0) {
                this.goal = null;
                next = null;
                pathToGoal = null;
                remaining = 0;
            } else {
                next = pathToGoal.get(0);
                remaining = (int) graph.getEdgeWeight(graph.getEdge(currentTile, next));
            }
        } else --remaining;
    }

    public void setGoal(Tile goal) {
        setGoal(goal, tiles -> true);
    }

    protected void setGoal(Tile goal, Predicate<List<Tile>> predicate) {
        if (currentTile.equals(goal)) {
            this.goal = null;
            next = null;
            pathToGoal = null;
            remaining = 0;
        } else {
            this.goal = goal;
            GraphPath<Tile, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(currentTile, goal);
            pathToGoal = path.getVertexList();
            if (predicate.test(pathToGoal)) {
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

    protected void notifyMoved(Occupant occupant) {
    }

    protected void notifyCoughing(Person person) {
    }

    protected void notifyDead(Person person) {
    }

    public void resetFood() {
    }

    public void giveMedicine() {
    }

    public boolean isDead() {
        return false;
    }
}
