package environment.view;

import environment.occupants.Person;
import jason.environment.TimeSteppedEnvironment;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class ControlPanel extends JPanel {
    private final PlayField playField;
    private JSlider speedSlider, moveChanceSlider,  goOutChanceSlider, goInChanceSlider, goSmokeChanceSlider, redLampModifierSlider, coughingChanceSlider, asymptomaticMinSlider, asymptomaticMaxSlider,  symptomaticMinSlider, symptomaticMaxSlider;
    TimeSteppedEnvironment env;
    public ControlPanel(PlayField playField, TimeSteppedEnvironment env) {
        this.playField = playField;
        this.env=env;
        speedSlider = new JSlider(JSlider.VERTICAL, 1, 100, 10);
        speedSlider.addChangeListener(this::sliderChanged);

        this.add(new JLabel("moveChance: "));
        this.add(moveChanceSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (Person.moveChance * 100)));
        moveChanceSlider.addChangeListener(changeEvent -> Person.moveChance = moveChanceSlider.getValue() * 1.0 / 100.0);
        this.add(new JLabel("goOutChance: "));
        this.add(goOutChanceSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (Person.goOutChance * 100)));
        goOutChanceSlider.addChangeListener(changeEvent -> Person.goOutChance = goOutChanceSlider.getValue() * 1.0 / 100.0);
        this.add(new JLabel("goInChance: "));
        this.add(goInChanceSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (Person.goInChance * 100)));
        goInChanceSlider.addChangeListener(changeEvent -> Person.goInChance = goInChanceSlider.getValue() * 1.0 / 100.0);
        this.add(new JLabel("goSmokeChance: "));
        this.add(goSmokeChanceSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (Person.goSmokeChance * 100)));
        goSmokeChanceSlider.addChangeListener(changeEvent -> Person.goSmokeChance = goSmokeChanceSlider.getValue() * 1.0 / 100.0);
        this.add(new JLabel("redLampModifier: "));
        this.add(redLampModifierSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (Person.redLampModifier * 100)));
        redLampModifierSlider.addChangeListener(changeEvent -> Person.redLampModifier = redLampModifierSlider.getValue() * 1.0 / 100.0);
        this.add(new JLabel("coughingChance: "));
        this.add(coughingChanceSlider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (Person.coughingChance * 100)));
        coughingChanceSlider.addChangeListener(changeEvent -> Person.coughingChance = coughingChanceSlider.getValue() * 1.0 / 100.0);
        this.add(new JLabel("asymptomaticMin: "));
        this.add(asymptomaticMinSlider = new JSlider(JSlider.VERTICAL, 0, 100, Person.asymptomaticMin));
        asymptomaticMinSlider.addChangeListener(changeEvent -> Person.asymptomaticMin = asymptomaticMinSlider.getValue());
        this.add(new JLabel("asymptomaticMax: "));
        this.add(asymptomaticMaxSlider = new JSlider(JSlider.VERTICAL, 0, 100, Person.asymptomaticMax));
        asymptomaticMaxSlider.addChangeListener(changeEvent -> Person.asymptomaticMax = asymptomaticMaxSlider.getValue());
        this.add(new JLabel("symptomaticMin: "));
        this.add(symptomaticMinSlider = new JSlider(JSlider.VERTICAL, 0, 100, Person.symptomaticMin));
        symptomaticMinSlider.addChangeListener(changeEvent -> Person.symptomaticMin = symptomaticMinSlider.getValue());
        this.add(new JLabel("symptomaticMax: "));
        this.add(symptomaticMaxSlider = new JSlider(JSlider.VERTICAL, 0, 100, Person.symptomaticMax));
        symptomaticMaxSlider.addChangeListener(changeEvent -> Person.symptomaticMax = symptomaticMaxSlider.getValue());
        this.add(new JLabel("Speed: "));
        this.add(speedSlider);

        playField.setStepTime(1000/speedSlider.getValue());
        env.setTimeout(1000/speedSlider.getValue());
    }

    private void sliderChanged(ChangeEvent changeEvent) {
        playField.setStepTime(1000/speedSlider.getValue());
        env.setTimeout(1000/speedSlider.getValue());
    }
}
