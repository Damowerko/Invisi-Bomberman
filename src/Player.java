import java.awt.*;
import java.util.*;

public class Player extends Obstacle implements Destructible {
    private final static long MOVEMENT_TIMEOUT = 150;
    private long lastMoveTime;
    private Grid grid;
    private int lives;
    private boolean bombBuffer = false;
    private int id;

    Player(int x, int y, Color color, Grid grid) {
        super(x, y, color);
        lives = 3;
        this.grid = grid;
        lastMoveTime = 0;
    }

    Player(int x, int y, Color color, Grid grid, int id) {
        this(x, y, color, grid);
        this.id = id;
    }

    public boolean isAlive(){
        return lives > 0;
    }

    public Queue<Message> spawn(){
        Queue<Message> messageQueue = new LinkedList<>();
        MoveMessage respawn = (MoveMessage) respawn();
        messageQueue.add(Message.create(respawn.newPos.x, respawn.newPos.y, color, Message.ObjClass.Player));
        messageQueue.add(Message.status(id, lives));
        return messageQueue;
    }

    public Message respawn(){
        int[] spawnPoint = grid.getSpawnPoint();
        Message msg = Message.move(x, y, spawnPoint[0], spawnPoint[1]);
        grid.remove(x,y);
        x = spawnPoint[0];
        y = spawnPoint[1];
        grid.place(this, x, y);
        return msg;
    }

    @Override
    public Queue<Message> destroy() {
        Queue<Message> messageQueue = new LinkedList<>();
        lives--;
        messageQueue.add(Message.status(id, lives));
        if(lives > 0){
            messageQueue.add(respawn());
        } else {
            messageQueue.add(Message.destory(x, y));
            grid.remove(x, y);
        }
        return messageQueue;
    }

    public Queue<Message> move(Direction direction){
        Queue<Message> msgs = new LinkedList<>();
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastMoveTime < MOVEMENT_TIMEOUT){
            return msgs;
        }
        lastMoveTime = currentTime;
        int[] vector = direction.getVector();
        int nx = vector[0] + x;
        int ny = vector[1] + y;
        if(grid.inBounds(nx, ny) && grid.isEmpty(nx, ny)){
            if(grid.move(x, y, nx, ny)){
                msgs.add(Message.move(x, y, nx, ny));
                if(bombBuffer){
                    bombBuffer = false;
                    msgs.add(Message.create(x, y, null, Message.ObjClass.Bomb));
                    grid.place(new Bomb(x, y, grid), x, y);
                }
                x = nx;
                y = ny;
            }
        }
        return msgs;
    }

    public void placeBomb() {
        bombBuffer = true;
    }

    @Override
    protected void drawShape(Graphics g){
        g.fillOval(0, 0, Tile.getSize(), Tile.getSize());
    }
}
