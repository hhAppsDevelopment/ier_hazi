package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class MainPanel extends JPanel {
    public MainPanel() {
        this.setLayout(new BorderLayout());
        this.add(new ControlPanel(), BorderLayout.PAGE_START);
        this.add(new PlayField(), BorderLayout.CENTER);
    }
}
