package environment.view;

import jason.environment.TimeSteppedEnvironment;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class ControlPanel extends JPanel {
    private final TimeSteppedEnvironment environment;
    private JLabel lSlider;
    private JSlider slider;
    public ControlPanel(TimeSteppedEnvironment quarantineEnvironment) {
        environment = quarantineEnvironment;
        lSlider = new JLabel("Speed: ");
        this.add(lSlider);

        slider = new JSlider(JSlider.HORIZONTAL, 1, 100, 10);
        slider.addChangeListener(this::sliderChanged);
        this.add(slider);
        environment.setTimeout(1000/slider.getValue());
    }

    private void sliderChanged(ChangeEvent changeEvent) {
        environment.setTimeout(1000/slider.getValue());
    }
}
