import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A messenger which automatically accepts connections. Add connections doesn't have to be (cannot) be implicitly
 * called.
 */
public class ServerMessenger extends Messenger {
    private volatile ServerSocket serverSocket;

    @Override
    public void stop() {
        super.stop();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException iox) {
            iox.printStackTrace();
        } finally {
            serverSocket = null;
        }
    }

    @Override
    protected void addConnection(int id, Socket socket){
        super.addConnection(id, socket);
    }

    @Override
    public void run(){
        super.run();

        try {
            serverSocket = new ServerSocket(Server.port);
        } catch (IOException iox) {
            iox.printStackTrace();
            running = false;
            serverSocket = null;
        }
        // Await new connections on the current thread
        try {
            int nextId = 0;
            while (running && !serverSocket.isClosed()) {
                if(openSockets.size() <= Server.playerMax) {
                    int id = nextId++;
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client" + id + " has connected successfully.");
                    addConnection(id, clientSocket);
                    //TODO: Register users?
                    //taskQueue.add(new Registration(userId));
                }
            }
        } catch (IOException iox) {
            iox.printStackTrace();
        } finally {
            stop();
        }
    }
}
