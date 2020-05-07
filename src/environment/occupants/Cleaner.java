package environment.occupants;

import environment.model.TileGraph;
import environment.view.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Cleaner extends Agent {
    private static Image drawing;


    @Override
    public Image getDrawing() {
        return drawing;
    }

    public Cleaner(TileGraph tileGraph, Tile currentTile) {
        super(tileGraph, currentTile);
        if(drawing == null) {
            File drawable = new File("res/cleaner.png");
            try {
                drawing = ImageIO.read(drawable).getScaledInstance(currentTile.getWidth() * 6 / 10, currentTile.getHeight() * 6 / 10, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cleanCurrentRoom() {
        currentTile.getPremise().getContagious().clear();
    }
}