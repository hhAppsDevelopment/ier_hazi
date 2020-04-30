package view;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private ControlPanel controlPanel;
    private PlayField playField;
    private JTextArea logText;
    private JScrollPane logPane;

    public MainPanel() {
        this.setLayout(new BorderLayout());
        this.add(controlPanel = new ControlPanel(), BorderLayout.PAGE_START);
        this.add(playField = new PlayField(), BorderLayout.CENTER);
        logText = new JTextArea();
        logText.setColumns(30);
        logText.setEditable(false);
        logPane = new JScrollPane(logText);
        logPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(logPane, BorderLayout.LINE_END);
    }

     void append(String msg) {
        logText.append("\r\n" + msg);
    }
}
