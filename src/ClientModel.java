import java.awt.*;
import java.util.Queue;

public class ClientModel {
    private boolean playing = false;
    Grid grid;

    ClientModel(){
        this.grid = new Grid();
    }

    public void draw(Graphics g){
        grid.draw(g);
    }

    public void processMessages(Queue<Message> messageQueue) {
        for(Message msg : messageQueue){
            processMessage(msg);
        }
    }

    private void processMessage(Message msg){
        switch(msg.getType()){
            case Create:
                TileObject object;
                CreateMessage cmsg = (CreateMessage) msg;
                switch(cmsg.objClass){
                    case DesObj:
                        object = new DestructibleObstalce(cmsg.position.x, cmsg.position.y);
                        grid.place(object, cmsg.position.x, cmsg.position.y);
                        break;
                }
            case Input:
                break;
            case Ready:
                break;
            case Destroy:
                DestroyMessage dmsg = (DestroyMessage) msg;
                grid.remove(dmsg.position.x, dmsg.position.y);
                break;
        }
    }

    public boolean isPlaying(){
        return playing;
    }
}