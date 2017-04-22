package Networking;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public final class Client {
    Socket socket;

    public Client(JFrame frame){
        socket = null;
        while(socket == null) {
            socket = Client.askJoinHost(frame);
        }
    }
    /**
     * Creates a dialog asking to join a game and establishes the connection.
     * @return The socket of the connection.
     */
    public static Socket askJoinHost(JFrame frame) {
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
        new Server();
        return connectToServer(InetAddress.getLoopbackAddress());
    }

    private static Socket connectToServer(InetAddress ipAddress) throws IOException {
        return new Socket(ipAddress, ServerConstants.port);
    }
}
