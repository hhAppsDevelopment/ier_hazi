package environment.occupants;

import environment.view.PlayField;
import environment.view.QuarantineLogger;
import environment.view.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Camera extends Agent {
    private static Image drawing;
    @Override
    public Image getDrawing() {
        return drawing;
    }

    public Camera(PlayField field, Tile currentTile) {
        super(field, currentTile);
        if(drawing == null) {
            File drawable = new File("res/camera.png");
            try {
                drawing = ImageIO.read(drawable).getScaledInstance(currentTile.getWidth() * 6 / 10, currentTile.getHeight() * 6 / 10, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void notifyMoved(Occupant occupant) {
    }

    @Override
    protected void notifyCoughing(Person person) {
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
