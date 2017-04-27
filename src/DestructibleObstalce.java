import java.awt.*;
import java.util.*;

public class DestructibleObstalce extends Obstacle implements Destructible {
    protected final Grid grid;

    DestructibleObstalce(int x, int y, Grid grid){
        super(x, y, Color.gray);
        this.grid = grid;
    }

    DestructibleObstalce(int x, int y, Color color, Grid grid){
        super(x, y, color);
        this.grid = grid;
    }

    public Queue<Message> destroy(){
        Queue<Message> messageQueue = new LinkedList<>();
        messageQueue.add(Message.destory(x, y));
        grid.remove(x, y);
        return messageQueue;
    }
}
