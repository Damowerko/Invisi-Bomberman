import java.awt.*;

public class DestructibleObstalce extends Obstacle implements Destructible {
    @Override
    protected Color getColor(){return Color.gray;}

    DestructibleObstalce(int x, int y){
        super(x, y);
    }

    public Message destroy(){
        return Message.destory(x, y);
    }
}
