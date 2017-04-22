package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class ServerBackend implements Runnable {
    Queue<Request> requestQueue;
    ServerModel model;
    ServerSocket serverSocket;
    Map<Integer, Socket> openSockets;
    private boolean running;

    ServerBackend(ServerModel model){
        requestQueue = new LinkedBlockingQueue<>();
        this.model = model;
        openSockets = Collections.synchronizedMap(new HashMap<Integer, Socket>());
        running = false;
    }

    @Override
    public void run() {
        running = true;

        try {
            serverSocket = new ServerSocket(ServerConstants.port);
        } catch (IOException iox) {
            iox.printStackTrace();
            running = false;
            serverSocket = null;
        }

        ExecutorService workerPool = Executors.newCachedThreadPool();
        try {
            int nextId = 0;
            while (running && !serverSocket.isClosed()) {
                int userId = nextId++;
                Socket clientSocket = serverSocket.accept();
                openSockets.put(userId, clientSocket);
                workerPool.execute(new ConnectionHandler(userId, clientSocket));
                System.out.println("User " + Integer.toString(userId) + " has connected!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Manages one client connection per instance. Parses reads socket and parses requests.
     */
    private final class ConnectionHandler implements Runnable {
        private final int userId;
        private final Socket clientSocket;

        private ConnectionHandler(int userId, Socket clientSocket) {
            this.userId = userId;
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            while(running && !clientSocket.isClosed()){

            }
        }
    }

    private interface Request{
        //TODO
    }
}
