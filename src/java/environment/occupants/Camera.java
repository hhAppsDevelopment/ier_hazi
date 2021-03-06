package environment.occupants;

import environment.view.PlayField;
import environment.view.QuarantineLogger;
import environment.view.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Camera extends Agent {
    private static Image drawing;
    
    private ArrayList<Person> coughs=new ArrayList<Person>();
    private ArrayList<Occupant> moves=new ArrayList<Occupant>();
    
     
    public ArrayList<Person> getCoughs() {
		return coughs;
	}

	public ArrayList<Occupant> getMoves() {
		return moves;
	}

	@Override
    public Image getDrawing() {
        return drawing;
    }

    public Camera(PlayField field, Tile currentTile) {
        super(field, currentTile);
        synchronized (Camera.class) {
        	if(drawing == null) {
                File drawable = new File("res/camera.png");
                try {
                	drawing = ImageIO.read(drawable).getScaledInstance(currentTile.getWidth() * 6 / 10, currentTile.getHeight() * 6 / 10, Image.SCALE_SMOOTH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
    }

    @Override
    protected void notifyMoved(Occupant occupant) {
    	moves.add(occupant);
    }

    @Override
    protected void notifyCoughing(Person person) {
    	coughs.add(person);
        QuarantineLogger.log("Coughing seen");
    }

    @Override
    protected void notifyDead(Person person) {
        QuarantineLogger.log("A person has died");
    }

    private void endQuarantine() {
        QuarantineLogger.log("Quarantine has ended!");
    }

    private void setRedLamp(boolean state) {
        currentTile.getPremise().setLocked(state);
    }
}
