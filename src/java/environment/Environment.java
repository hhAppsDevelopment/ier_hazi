package environment;

import environment.view.MainFrame;

public class Environment {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(args[0], this);
        mainFrame.setVisible(true);
    }
	

}
