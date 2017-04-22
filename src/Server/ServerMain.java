import java.util.Timer;
import java.util.TimerTask;

/**
 * This methods initializes the server model and network sockets.
 */
public class ServerMain {

    public static void main(String[] args){
        final ServerModel serverModel = new ServerModel();
        final ServerBackend server = new ServerBackend();
        new Thread(server).start();
    }
    private ServerMain{
    }
}