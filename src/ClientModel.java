import java.awt.*;
import java.util.Queue;

public class ClientModel {
    Grid grid;
    private int id;
    private int lives;
    private boolean reset;
    private boolean died;

    private enum Status{
        WAITING, PLAYING, DEAD
    }
    private Status status;

    ClientModel(){
        reset = false;
        reset();
    }

    public void reset(){
        this.grid = new Grid();
        lives = 3;
        died = false;
        status = Status.WAITING;
    }

    public void draw(Graphics g){
        grid.draw(g);
    }

    public void processMessages(Queue<Message> messageQueue) {
        for(Message msg : messageQueue){
            processMessage(msg);
        }
    }

    public String status(){
        switch (status){
            case WAITING:
                return "Waiting for players...";
            case PLAYING:
                return "Lives: " + Integer.toString(lives);
            case DEAD:
                return "Dead... spectating from beyond";
        }
        return null;
    }

    public int getId(){
        return id;
    }

    public int getLives() {
        return lives;
    }

    public boolean isReset() {
        if(reset){
            reset = false;
            return true;
        }
        return false;
    }

    public boolean died() {
        if(died){
            status = Status.DEAD;
            died = false;
            return true;
        }
        return false;
    }

    private void processMessage(Message msg){
        switch(msg.getType()){
            case Create: {
                TileObject object;
                CreateMessage cmsg = (CreateMessage) msg;
                int x = cmsg.position.x;
                int y = cmsg.position.y;
                switch (cmsg.objClass) {
                    case DesObj:
                        object = new DestructibleObstalce(x, y);
                        grid.place(object, x, y);
                        break;
                    case Player:
                        Player player = new Player(x, y, cmsg.color, grid);
                        grid.place(player, player.x, player.y);
                        break;
                    case Bomb:
                        Bomb bomb = new Bomb(x, y, grid);
                        grid.place(bomb, x, y);
                        break;
                    case Explosion:
                        Explosion explosion = Explosion.explode(x, y, grid);
                        System.out.print("Explode\n");
                        grid.place(explosion, x, y);
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
                System.out.printf("Destroy: (%d, %d)\n", dmsg.position.x, dmsg.position.y);
                grid.remove(dmsg.position.x, dmsg.position.y);
                break;
            }
            case Connected: {
                ConnectedMessage cmsg = (ConnectedMessage) msg;
                id = cmsg.id;
                break;
            }
            case Move:
                MoveMessage mmsg = (MoveMessage) msg;
                grid.move(mmsg.oldPos.x, mmsg.oldPos.y, mmsg.newPos.x, mmsg.newPos.y);
                break;
            case Reset:
                reset = true;
                break;
            case PlayerStatus:
                PlayerStatus smsg = (PlayerStatus) msg;
                if(smsg.id == id){
                    status = Status.PLAYING;
                    lives = smsg.lives;
                    if(lives < 1){
                        died = true;
                    }
                }
                break;
        }
    }
}