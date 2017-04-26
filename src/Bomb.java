import java.awt.*;
import java.util.*;

public class Bomb extends DestructibleObstalce implements Updateable {
    private final static long FUSE = 3000;
    private final long creationTime;
    private final Grid grid;

    Bomb(int x, int y, Grid grid) {
        super(x, y, Color.black);
        creationTime = System.currentTimeMillis();
        this.grid = grid;
    }

    public Queue<Message> update(){
        Queue<Message> messageQueue = new LinkedList<>();
        long currentTime = System.currentTimeMillis();
        if(currentTime - creationTime > FUSE){
            return destroy();
        }
        return messageQueue;
    }

    @Override
    public Queue<Message> destroy(){
        Queue<Message> messageQueue = new LinkedList<>();
        grid.remove(x, y);
        messageQueue.add(Message.destory(x, y));
        grid.place(Explosion.explode(x, y, grid), x, y);
        messageQueue.add(Message.create(x, y, null, Message.ObjClass.Explosion));
        return  messageQueue;
    }

    @Override
    protected void drawShape(Graphics g){
        g.fillOval(0, 0, Tile.getSize(), Tile.getSize());
    }
}
