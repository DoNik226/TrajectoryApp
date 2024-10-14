import interfaces.MainPanel;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        MainPanel mainPanel = new MainPanel();
        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Траектории");
        frame.setLocationRelativeTo(null);
    }
}
