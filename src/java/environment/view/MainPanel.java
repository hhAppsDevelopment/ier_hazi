package environment.view;

import jason.environment.TimeSteppedEnvironment;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private ControlPanel controlPanel;

    public PlayField getPlayField() {
        return playField;
    }

    private PlayField playField;
    private JTextArea logText;
    private JScrollPane logPane;

    public MainPanel(TimeSteppedEnvironment env) {
        this.setLayout(new BorderLayout());
        playField = new PlayField();
        this.add(controlPanel = new ControlPanel(playField, env), BorderLayout.PAGE_START);
        this.add(playField, BorderLayout.CENTER);
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
