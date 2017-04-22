package Server;

/**
 * This methods initializes the server model and backend (in a separate thread).
 */
public class ServerMain {

    private ServerMain() {
    }

    public static void main(String[] args){
        final ServerModel serverModel = new ServerModel();
        final ServerBackend server = new ServerBackend(serverModel);
        new Thread(server).start();
    }
}