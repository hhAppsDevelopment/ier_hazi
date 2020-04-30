import view.MainFrame;
import view.QuarantineLogger;

public class Environment {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        QuarantineLogger quarantineLogger = mainFrame.getLogger();

    }
}
