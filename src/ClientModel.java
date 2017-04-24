import java.awt.*;
import java.util.Queue;

public class ClientModel {
    Grid grid;
    private int id;

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

    public int getId(){
        return id;
    }

    private void processMessage(Message msg){
        switch(msg.getType()){
            case Create: {
                TileObject object;
                CreateMessage cmsg = (CreateMessage) msg;
                switch (cmsg.objClass) {
                    case DesObj:
                        object = new DestructibleObstalce(cmsg.position.x, cmsg.position.y);
                        grid.place(object, cmsg.position.x, cmsg.position.y);
                        break;
                    case Player:
                        Player player = new Player(cmsg.position.x, cmsg.position.y, cmsg.color, null);
                        grid.place(player, player.x, player.y);
                        break;
                }
            }
                break;
            case Input: {
                break;
            }
            case Ready: {
                break;
            }
            case Destroy: {
                DestroyMessage dmsg = (DestroyMessage) msg;
                grid.remove(dmsg.position.x, dmsg.position.y);
                break;
            }
            case Connected: {
                ConnectedMessage cmsg = (ConnectedMessage) msg;
                id = cmsg.id;
                break;
            }
        }
    }
}