package environment.view;

import jason.environment.TimeSteppedEnvironment;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class MainFrame extends JFrame {
    private static final int width = 1600;
    private static final int height = 900;
    private static final String title = "Automatic Quarantine System Simulation";
    private PlayField playField;


    public MainFrame(String arg, TimeSteppedEnvironment quarantineEnvironment) throws IOException {
        this.setSize(width, height);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width / 2 - width/2, screenSize.height / 2 - height/2);
        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MainPanel mainPanel;
        this.setContentPane(mainPanel = new MainPanel(arg, quarantineEnvironment));
        playField = mainPanel.getPlayField();
        this.setResizable(false);
        QuarantineLogger.setMainPanel(mainPanel);
    }

    public PlayField getPlayField() {
        return playField;
    }
}
