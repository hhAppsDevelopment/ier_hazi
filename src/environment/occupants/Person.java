package environment.occupants;

import environment.model.Cabin;
import environment.model.Corridor;
import environment.model.SmokingRoom;
import environment.model.TileGraph;
import environment.view.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Person extends Occupant {
    private double movingModifier = 1;
    private double moveChance;
    private double goOutChance;
    private double goInChance;
    private double goSmokeChance;
    private double redLampModifier;
    private double coughingChance;
    private double recoveryChance;
    private int food;
    private static final double moveChanceMax = 0.5;
    private static final double goOutChanceMax = 0.05;
    private static final double goInChanceMax = 0.05;
    private static final double goSmokeChanceMax = 0.1;
    private static final double redLampModifierMax = 0.1;
    private static final double coughingChanceMax = 0.25;
    private static final double recoveryChanceMax = 0.2;
    private static final double recoveryChanceMin = 0.1;
    private static final double coughingChanceMin = 0.1;
    private static final double moveChanceMin = 0.4;
    private static final double goOutChanceMin = 0.01;
    private static final double goInChanceMin = 0.01;
    private static final double goSmokeChanceMin = 0.05;
    private static final double redLampModifierMin = 0.01;
    private static final double smokerChance = 0.25;
    private static final int asymptomaticMin = 100;
    private static final int asymptomaticMax = 150;
    private static final int symptomaticMin = 50;
    private static final int symptomaticMax = 100;
    private static final int foodMin = 25;
    private static final int foodMax = 100;

    public void setContagious() {
        contagious = true;
        state = ILLNESS_STATE.ILL_ASYMPTOMATIC;
        remainingTime = (int) (asymptomaticMin + Math.random() * (asymptomaticMax - asymptomaticMin));
        movingModifier *= 0.5;
    }


    enum ILLNESS_STATE {
        HEALTHY,
        ILL_ASYMPTOMATIC,
        ILL_SYMPTOMATIC,
        DEAD
    };
    private ILLNESS_STATE state = ILLNESS_STATE.HEALTHY;
    private int remainingTime = 0;

    private static Image drawing;

    public Person(TileGraph tileGraph, Tile currentTile) {
        super(tileGraph, currentTile);
        if(drawing == null) {
            File personDrawable = new File("res/person.png");
            try {
                drawing = ImageIO.read(personDrawable).getScaledInstance(currentTile.getWidth() * 6 / 10, currentTile.getHeight() * 6 / 10, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        baseColor = Color.BLUE;

        moveChance = moveChanceMin + Math.random() * (moveChanceMax - moveChanceMin);
        goOutChance = goOutChanceMin + Math.random() * (goOutChanceMax - goOutChanceMin);
        goInChance = goInChanceMin + Math.random() * (goInChanceMax - goInChanceMin);
        goSmokeChance = (Math.random() <= smokerChance) ? goSmokeChanceMin + Math.random() * (goSmokeChanceMax - goSmokeChanceMin) : 0;
        redLampModifier = redLampModifierMin + Math.random() * (redLampModifierMax - redLampModifierMin);
        coughingChance = coughingChanceMin + Math.random() * (coughingChanceMax - coughingChanceMin);
        recoveryChance = recoveryChanceMin + Math.random() * (recoveryChanceMax - recoveryChanceMin);
        food = (int) (foodMin + Math.random() * (foodMax - foodMin));

    }

    @Override
    public Image getDrawing() {
        return drawing;
    }

    private boolean redLampCalc(List<Tile> tileList) {
        final int[] redLamps = {0};
        tileList.forEach(tile -> {
            if(tile.getLamp()) ++redLamps[0];
        } );
        return Math.random() <= Math.pow(redLampModifier, redLamps[0]);
    }

    public void resetFood() {
        food = (int) (foodMin + Math.random() * (foodMax - foodMin));
    }

    public void giveMedicine() {
        if(state == ILLNESS_STATE.ILL_ASYMPTOMATIC || state == ILLNESS_STATE.ILL_SYMPTOMATIC) {
            recoveryChance *= 1.1;
        }
    }

    @Override
    public void step() {
        if(state == ILLNESS_STATE.DEAD) {

            return;
        }
        super.step();

        if(food == 1) {
            movingModifier*=1.2;
            food = 0;
        }
        else if(food > 1) --food;

        if(remainingTime == 1) {
            switch(state) {
                case ILL_ASYMPTOMATIC:
                    state = ILLNESS_STATE.ILL_SYMPTOMATIC;
                    remainingTime = (int) (symptomaticMin + Math.random()*(symptomaticMax - symptomaticMin));
                    movingModifier *= 0.25;
                    break;
                case ILL_SYMPTOMATIC:
                    if(Math.random() <= recoveryChance){
                        state = ILLNESS_STATE.HEALTHY;
                        remainingTime = 0;
                        contagious = false;
                        baseColor = Color.BLUE;
                        recovered();
                    }
                    else {
                        state = ILLNESS_STATE.DEAD;
                        baseColor = Color.BLACK;
                        for (Tile t : currentTile.getPremise().getTiles()) {
                            for (Occupant o : t.getOccupants()) {
                                o.notifyDead(this);
                            }
                        }
                        remainingTime = 0;
                        movingModifier = 0;
                    }
                    break;
            }
        }
        else if (remainingTime > 1){
            --remainingTime;
        }
        if(state == ILLNESS_STATE.ILL_SYMPTOMATIC) {
            if (Math.random() <= coughingChance) {
                baseColor = Color.GREEN;
                for(Tile t : currentTile.getPremise().getTiles()){
                    for(Occupant o : t.getOccupants()) {
                        o.notifyCoughing(this);
                    }
                }
            } else {
                baseColor = Color.BLUE;
            }
        }
        if(!hasGoal()) {
            double rnd = Math.random();
            if(rnd <= moveChance*movingModifier) {
                setGoal(currentTile.getPremise().getRandomTile(), this::redLampCalc);
            }
            else if((rnd-=moveChance*movingModifier) <= goOutChance*movingModifier) {
                if(currentTile.getPremise() instanceof Cabin || currentTile.getPremise() instanceof SmokingRoom) {
                    setGoal(tileGraph.getRandomPremise(tileGraph.getCorridors()).getRandomTile(), this::redLampCalc);
                }
            }
            else if((rnd-=goOutChance*movingModifier) <= goInChance*movingModifier) {
                if(currentTile.getPremise() instanceof Corridor || currentTile.getPremise() instanceof SmokingRoom) {
                    setGoal(tileGraph.getRandomPremise(tileGraph.getCabins()).getRandomTile(), this::redLampCalc);
                }
            }
            else if(rnd - (goInChance * movingModifier) <= goSmokeChance*movingModifier) {
                if(currentTile.getPremise() instanceof Cabin || currentTile.getPremise() instanceof Corridor) {
                    setGoal(tileGraph.getRandomPremise(tileGraph.getSmokingRooms()).getRandomTile(), this::redLampCalc);
                }
            }
        }
    }

    @Override
    public void notifyContagious(Tile t) {
        if(!contagious) {
            super.notifyContagious(t);
            if (contagious) {
                state = ILLNESS_STATE.ILL_ASYMPTOMATIC;
                remainingTime = (int) (asymptomaticMin + Math.random() * (asymptomaticMax - asymptomaticMin));
                movingModifier *= 0.5;
            }
        }
    }

    @Override
	public boolean isDead() {
        return state == ILLNESS_STATE.DEAD;
    }
}
