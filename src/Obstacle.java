import java.awt.*;

public class Obstacle extends TileObject {
    Obstacle(int x, int y){
        super(x, y);
    }

    @Override
    protected Color getColor(){return Color.black;}

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Tile.getSize(), Tile.getSize());
    }
}
