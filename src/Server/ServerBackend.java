import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerBackend implements Runnable {

    Queue<Request> requestQueue;
    ServerModel model;

    ServerBackend(ServerModel model){
        requestQueue = new LinkedBlockingQueue<>();
        this.model = model;
    }

    @Override
    public void run() {
        //TODO
    }

    /**
     * Manages one client connection per instance. Parses reads socket and parses requests.
     */
    private class ConnectionHandler implements Runnable {
        private final int userId;
        private final Socket clientSocket;

        private ConnectionHandler(int userId, Socket clientSocket) {
            this.userId = userId;
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

        }
    }

    private interface Request{
        //TODO
    }
}
port = 1626