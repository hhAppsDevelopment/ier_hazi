package view;

import model.TileGraph;
import occupants.Occupant;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Tile extends JPanel implements MouseListener {
    private ArrayList<Occupant> occupants;
    private final TileGraph tileGraph;

    public Tile(TileGraph tileGraph) {
        this.tileGraph = tileGraph;
        this.addMouseListener(this);
    }

    private static Tile first = null;
    private static List<Tile> list = new ArrayList<>();

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(first == null) {
            first = this;
            for(Tile t : list) {
                t.removeAll();
                t.repaint();
            }
        } else {
            QuarantineLogger.log("Shortest path: " + calcShortestPath(first, this));
            for(Tile t : list) {
                JLabel label = new JLabel("X");
                label.setSize(t.getWidth(), t.getHeight());
                label.setForeground(Color.BLUE);
                t.add(label);
                t.repaint();
            }
            first = null;
        }
    }

    private int calcShortestPath(Tile first, Tile second) {
        DijkstraShortestPath<Tile, DefaultWeightedEdge> dijkstraShortestPath = tileGraph.getDijkstraShortestPath();
        GraphPath<Tile, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(first, second);
        list = graphPath.getVertexList();
        return (int) graphPath.getWeight();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
