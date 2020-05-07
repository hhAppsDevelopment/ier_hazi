package environment.view;

import environment.model.Premise;
import environment.model.TileGraph;
import environment.occupants.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PlayField extends JPanel {
    private ArrayList<Agent> agents;
    private ArrayList<Person> people;

    private TileGraph tileGraph;

    public PlayField() {
        this.setLayout(null);
        agents = new ArrayList<>();
        people = new ArrayList<>();
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    public void loadFile(File file) throws IOException {
        int rows, cols;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        String[] args = line.split(",");

        rows = Integer.parseInt(args[0]);
        cols = Integer.parseInt(args[1]);

        Tile[][] tiles = new Tile[rows][cols];
        Color[][] colors = readColors(bufferedReader, rows, cols);

        bufferedReader.close();
        this.removeAll();
        tileGraph = new TileGraph();

        int constraining = Math.min(this.getHeight()/rows, this.getWidth()/cols);
        int x, y;
        for(int i = 0; i < rows; ++i) {
            y = i*constraining;
            for(int j = 0; j < cols; ++j) {
                tiles[i][j] = new Tile();
                tiles[i][j].setSize(constraining, constraining);
                x = j*constraining;
                tiles[i][j].setLocation(x, y);
                tiles[i][j].setBackground(colors[i][j]);
                tiles[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                this.add(tiles[i][j]);
            }
        }
        tileGraph.init(colors, tiles, rows, cols);

        for(Premise p : tileGraph.getCabins()) {
            Tile personTile = p.getRandomTile();
            Person person = new Person(tileGraph, personTile);
            people.add(person);
        }

        people.get((int) (Math.random()*people.size())).setContagious();

        this.repaint();
    }

    private Color[][] readColors(BufferedReader bufferedReader, int rows, int cols) throws IOException {
        Color[][] colors = new Color[rows][cols];
        String line;
        String[] args;
        for(int i = 0; i < rows; ++i) {
            line = bufferedReader.readLine();
            args = line.split(",");
            for(int j = 0; j < cols; ++j) {
                switch((args[j])) {
                    case "1": colors[i][j] = Color.WHITE; break;
                    case "0": colors[i][j] = Color.BLACK; break;
                    case "r": colors[i][j] = Color.RED; break;
                    case "b": colors[i][j] = new Color(150,75,0); break;
                    case "y": colors[i][j] = Color.YELLOW; break;
                }
            }
        }
        return colors;
    }

    public TileGraph getTileGraph() {
        return tileGraph;
    }

    public void step() {
        agents.forEach(Agent::step);
        people.forEach(Person::step);
        this.repaint();
    }
}
