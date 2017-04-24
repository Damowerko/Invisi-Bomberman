import java.awt.*;

public class Tile {
    private TileObject object;
    private final int x;
    private final int y;

    public Tile(TileObject object, int x, int y){
        this.object = object;
        this.x = x;
        this.y = y;
    }

    /**
     * Get the pixel size of an individual tile. This scales depending on DPI.
     * @return Size in pixels.
     */
    public static int getSize() {
        try {
            int DPI = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
            return Math.round((10 * DPI)/25.4f);
        } catch (HeadlessException e){
            return 0;
        }
    }

    public void draw(Graphics g){
        Graphics gt = g.create(getSize() * x, getSize() * y, getSize(), getSize());
        if(object != null){
            object.draw(gt);
        }
    }

    public boolean isEmpty(){
        return object == null;
    }

    public void setObject(TileObject obj){
        this.object = obj;
    }

    public TileObject removeObject(){
        TileObject object = this.object;
        this.object = null;
        return object;
    }

    // Getters and setters
    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

}
