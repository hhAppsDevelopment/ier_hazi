package view;

public class QuarantineLogger {
    private static MainPanel mainPanel;
    private static long startMillis;

    static void setMainPanel(MainPanel mainPanel) {
        QuarantineLogger.mainPanel = mainPanel;
        startMillis = System.currentTimeMillis();
    }

    public static void log(String s) {
        if(mainPanel == null) {
            System.err.println("Logger not yet initialized! Error msg: \"" + s + "\"");
        }
        else {
            long min, sec, mil, elapsed;
            elapsed = System.currentTimeMillis() - startMillis;
            min = elapsed / 1000 / 60;
            sec = (elapsed / 1000) % 60;
            mil = (elapsed) % 1000;
            mainPanel.append(String.format("%02d:%02d.%03d: %s", min, sec, mil, s));
        }
    }
}
