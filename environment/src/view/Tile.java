package view;

import model.Premise;
import occupants.Occupant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Tile extends JPanel {
    private ArrayList<Occupant> occupants;
    private Premise premise;

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
        for (int i = 0, occupantsSize = occupants.size(); i < occupantsSize; i++) {
            Occupant o = occupants.get(i); // drawing on top of each other
            graphics.setColor(o.getBaseColor());
            graphics.fillOval(getWidth() / 10, getHeight() / 10, (getWidth() * 8) / 10, (getHeight() * 8) / 10);
            Image drawing = o.getDrawing();
            graphics.drawImage(drawing, getWidth() * 2 / 10, getHeight() * 2 / 10, null);
        }

    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    public Premise getPremise() {
        return premise;
    }
}
