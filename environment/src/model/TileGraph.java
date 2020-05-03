package model;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import view.Tile;

import java.awt.*;
import java.util.ArrayList;

public class TileGraph {

    private static final int CABIN_WEIGHT = 6;
    private static final int SMOKING_WEIGHT = 4;
    private static final int CABIN_SMOKING_WEIGHT = 5;
    private static final int CORRIDOR_WEIGHT = 2;
    private static final int CABIN_CORRIDOR_WEIGHT = 4;
    private static final int SMOKING_CORRIDOR_WEIGHT = 3;
    private static final int DOOR_WEIGHT = 20;
    private static final int CABIN_DOOR_WEIGHT = 13;
    private static final int SMOKING_DOOR_WEIGHT = 12;
    private static final int CORRIDOR_DOOR_WEIGHT = 11;

    public ArrayList<Cabin> getCabins() {
        return cabins;
    }

    public ArrayList<SmokingRoom> getSmokingRooms() {
        return smokingRooms;
    }

    public ArrayList<Corridor> getCorridors() {
        return corridors;
    }

    private ArrayList<Cabin> cabins;
    private ArrayList<SmokingRoom> smokingRooms;
    private ArrayList<Corridor> corridors;

    private DijkstraShortestPath<Tile, DefaultWeightedEdge> dijkstraShortestPath;

    public SimpleWeightedGraph<Tile, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    private SimpleWeightedGraph<Tile, DefaultWeightedEdge> graph;

    public void init(Color[][] colors, Tile[][] panels, int rows, int cols) {
        boolean[][] processed = new boolean[rows][cols];
        cabins = new ArrayList<>();
        smokingRooms = new ArrayList<>();
        corridors = new ArrayList<>();
        graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < cols; ++j) {
                if (Color.BLACK.equals(colors[i][j])) { continue; }
                graph.addVertex(panels[i][j]);
                if(i > 0 && graph.containsVertex(panels[i-1][j])) {
                    DefaultWeightedEdge defaultWeightedEdge = new DefaultWeightedEdge();
                    graph.addEdge(panels[i][j], panels[i-1][j], defaultWeightedEdge);
                    graph.setEdgeWeight(defaultWeightedEdge, calcWeight(colors[i][j], colors[i-1][j]));
                }
                if(j > 0 && graph.containsVertex(panels[i][j-1])) {
                    DefaultWeightedEdge defaultWeightedEdge = new DefaultWeightedEdge();
                    graph.addEdge(panels[i][j], panels[i][j-1], defaultWeightedEdge);
                    graph.setEdgeWeight(defaultWeightedEdge, calcWeight(colors[i][j], colors[i][j-1]));
                }
                if (Color.WHITE.equals(colors[i][j])) {
                    if(!processed[i][j]) {
                        cabins.add((Cabin) discoverPremise(processed, colors, panels, i, j, rows, cols, Color.WHITE, new Cabin()));
                    }
                } else if (Color.RED.equals(colors[i][j])) {
                    if(!processed[i][j]) {
                        smokingRooms.add((SmokingRoom) discoverPremise(processed, colors, panels, i, j, rows, cols, Color.RED, new SmokingRoom()));
                    }
                } else if (Color.YELLOW.equals(colors[i][j])) {
                    if(!processed[i][j]) {
                        corridors.add((Corridor) discoverPremise(processed, colors, panels, i, j, rows, cols, Color.YELLOW, new Corridor()));
                    }
                }
            }
        }
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private int calcWeight(Color color, Color color1) {
        int value = encodeColor(color) | encodeColor(color1);
        switch(value) {
            case 1: return CABIN_WEIGHT;
            case 2: return SMOKING_WEIGHT;
            case 3: return CABIN_SMOKING_WEIGHT;
            case 4: return CORRIDOR_WEIGHT;
            case 5: return CABIN_CORRIDOR_WEIGHT;
            case 6: return SMOKING_CORRIDOR_WEIGHT;
            case 8: return DOOR_WEIGHT;
            case 9: return CABIN_DOOR_WEIGHT;
            case 10: return SMOKING_DOOR_WEIGHT;
            case 12: return CORRIDOR_DOOR_WEIGHT;
        }
        System.out.println("Somethings's fishy: " + color + " " + color1 + " value: " + value);
        return 0;
    }

    private int encodeColor(Color color) {
        if (Color.WHITE.equals(color)) {
            return 1;
        } else if (Color.RED.equals(color)) {
            return 2;
        } else if (Color.YELLOW.equals(color)) {
            return 4;
        } else if (new Color(150,75,0).equals(color)) {
            return 8;
        }
        return 0;
    }

    private Premise discoverPremise(boolean[][] processed, Color[][] colors, Tile[][] panels, int i, int j, int rows, int cols, Color color, Premise premise) {
        if(!processed[i][j] && colors[i][j].equals(color)) {
            premise.addTile(panels[i][j]);
            processed[i][j] = true;
            if(i > 0)           premise = discoverPremise(processed, colors, panels, i-1, j, rows, cols, color, premise);
            if(j > 0)           premise = discoverPremise(processed, colors, panels, i, j-1, rows, cols, color, premise);
            if(i < rows - 1)    premise = discoverPremise(processed, colors, panels, i+1, j, rows, cols, color, premise);
            if(j < cols - 1)    premise = discoverPremise(processed, colors, panels, i, j+1, rows, cols, color, premise);
        }
        return premise;
    }

    public DijkstraShortestPath<Tile, DefaultWeightedEdge> getDijkstraShortestPath() {
        return dijkstraShortestPath;
    }
}
