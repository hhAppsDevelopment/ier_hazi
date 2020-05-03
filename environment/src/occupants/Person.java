package occupants;

import model.TileGraph;
import view.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Person extends Occupant {

    private double moveChance = 1;

    public Person(TileGraph tileGraph, Tile currentTile) {
        super(tileGraph, currentTile);
        File personDrawable = new File("res/person.png");
        try {
            drawing = ImageIO.read(personDrawable).getScaledInstance(currentTile.getWidth()*6/10, currentTile.getHeight()*6/10, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        baseColor = Color.BLUE;
    }

    @Override
    public void step() {
        super.step();
        if(!hasGoal()) {
            if (Math.random() <= moveChance) {
                setNext(getRandomNeighbour());
            }
        }
    }


}
