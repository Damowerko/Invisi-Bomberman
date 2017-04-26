import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

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
    private Input input = null;
    private Input bombInput = null;
    private boolean lost = false;

    //Message processor;
    ClientModel model;
    ClientMessenger messenger;

    JFrame frame;

    public GameCourt(JLabel status) {
        // initialize client
        frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        model = new ClientModel();

        // Ask to join host
        messenger = new ClientMessenger();
        messenger.start();
        messenger.addConnection(askJoinHost(frame));

        start();

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // The key listener allows movement of the player.
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    input = Input.LEFT;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    input = Input.RIGHT;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    input = Input.DOWN;
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    input = Input.UP;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    bombInput = Input.BOMB;
                }
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    bombInput = null;
                } else {
                    input = null;
                }
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
        messenger.sendMessage(inputMessages());
        status.setText(model.status());
        if(model.died()){
            lost = true;
            JOptionPane.showMessageDialog(frame, "You lose!", "Lost", JOptionPane.PLAIN_MESSAGE);
        }
        if(model.isReset()){
            restart();
        }
        repaint();
    }

    private Queue<Message> inputMessages(){
        Queue<Message> out = new LinkedList<>();
        if(input != null){
            out.add(Message.input(model.getId(), input));
        }
        if(bombInput != null){
            out.add(Message.input(model.getId(), bombInput));
        }
        return out;
    }

    public void start(){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                readyDialog(frame);
                messenger.sendMessage(Message.ready(model.getId()));
            }
        })).start();
    }

    private void restart(){
        if(!lost){
            JOptionPane.showMessageDialog(frame, "You win!", "Won", JOptionPane.PLAIN_MESSAGE);
        }
        lost = false;
        model.reset();
        start();
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
                options[0]);
        if(choice == 1){
            System.exit(0);
        }
    }
}