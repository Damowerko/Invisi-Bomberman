import java.awt.*;

public class DestructibleObstalce extends Obstacle implements Destructible {
    DestructibleObstalce(int x, int y){
        super(x, y, Color.gray);
    }

    public Message destroy(){
        return Message.destory(x, y);
    }
}
