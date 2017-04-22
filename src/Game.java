import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import Server.Server;
import Server.ServerConstants;
import javafx.embed.swing.JFXPanel;

public class Game implements Runnable{

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }

    public void run() {
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    /**
     * Helper function that allows to host or join game.
     * @return The socket of the connection.
     */
    private Socket askJoinHost(JFrame frame) throws IOException{
        Object[] options = {"Host Game","Join Game", "Exit"};
        int choice = JOptionPane.showOptionDialog(frame,
                "",
                "",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        switch(choice){
            case 2:
                System.exit(0);
                break;
            case 1:
                return joinGame(frame);
                break;
            case 0:
                return hostGame();
                break;
    }

    private Socket joinGame(JFrame frame) throws IOException{
        String input = "";
        while(!input.matches("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b") || !input.equals("localhost")){
            input = JOptionPane.showInputDialog(frame, "Host Adress:");
        }
        try {
            return connectToServer(InetAddress.getByName(input));
        } catch (UnknownHostException e) {
            return joinGame(frame); // ask for IP again
        }
    }

    private Socket hostGame() throws IOException {
        new Server();
        return connectToServer(InetAddress.getLoopbackAddress());
    }

    private Socket connectToServer(InetAddress ipAddress) throws IOException {
        return new Socket(ipAddress, ServerConstants.port);
    }
}
