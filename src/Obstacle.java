import java.awt.*;

public class Obstacle extends TileObject {
    protected final Color color;
    Obstacle(int x, int y, Color color){
        super(x, y);
        this.color = color;
    }

    Obstacle(int x, int y){
        super(x, y);
        this.color = Color.black;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(0, 0, Tile.getSize(), Tile.getSize());
    }
}
