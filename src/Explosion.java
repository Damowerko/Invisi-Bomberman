import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class Explosion extends TileObject implements Updateable {
    private static long MAX_LIFETIME = 400; // the time the explosion lives
    private static long SPREAD_TIME = 100; // the time for the explosion to propagate
    private static int MAX_RANGE = 100;
    private final int range;
    private final long creationTime;
    private boolean hasSpread = false;

    private final Grid grid;
    private Direction[] directionList;

    /**
     * Create explosion at given point.
     * @return
     */
    public static Explosion explode(int x, int y, Grid grid){
        return new Explosion(
                x,
                y,
                grid,
                new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT},
                MAX_RANGE);
    }

    private Explosion(int x, int y, Grid grid, Direction direction, int range){
        this(x, y, grid, new Direction[]{direction}, range);
    }

    private Explosion(int x, int y, Grid grid, Direction[] directions, int range){
        super(x, y);
        this.grid = grid;
        directionList = directions;
        creationTime = System.currentTimeMillis();
        this.range = range;
    }

    @Override
    public Queue<Message> update() {
        Queue<Message> messageQueue = new LinkedList<>();
        long deltaTime = System.currentTimeMillis() - creationTime;
        if(deltaTime > SPREAD_TIME && !hasSpread && range > 0){
            for(Direction direction : directionList){
                messageQueue.addAll(spread(direction));
            }
        }
        if(deltaTime > MAX_LIFETIME){
            grid.remove(x, y);
            messageQueue.add(Message.destory(x,y));
        }
        return messageQueue;
    }

    private Queue<Message> spread(Direction direction){
        hasSpread = true;
        return explode(x, y, direction);
    }

    private Queue<Message> explode(int x, int y, Direction direction){
        Queue<Message> messageQueue = new LinkedList<>();
        int[] vector = direction.getVector();
        int nx = x + vector[0];
        int ny = y + vector[1];
        if(grid.inBounds(nx, ny)){
            if(grid.isEmpty(nx, ny)){
                Explosion explosion = new Explosion(nx, ny, grid, direction, range - 1);
                grid.place(explosion, nx, ny);
                messageQueue.add(Message.create(nx, ny, null, Message.ObjClass.Explosion));
            } else if(grid.isDestructible(nx, ny)) {
                // destroy object
                Destructible destructible = (Destructible) grid.getObject(nx, ny);
                messageQueue.addAll(destructible.destroy());
            } else if(grid.isInstanceOf(nx, ny, Explosion.class)){
                messageQueue.addAll(explode(nx, ny, direction));
            }
        }
        return  messageQueue;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0, 0, Tile.getSize(), Tile.getSize());
    }
}
