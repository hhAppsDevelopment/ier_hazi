package environment.view;

import jason.environment.TimeSteppedEnvironment;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class MainFrame extends JFrame {
    private static final int width = 1600;
    private static final int height = 1000;
    private static final String title = "Automatic Quarantine System Simulation";
    private PlayField playField;


    public MainFrame(String arg, TimeSteppedEnvironment env) throws IOException {
        this.setSize(width, height);
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width / 2 - width/2, screenSize.height / 2 - height/2);
        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MainPanel mainPanel;
        this.setContentPane(mainPanel = new MainPanel(env));
        playField = mainPanel.getPlayField();
        QuarantineLogger.setMainPanel(mainPanel);
        this.setVisible(true);
        playField.loadFile(new File(arg));
    }

    public PlayField getPlayField() {
        return playField;
    }
}
