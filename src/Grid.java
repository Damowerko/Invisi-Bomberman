import java.awt.*;
import java.util.*;


public class Grid implements Updateable {
    public static final int GRID_SIZE = 11;
    private static final double OBS_PROB = 0.4;
    private static final int SPAWN_SIZE = 3;
    private static final int[][] spawnPoints = {{0, 0}, {0, GRID_SIZE - 1}, {GRID_SIZE - 1, 0}, {GRID_SIZE - 1, GRID_SIZE - 1}};

    private Tile[][] grid;
    private Set<Updateable> updateableTileObjects;

    public Grid() {
        updateableTileObjects = new HashSet<>();
        grid = new Tile[GRID_SIZE][GRID_SIZE];
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                Tile tile;
                if (x % 2 == 1 && y % 2 == 1) {
                    tile = new Tile(new Obstacle(x, y), x, y);
                } else {
                    tile = new Tile(x, y);
                }
                grid[x][y] = tile;
            }
        }
    }

    public Queue<Message> initializeObstacles() {
        Queue<Message> messageQueue = new LinkedList<>();
        for (Tile[] row : grid) {
            for (Tile tile : row) {
                if (tile.isEmpty() && !isSpawnArea(tile.getX(), tile.getY())) {
                    if (Math.random() < OBS_PROB) {
                        TileObject object = new DestructibleObstalce(tile.getX(), tile.getY(), this);
                        Message msg = Message.create(tile.getX(), tile.getY(), null, Message.ObjClass.DesObj);
                        tile.setObject(object);
                        messageQueue.add(msg);
                    }
                }
            }
        }
        return messageQueue;
    }

    public Queue<Message> update() {
        Queue<Message> msgs = new LinkedList<>();
        Updateable[] objects = updateableTileObjects.toArray(new Updateable[updateableTileObjects.size()]); // to prevent concurrent acccess
        for (Updateable obj : objects) {
            msgs.addAll(obj.update());
        }
        return msgs;
    }

    /**
     * Moves object from one point to the other
     *
     * @param x  Old x
     * @param y  Old y
     * @param nx New x
     * @param ny New y
     * @return True if successful. That is the old object existed.
     */
    public boolean move(int x, int y, int nx, int ny) {
        if (isEmpty(x, y)) {
            return false;
        }
        TileObject object = getTile(x, y).removeObject();
        place(object, nx, ny);
        return true;
    }

    public TileObject getObject(int x, int y){
        return getTile(x, y).getObject();
    }

    public boolean isEmpty(int x, int y) {
        return getTile(x, y).isEmpty();
    }

    public int[] getSpawnPoint() {
        Random rand = new Random();
        int[] point;
        do {
            int randInt = rand.nextInt(4);
            point = spawnPoints[randInt];
        } while (!getTile(point[0], point[1]).isEmpty());
        return point;
    }

    private Tile getTile(int x, int y) {
        if (inBounds(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    public boolean inBounds(int x, int y) {
        if (x < 0 || y < 0 || x > GRID_SIZE - 1 || y > GRID_SIZE - 1) {
            return false;
        }
        return true;
    }

    private boolean isSpawnArea(int x, int y) {
        return isSpawnAreaHelper(x) && isSpawnAreaHelper(y);
    }

    private boolean isSpawnAreaHelper(int n) {
        return n - SPAWN_SIZE < 0 || GRID_SIZE < 1 + n + SPAWN_SIZE;
    }

    public void place(TileObject obj, int x, int y) {
        if(obj instanceof Updateable){
            updateableTileObjects.add((Updateable) obj);
        }
        grid[x][y].setObject(obj);
    }

    public TileObject remove(int x, int y) {
        TileObject tileObject = grid[x][y].removeObject();
        if(tileObject instanceof Updateable){
            updateableTileObjects.remove(tileObject);
        }
        return tileObject;
    }

    public void draw(Graphics g) {
        for (Tile[] row : grid) {
            for (Tile tile : row) {
                tile.draw(g);
            }
        }
    }

    public boolean isDestructible(int x, int y){
        return getTile(x,y).isDestructible();
    }

    public boolean isInstanceOf(int x, int y, Class c){
        return getTile(x, y).isInstanceOf(c);
    }

    public static int getSize() {
        return GRID_SIZE * Tile.getSize();
    }
}
