package environment.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

import jason.environment.TimeSteppedEnvironment;

public class ControlPanel extends JPanel {
    private final PlayField playField;
    private JLabel lSlider;
    private JSlider slider;
    TimeSteppedEnvironment env;
    public ControlPanel(PlayField playField, TimeSteppedEnvironment env) {
        this.playField = playField;
        this.env=env;
        lSlider = new JLabel("Speed: ");
        this.add(lSlider);

        slider = new JSlider(JSlider.HORIZONTAL, 1, 100, 10);
        slider.addChangeListener(this::sliderChanged);
        this.add(slider);
        playField.setStepTime(1000/slider.getValue());
        env.setTimeout(1000/slider.getValue());
    }

    private void sliderChanged(ChangeEvent changeEvent) {
        playField.setStepTime(1000/slider.getValue());
        env.setTimeout(1000/slider.getValue());
    }
}
