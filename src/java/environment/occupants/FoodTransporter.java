package environment.occupants;

import environment.model.Cabin;
import environment.view.PlayField;
import environment.view.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FoodTransporter extends Agent {
    private static Image drawing;

    @Override
    public Image getDrawing() {
        return drawing;
    }

    public FoodTransporter(PlayField field, Tile currentTile) {
        super(field, currentTile);
        if(drawing == null) {
            File drawable = new File("res/food_transporter.png");
            try {
                drawing = ImageIO.read(drawable).getScaledInstance(currentTile.getWidth() * 6 / 10, currentTile.getHeight() * 6 / 10, Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void leaveFood() {
        if(currentTile.getPremise() instanceof Cabin) currentTile.getPremise().getTiles().forEach(tile -> tile.getOccupants().forEach(Occupant::resetFood));
    }

    private void leaveMedicine() {
        if(currentTile.getPremise() instanceof Cabin) currentTile.getPremise().getTiles().forEach(tile -> tile.getOccupants().forEach(Occupant::giveMedicine));
    }
}
