package view;

import model.Premise;
import model.TileGraph;
import occupants.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PlayField extends JPanel {
    private static final int CLEANER_NUM = 3;
    private static final int FOOD_TRANSPORTER_NUM = 5;
    private static final int CORPSE_TRANSPORTER_NUM = 2;
    private ArrayList<Agent> agents;
    private ArrayList<Person> people;

    private TileGraph tileGraph;

    public PlayField() {
        this.setLayout(null);
        agents = new ArrayList<>();
        people = new ArrayList<>();
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
            Tile cameraTile = p.getTiles().get(0);
            Tile personTile = p.getRandomTile();
            Camera camera = new Camera(tileGraph, cameraTile);
            Person person = new Person(tileGraph, personTile);
            people.add(person);
            agents.add(camera);
            personTile.registerOccupant(person);
            cameraTile.registerOccupant(camera);
        }

        for(Premise p : tileGraph.getSmokingRooms()) {
            Tile cameraTile = p.getTiles().get(0);
            Camera camera = new Camera(tileGraph, cameraTile);
            agents.add(camera);
            cameraTile.registerOccupant(camera);
        }

        for(Premise p : tileGraph.getCorridors()) {
            Tile cameraTile = p.getTiles().get(0);
            Camera camera = new Camera(tileGraph, cameraTile);
            agents.add(camera);
            cameraTile.registerOccupant(camera);
            for(int i = 0; i < CLEANER_NUM; ++i) {
                Tile tile = p.getRandomTile();
                Agent agent = new Cleaner(tileGraph, tile);
                agents.add(agent);
                tile.registerOccupant(agent);
            }
            for(int i = 0; i < FOOD_TRANSPORTER_NUM; ++i) {
                Tile tile = p.getRandomTile();
                Agent agent = new FoodTransporter(tileGraph, tile);
                agents.add(agent);
                tile.registerOccupant(agent);
            }
            for(int i = 0; i < CORPSE_TRANSPORTER_NUM; ++i) {
                Tile tile = p.getRandomTile();
                Agent agent = new CorpseTransporter(tileGraph, tile);
                agents.add(agent);
                tile.registerOccupant(agent);
            }
        }


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
