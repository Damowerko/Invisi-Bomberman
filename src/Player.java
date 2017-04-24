import java.awt.*;

public class Player extends Obstacle implements Destructible {
    private Grid grid;
    private int lives;

    Player(int x, int y, Color color, Grid grid) {
        super(x, y, color);
        lives = 3;
        this.grid = grid;
    }

    public Message spawn(){
        MoveMessage respawn = (MoveMessage) respawn();
        return Message.create(respawn.newPos.x, respawn.oldPos.y, null, Message.ObjClass.Player);
    }

    public Message respawn(){
        int[] spawnPoint = grid.getSpawnPoint();
        Message msg = Message.move(x, y, spawnPoint[0], spawnPoint[1]);
        x = spawnPoint[0];
        y = spawnPoint[1];
        grid.place(this, x, y);
        return msg;
    }

    @Override
    public Message destroy() {
        return null;
    }

    public Message move(Direction direction){
        int[] vector = direction.getVector();
        return null;
    }

    @Override
    protected void drawShape(Graphics g){
        g.fillOval(x, y, Tile.getSize(), Tile.getSize());
    }
}
