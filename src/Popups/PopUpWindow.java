package Popups;

import javax.swing.*;
import java.awt.*;

public class PopUpWindow {
    private final JLabel label;

    public PopUpWindow(String message) {
        label = new JLabel(message);
    }

    public void display() {
        // create the frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set the frame's size and centre it
        frame.setSize(new Dimension(350, 200));
        frame.setLocationRelativeTo(null);

        // layout the fields and button on the frame
        JPanel panel = new JPanel();
        placeComponents(panel);
        frame.add(panel);

        // display the window
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        // panel.setLayout(null);

        // set message on centre of panel
        panel.add(label);
    }
}
