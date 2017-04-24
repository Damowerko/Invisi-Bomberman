import java.util.Queue;

public class ServerModel {

    // Grid
    Grid grid;

    ServerModel(){
        grid = new Grid();
    }

    public Queue<Message> initializeGrid(){
        return grid.initializeObstacles();
    }

}