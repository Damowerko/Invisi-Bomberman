import java.awt.*;
import java.util.*;
import java.util.stream.IntStream;


public class Grid {
    public static final int GRID_SIZE = 17;
    private static final double OBS_PROB = 0.3;
    private static final int SPAWN_SIZE = 3;
    private static final int[][] spawnPoints = {{0,0},{0,GRID_SIZE-1},{GRID_SIZE-1,0},{GRID_SIZE-1,GRID_SIZE-1}};

    private Tile[][] grid;

    public Grid(){
        grid = new Tile[GRID_SIZE][GRID_SIZE];
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                TileObject object = null;
                if(x % 2 == 1 && y % 2 == 1){
                    Obstacle obstacle = new Obstacle(x, y);
                    object = obstacle;
                }
                Tile tile = new Tile(object, x, y);
                grid[x][y] = tile;
            }
        }
    }

    public Queue<Message> initializeObstacles(){
        Queue<Message> messageQueue = new LinkedList<>();
        for(Tile[] row : grid){
            for(Tile tile : row){
                if(tile.isEmpty() && !isSpawnArea(tile.getX(), tile.getY())){
                    if(Math.random() < OBS_PROB){
                        TileObject object = new DestructibleObstalce(tile.getX(),tile.getY());
                        Message msg = Message.create(tile.getX(), tile.getY(), null, Message.ObjClass.DesObj);
                        tile.setObject(object);
                        messageQueue.add(msg);
                    }
                }
            }
        }
        return messageQueue;
    }

    public int[] getSpawnPoint(){
        Random rand = new Random();
        int[] point;
        do{
            int randInt = rand.nextInt(4);
            point = spawnPoints[randInt];
        } while(!getTile(point[0], point[1]).isEmpty());
        return point;
    }

    private Tile getTile(int x, int y){
        if(inBounds(x, y)){
            return grid[x][y];
        }
        return null;
    }

    private boolean inBounds(int x, int y){
        if(x < 0 || y < 0 || x > GRID_SIZE - 1 || y > GRID_SIZE - 1){
            return false;
        }
        return true;
    }

    private boolean isSpawnArea(int x, int y){
        return isSpawnAreaHelper(x) && isSpawnAreaHelper(y);
    }

    private boolean isSpawnAreaHelper(int x){
        return x - SPAWN_SIZE < 0 || GRID_SIZE < x + SPAWN_SIZE;
    }

    public void place(TileObject obj, int x, int y){
        grid[x][y].setObject(obj);
    }

    public TileObject remove(int x, int y){
        return grid[x][y].removeObject();
    }

    public void draw(Graphics g){
        for(Tile[] row : grid){
            for(Tile tile : row){
                tile.draw(g);
            }
        }
    }

    public static int getSize(){
        return GRID_SIZE * Tile.getSize();
    }
}
