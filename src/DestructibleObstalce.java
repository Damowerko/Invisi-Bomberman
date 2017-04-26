import java.awt.*;
import java.util.*;

public class DestructibleObstalce extends Obstacle implements Destructible {
    DestructibleObstalce(int x, int y){
        super(x, y, Color.gray);
    }

    DestructibleObstalce(int x, int y, Color color){
        super(x, y, color);
    }

    public Queue<Message> destroy(){
        Queue<Message> messageQueue = new LinkedList<>();
        messageQueue.add(Message.destory(x, y));
        return messageQueue;
    }
}
