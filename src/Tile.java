import java.awt.*;

public class Tile {
    private final static int TILE_WIDTH = 15; // tile width in mm
    private TileObject object;
    private final int x;
    private final int y;

    public Tile(TileObject object, int x, int y){
        this.object = object;
        this.x = x;
        this.y = y;
    }

    public Tile(int x, int y){
        this(null, x, y);
    }

    /**
     * Get the pixel size of an individual tile. This scales depending on DPI.
     * @return Size in pixels.
     */
    public static int getSize() {
        try {
            int DPI = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();
            return Math.round((TILE_WIDTH * DPI)/25.4f);
        } catch (HeadlessException e){
            return 0;
        }
    }

    public void draw(Graphics g){
        Graphics gt = g.create(getSize() * x, getSize() * y, getSize(), getSize());
        if(object != null){
            object.draw(gt);
        }
        gt.setColor(Color.red);
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

    public TileObject getObject(){
        return object;
    }

    public boolean isDestructible(){
        if(object == null) {
            return false;
        }
        return object instanceof Destructible;
    }

    public boolean isInstanceOf(Class<?> c){
        if(object == null){
            return false;
        }
        return c.isInstance(object);
    }

    // Getters and setters
    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

}
