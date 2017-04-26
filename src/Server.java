public class Server implements Runnable {
    // Server constants
    public static final int port = 1626;
    public static final int playerMax = 4;
    final ServerModel serverModel;
    final Messenger messenger;

    public Server() {
        messenger = new ServerMessenger();
        serverModel = new ServerModel();
        messenger.start();
    }

    public void start() {
        (new Thread(this)).start();
    }

    public void run() {
        // Game loop
        do {
            reset();
            while (!serverModel.isGameOver()) {
                messenger.sendMessage(serverModel.update());
                messenger.sendMessage(serverModel.processMessages(messenger.getMessages()));
            }
        } while(!serverModel.isShutdown());
    }

    private void reset() {
        messenger.sendMessage(Message.reset());
        while (!serverModel.isReady()) {
            serverModel.processMessages(messenger.getMessages());
        }
        messenger.sendMessage(serverModel.reset());
        messenger.sendMessage(serverModel.initializeGrid());
    }
}
