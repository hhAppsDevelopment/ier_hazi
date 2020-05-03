package view;

import model.Premise;
import occupants.Occupant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Tile extends JPanel {
    public ArrayList<Occupant> getOccupants() {
        return occupants;
    }

    private ArrayList<Occupant> occupants;
    private Premise premise;
    private boolean lamp;

    public Tile(){
        occupants = new ArrayList<>();
    }

    public void registerOccupant(Occupant o) {
        occupants.add(o);
    }

    public void unregisterOccupant(Occupant o) {
        occupants.remove(o);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if(lamp) {
            graphics.setColor(Color.RED);
            graphics.fillOval(1, 1, getWidth()/3, getHeight()/3);
        }
        for (int i = 0; i < occupants.size(); i++) {
            Occupant o = occupants.get(i); // drawing on top of each other
            graphics.setColor(o.getBaseColor());
            graphics.fillOval(getWidth() / 10, getHeight() / 10, (getWidth() * 8) / 10, (getHeight() * 8) / 10);
            Image drawing = o.getDrawing();
            graphics.drawImage(drawing, getWidth() * 2 / 10, getHeight() * 2 / 10, null);
        }
        if(occupants.size() > 1) {
            graphics.setColor(Color.GRAY);
            graphics.fillOval(getWidth()*2/3-1, getHeight()*2/3-1, getWidth()/3, getHeight()/3);
            graphics.setFont(graphics.getFont().deriveFont(getHeight()*0.3f));
            graphics.setColor(Color.WHITE);
            graphics.drawString("" + occupants.size(), (int) (getWidth()*2/3 + getWidth()*0.3/4), (int) (getHeight()*0.92f));
        }


    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    public Premise getPremise() {
        return premise;
    }

    public boolean getLamp() {
        return lamp;
    }

    public void setLamp(boolean lamp) {
        this.lamp = lamp;
    }
}
