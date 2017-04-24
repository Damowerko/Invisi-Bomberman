import javax.swing.*;
import java.awt.*;

public final class Game implements Runnable {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    public void run() {
        final JFrame frame = new JFrame("InvisiBomber");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
