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
    public static double moveChance = 0.1;
    public static double goOutChance = 0.1;
    public static double goInChance = 0.1;
    public static double goSmokeChance = 0.1;
    public static double redLampModifier = 0.05;
    public static double coughingChance = 0.5;
    public static int asymptomaticMin = 50;
    public static int asymptomaticMax = 100;
    public static int symptomaticMin = 50;
    public static int symptomaticMax = 100;

    private static double baseRecoveryChance = 0.5;
    private static int foodMax = 100;


    private double recoveryChance;
    private int food;
    private double movingModifier = 1;

    private int id;
    private static int cntr=0;

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
    
    public int getId() {
    	return id;
    }

    public Person(TileGraph tileGraph, Tile currentTile) {
        super(tileGraph, currentTile);
        id=cntr++;
        synchronized (Person.class) {
        	if(drawing == null) {
                File personDrawable = new File("res/person.png");
                try {
                    drawing = ImageIO.read(personDrawable).getScaledInstance(currentTile.getWidth() * 6 / 10, currentTile.getHeight() * 6 / 10, Image.SCALE_SMOOTH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
        baseColor = new Color(0, 230, 255);
        recoveryChance = baseRecoveryChance;
        food = (int) (Math.random() * (foodMax));

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
        food = foodMax;
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
                        baseColor = new Color(0, 230, 255);
                        recovered();
                        recoveryChance = baseRecoveryChance;
                        movingModifier = 1;
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
                baseColor = new Color(0, 230, 255);
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
