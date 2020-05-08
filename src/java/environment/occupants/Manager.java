package environment.occupants;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import environment.view.PlayField;
import environment.view.Tile;

public class Manager extends Agent{
	
	private static Image drawing;

    @Override
    public Image getDrawing() {
        return drawing;
    }

	public Manager(PlayField field, Tile currentTile) {
		super(field, currentTile);
        synchronized (Manager.class) {
        	if(drawing == null) {
                File drawable = new File("res/manager.png");
                try {
                    drawing = ImageIO.read(drawable).getScaledInstance(currentTile.getWidth() * 6 / 10, currentTile.getHeight() * 6 / 10, Image.SCALE_SMOOTH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}	}

	
	
}
