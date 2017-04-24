import java.io.IOException;
import java.net.Socket;

public class ClientMessenger extends Messenger {
    private int nextId;

    @Override
    protected Type type() {
        return Type.Client;
    }

    public ClientMessenger(){
        nextId = 0;
    }

    public void addConnection(Socket socket){
        if(openSockets.size() == 0){
            int id = nextId++;
            addConnection(id, socket);
        }
    }
}
