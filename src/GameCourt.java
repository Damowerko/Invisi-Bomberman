import oracle.jrockit.jfr.JFR;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
    private JLabel status; // Current status text, i.e. "Running..."

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 25;

    //User input
    final Direction[] movementDir = {null};

    //Message processor;
    ClientModel model;
    ClientMessenger messenger;

    public GameCourt(JLabel status) {
        // initialize client
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Ask to join host
        messenger = new ClientMessenger();
        messenger.start();
        messenger.addConnection(askJoinHost(frame));

        reset(frame);

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // The key listener allows movement of the player.
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    movementDir[0] = Direction.LEFT;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    movementDir[0] = Direction.RIGHT;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    movementDir[0] = Direction.DOWN;
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    movementDir[0] = Direction.UP;
                }
            }
            public void keyReleased(KeyEvent e) {
                movementDir[0] = null;
            }
        });

        // Update timer
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        this.status = status;
    }

    public void update(){
        model.processMessages(messenger.getMessages());
        repaint();
    }

    public void reset(JFrame frame){
        model = new ClientModel();
        (new Thread(new Runnable() {
            @Override
            public void run() {
                readyDialog(frame);
                messenger.sendMessage(Message.ready(model.getId()));
            }
        })).start();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        model.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Grid.getSize(), Grid.getSize());
    }

    /**
     * Creates a dialog asking to join a game and establishes the connection.
     * @return The socket of the connection.
     */
    private static Socket askJoinHost(JFrame frame) {
        Object[] options = {"Host Game", "Join Game", "Exit"};
        int choice = JOptionPane.showOptionDialog(frame,
                "",
                "",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        switch (choice) {
            case 2:
                System.exit(0);
                break;
            case 1:
                return joinGame(frame);
            case 0:
                try {
                    return hostGame();
                } catch (IOException e){
                    e.printStackTrace();
                }
        }
        return null;
    }

    private static Socket joinGame(JFrame frame) {
        while(true) {
            try {
                String input = JOptionPane.showInputDialog(frame, "Host Adress:");
                if (input == null){
                    return null;
                }
                if (input.equals("localhost")) {
                    input = null;
                }
                if (input != null && !input.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}\n" +
                        " (?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")){
                    throw new IOException("Invalid input");
                } else {
                    return connectToServer(InetAddress.getByName(input));
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, e.getMessage());
            }
        }
    }

    private static Socket hostGame() throws IOException {
        Server server = new Server();
        server.start();
        return connectToServer(InetAddress.getLoopbackAddress());
    }

    private static Socket connectToServer(InetAddress ipAddress) throws IOException {
        return new Socket(ipAddress, Server.port);
    }

    private static void readyDialog(JFrame frame){
        Object[] options = {"Ready!", "Exit"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Ready?",
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                1);
        if(choice == 1){
            System.exit(0);
        }
    }
}