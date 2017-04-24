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

    public void start(){
        (new Thread(this)).start();
    }

    public void run(){
        // Game loop
        while(!serverModel.isGameOver()){
            reset();
            while(serverModel.isReady()){

            }
        }
    }

    private void reset(){
        serverModel.reset();
        while(!serverModel.isReady()){
            serverModel.processMessages(messenger.getMessages());
        }
        messenger.sendMessage(serverModel.initializeGrid());
        messenger.sendMessage(serverModel.reset());
    }
}
