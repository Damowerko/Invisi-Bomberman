import Networking.Client;
import javax.swing.*;
import java.awt.*;

public final class Game implements Runnable {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    public void run() {
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300, 300);

        Client client = new Client(frame);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

//        // Reset button
//        final JPanel control_panel = new JPanel();
//        frame.add(control_panel, BorderLayout.NORTH);
//
//        // Note here that when we add an action listener to the reset button, we define it as an
//        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
//        // method overridden. When the button is pressed, actionPerformed() will be called.
//        final JButton reset = new JButton("Reset");
//        reset.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                court.reset();
//            }
//        });
//        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
}
