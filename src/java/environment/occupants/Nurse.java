package environment.occupants;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import environment.view.PlayField;
import environment.view.Tile;

public class Nurse extends Agent {
    private static Image drawing;
    
    private int medicineCnt=0;
    
    public void addMedicine() {
    	++medicineCnt;
    }
    
    public void useMedicine() {
    	--medicineCnt;
    }
    
    public int getMedicineCnt() {
		return medicineCnt;
	}


    @Override
    public Image getDrawing() {
        return drawing;
    }

    public Nurse(PlayField field, Tile currentTile) {
        super(field, currentTile);
        synchronized (Cleaner.class) {
        	if(drawing == null) {
                File drawable = new File("res/nurse.png");
                try {
                    drawing = ImageIO.read(drawable).getScaledInstance(currentTile.getWidth() * 6 / 10, currentTile.getHeight() * 6 / 10, Image.SCALE_SMOOTH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
    }
}
