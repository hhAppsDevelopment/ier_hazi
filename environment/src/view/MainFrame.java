package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final int width = 1280;
    private static final int height = 720;
    private static final String title = "Automatic Quarantine System Simulation";

    public MainFrame() {
        this.setSize(width, height);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width / 2 - width/2, screenSize.height / 2 - height/2);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
