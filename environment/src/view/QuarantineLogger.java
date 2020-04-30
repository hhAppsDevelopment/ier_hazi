package view;

public class QuarantineLogger {
    private final MainPanel mainPanel;
    private final long startMillis;
    public QuarantineLogger(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        startMillis = System.currentTimeMillis();
    }
    public void log(String s) {
        long min, sec, mil, elapsed;
        elapsed = System.currentTimeMillis() - startMillis;
        min = elapsed / 1000 / 60;
        sec = (elapsed / 1000) % 60;
        mil = (elapsed) % 1000;
        mainPanel.append(String.format("%02d:%02d.%03d: %s", min, sec, mil, s));
    }
}
