import java.awt.*;

/** 
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public abstract class TileObject {
    protected int x;
    protected int y;
    TileObject(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public abstract void draw(Graphics g);
}