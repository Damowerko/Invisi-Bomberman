import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class send and receives messages between the client and server. It puts the messages on a thread-safe blocking
 * queue. Don't forget to run execute the run method after initializing.
 */
public abstract class Messenger implements Runnable {
    protected enum Type {
        Server, Client
    }

    protected final Map<Integer, Socket> openSockets;
    protected final BlockingQueue<Message> inputQueue;
    protected final BlockingQueue<Message> outputQueue;
    protected volatile boolean running;
    private final OutputHandler outputHandler;

    public Thread start() {
        if (!running) {
            Thread thread = new Thread(this);
            thread.start();
            return thread;
        }
        return null;
    }

    protected abstract Type type();

    Messenger() {
        openSockets = Collections.synchronizedMap(new HashMap<Integer, Socket>());
        inputQueue = new LinkedBlockingQueue<>();
        outputQueue = new LinkedBlockingQueue<>();
        outputHandler = new OutputHandler();
        running = false;
    }

    @Override
    public void run() {
        running = true;
        (new Thread(outputHandler)).start();
    }

    /**
     * Add a new connection. Must be running.
     *
     * @param id
     * @param socket
     * @throws IOException
     */
    protected void addConnection(int id, Socket socket) {
        if (running) {
            try {
                //Output (must go first to avoid deadlock)
                outputHandler.addConnection(id, socket);
                openSockets.put(id, socket);
                //Input
                (new Thread(new InputHandler(id, socket))).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves all incoming messages.
     *
     * @return Message queue.
     */
    public Queue<Message> getMessages() {
        Queue<Message> out = new LinkedList<>();
        inputQueue.drainTo(out);
        return out;
    }

    /**
     * Will send an array of messages.
     *
     * @param msgs
     */
    public void sendMessage(Message[] msgs) {
        if (msgs == null) {
            return;
        }
        for (Message msg : msgs) {
            sendMessage(msgs);
        }
    }

    /**
     * Will send a queue of messages. But will block the thread.
     *
     * @param msgs
     */
    public void sendMessage(Queue<Message> msgs) {
        if (msgs == null) {
            return;
        }
        while (!msgs.isEmpty()) {
            Message msg = msgs.remove();
            sendMessage(msg);
        }
    }

    /**
     * This will block the thread.
     *
     * @param msg Message to send
     */
    public void sendMessage(Message msg) {
        if (msg == null) {
            return;
        }
        try {
            outputQueue.put(msg);
        } catch (InterruptedException e) { // if the thread is interrupted it means we are shutting down - stop.
            e.printStackTrace();
        }
    }

    /**
     * Gracefully stops all threads. And clears all queues.
     */
    public void stop() {
        running = false;
        inputQueue.clear();
        outputQueue.clear();

        // Close all sockets
        synchronized (openSockets) {
            Iterator<Socket> iterator = openSockets.values().iterator();
            while (iterator.hasNext()) {
                Socket clientSocket = iterator.next();
                try {
                    clientSocket.close();
                } catch (IOException iox) {
                    iox.printStackTrace();
                } finally {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Reads/writes socket and adds to the input list takes from output. One socket per instance.
     */
    protected final class InputHandler implements Runnable {
        private final Socket socket;
        private final ObjectInputStream inputStream;
        private final int id; //for debugging

        private InputHandler(int id, Socket socket) throws IOException {
            this.socket = socket;
            inputStream = new ObjectInputStream(socket.getInputStream());
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (running && !socket.isClosed()) {
                    try {
                        Message msg = (Message) inputStream.readObject();
                        if (msg != null) {
                            inputQueue.put(msg);
                        } else {
                            socket.close();
                            inputQueue.add(Message.connected(false, id));
                        }
                    } catch (ClassNotFoundException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException iox) {
                iox.printStackTrace();
                inputQueue.add(Message.connected(false, id));
            } finally {
                openSockets.remove(id);
            }
        }
    }

    /**
     * Runs as a separate thread to send output. There is only one instance, but may send to many sockets.
     */
    protected class OutputHandler implements Runnable {
        private final Map<Integer, ObjectOutputStream> streams;

        public OutputHandler() {
            streams = Collections.synchronizedMap(new HashMap<Integer, ObjectOutputStream>());
        }

        /**
         * Add a connection to the output handler.
         *
         * @param id     The id of the connection.
         * @param socket The socket on which the connection is.
         * @throws IOException Unable to getOutputStream of socket.
         */
        public void addConnection(int id, Socket socket) throws IOException {
            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
            stream.flush();
            streams.put(id, stream);
            if (type() == Type.Server) {
                sendMessage(Message.connected(true, id), stream);
            }
        }

        @Override
        public void run() {
            while (running) {
                while (!outputQueue.isEmpty()) {
                    Message msg = null;
                    try {
                        msg = outputQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // bad shutdown
                    }
                    if (msg == null) {
                        continue;
                    }
                    for (int id : streams.keySet()) {
                        ObjectOutputStream outputStream = streams.get(id);
                        try {
                            sendMessage(msg, outputStream);
                        } catch (IOException e) {
                            streams.remove(id); //Disconnect
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private void sendMessage(Message msg, ObjectOutputStream os) throws IOException {
            os.writeObject(msg);
            os.flush();
        }
    }
}

