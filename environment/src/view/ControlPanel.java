package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class ControlPanel extends JPanel {
    private final PlayField playField;
    private JLabel lFileName;
    private JTextField fileName;
    private JButton browse;
    private JButton load;
    private JButton start;
    private JButton stop;
    public ControlPanel(PlayField playField) {
        this.playField = playField;
        lFileName = new JLabel("File: ");
        this.add(lFileName);

        fileName = new JTextField();
        fileName.setColumns(20);
        this.add(fileName);

        browse = new JButton("Browse");
        browse.addActionListener(this::browse);
        this.add(browse);

        load = new JButton("Load");
        load.addActionListener(this::load);
        this.add(load);

        start = new JButton("Start");
        start.addActionListener(this::start);
        this.add(start);

        stop = new JButton("Stop");
        stop.addActionListener(this::stop);
        this.add(stop);
    }

    private void stop(ActionEvent actionEvent) {
        QuarantineLogger.log("Not yet implemented!");
    }

    private void start(ActionEvent actionEvent) {
        QuarantineLogger.log("Not yet implemented!");
    }

    private void browse(ActionEvent actionEvent) {
        JFileChooser jFileChooser = new JFileChooser();
        File workingDir = new File(System.getProperty("user.dir"));
        jFileChooser.setCurrentDirectory(workingDir);
        int res = jFileChooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) fileName.setText(workingDir.toURI().relativize(jFileChooser.getSelectedFile().toURI()).getPath());
    }

    private void load(ActionEvent actionEvent) {
        try {
            playField.loadFile(new File(fileName.getText()));
        } catch (IOException e) {
            QuarantineLogger.log(e.toString());
        }
    }
}
