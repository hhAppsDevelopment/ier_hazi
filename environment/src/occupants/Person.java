package occupants;

import model.Cabin;
import model.Corridor;
import model.SmokingRoom;
import model.TileGraph;
import view.QuarantineLogger;
import view.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Person extends Occupant {
    private double movingModifier = 1;
    private double moveChance;
    private double goOutChance;
    private double goInChance;
    private double goSmokeChance;
    private static final double moveChanceMax = 0.5;
    private static final double goOutChanceMax = 0.05;
    private static final double goInChanceMax = 0.05;
    private static final double goSmokeChanceMax = 0.1;
    private static final double smokerChance = 0.25;

    public Person(TileGraph tileGraph, Tile currentTile) {
        super(tileGraph, currentTile);
        File personDrawable = new File("res/person.png");
        try {
            drawing = ImageIO.read(personDrawable).getScaledInstance(currentTile.getWidth()*6/10, currentTile.getHeight()*6/10, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        baseColor = Color.BLUE;

        moveChance = Math.random() * moveChanceMax;
        goOutChance = Math.random() * moveChanceMax;
        goInChance = Math.random() * moveChanceMax;
        goSmokeChance = (Math.random() <= smokerChance) ? Math.random() * goSmokeChanceMax : 0;

    }

    @Override
    public void step() {
        super.step();
        if(!hasGoal()) {
            double rnd = Math.random();
            if(rnd <= moveChance*movingModifier) {
                setGoal(currentTile.getPremise().getRandomTile());
            }
            else if((rnd-=moveChance*movingModifier) <= goOutChance) {
                if(currentTile.getPremise() instanceof Cabin || currentTile.getPremise() instanceof SmokingRoom) {
                    setGoal(tileGraph.getRandomPremise(tileGraph.getCorridors()).getRandomTile());
                }
            }
            else if((rnd-=goOutChance*movingModifier) <= goInChance) {
                if(currentTile.getPremise() instanceof Corridor || currentTile.getPremise() instanceof SmokingRoom) {
                    setGoal(tileGraph.getRandomPremise(tileGraph.getCabins()).getRandomTile());
                }
            }
            else if((rnd-=goInChance*movingModifier) <= goSmokeChance) {
                if(currentTile.getPremise() instanceof Cabin || currentTile.getPremise() instanceof Corridor) {
                    setGoal(tileGraph.getRandomPremise(tileGraph.getSmokingRooms()).getRandomTile());
                }
            }
        }
    }


}
