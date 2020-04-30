package view;

import agents.Agent;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class PlayField extends JPanel {
    private JPanel[][] tiles;
    private ArrayList<Agent> agents;
    public PlayField() {
        this.setLayout(null);
    }
    public void loadFile(File file) throws IOException {
        int rows, cols;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        String[] args = line.split(",");
        rows = Integer.parseInt(args[0]);
        cols = Integer.parseInt(args[1]);
        tiles = new JPanel[rows][cols];
        Color[][] colors = new Color[rows][cols];
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
        bufferedReader.close();
        this.removeAll();

        int constraining = Math.min(this.getHeight()/rows, this.getWidth()/cols);
        int x, y;
        for(int i = 0; i < rows; ++i) {
            y = i*constraining;
            for(int j = 0; j < cols; ++j) {
                tiles[i][j] = new JPanel();
                tiles[i][j].setSize(constraining, constraining);
                x = j*constraining;
                tiles[i][j].setLocation(x, y);
                tiles[i][j].setBackground(colors[i][j]);
                tiles[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                this.add(tiles[i][j]);
            }
        }
        this.repaint();
    }

}
