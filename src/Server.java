public class Server {
    // Server constants
    public static final int port = 1626;
    public static final int playerMax = 4;

    public Server() {
        final ServerModel serverModel = new ServerModel();
        final Messenger messenger = new ServerMessenger();
        messenger.start();

        messenger.sendMessage(serverModel.initializeGrid());
    }
}
