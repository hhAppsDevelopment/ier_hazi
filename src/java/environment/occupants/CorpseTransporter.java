package environment.occupants;

import environment.view.PlayField;
import environment.view.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CorpseTransporter extends Agent {

    private static Image drawing;


    @Override
    public Image getDrawing() {
        return drawing;
    }

    public CorpseTransporter(PlayField field, Tile currentTile) {
        super(field, currentTile);
        synchronized (CorpseTransporter.class) {
        	if(drawing == null) {
                File drawable = new File("res/corpse_transporter.png");
                try {
                    drawing = ImageIO.read(drawable).getScaledInstance(currentTile.getWidth() * 6 / 10, currentTile.getHeight() * 6 / 10, Image.SCALE_SMOOTH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
        
    }

    private void clearCorpse() {
        currentTile.getOccupants().forEach(occupant -> {
            if(occupant.isDead()) currentTile.unregisterOccupant(occupant);
        });
    }
}
