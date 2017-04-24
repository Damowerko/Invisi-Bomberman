import java.awt.*;
import java.util.*;


public class Grid {
    public static final int GRID_SIZE = 17;
    public static final double OBS_PROB = 0.2;

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
                grid[y][x] = tile;
            }
        }
    }

    public Queue<Message> initializeObstacles(){
        Queue<Message> messageQueue = new LinkedList<>();
        for(Tile[] row : grid){
            for(Tile tile : row){
                if(tile.isEmpty()){
                    if(Math.random() < OBS_PROB){
                        TileObject object = new DestructibleObstalce(tile.getX(),tile.getY());
                        Message msg = Message.create(tile.getX(), tile.getY(), Message.ObjClass.DesObj);
                        messageQueue.add(msg);
                    }
                }
            }
        }
        return messageQueue;
    }

    public void place(TileObject obj, int x, int y){
        grid[y][x].setObject(obj);
    }

    public TileObject remove(int x, int y){
        return grid[y][x].removeObject();
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
