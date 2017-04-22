package Server;

public class Server {
    public Server() {
        final ServerModel serverModel = new ServerModel();
        final ServerBackend server = new ServerBackend(serverModel);
        new Thread(server).start();
    }
}
