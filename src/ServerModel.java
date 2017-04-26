import com.sun.xml.internal.ws.api.model.MEP;

import java.awt.*;
import java.util.*;

public class ServerModel implements Updateable {
    private Map<Integer, Boolean> clients;
    private Map<Integer, Player> players;
    private static Color[] playerColors = {Color.orange, Color.blue, Color.green, Color.magenta};

    // Grid
    Grid grid;

    ServerModel() {
        clients = new HashMap<>();
        reset();
    }

    public Queue<Message> reset() {
        Queue<Message> msgs = new LinkedList<>();
        grid = new Grid();
        for (int key : clients.keySet()) {
            clients.put(key, false);
        }
        players = new HashMap<>();
        for (int id : clients.keySet()) {
            msgs.addAll(spawnPlayer(id));
        }
        return msgs;
    }

    private Queue<Message> spawnPlayer(int id) {
        Queue<Message> messageQueue = new LinkedList<>();
        Player player = new Player(0, 0, playerColors[players.size()], grid, id);
        players.put(id, player);
        return player.spawn();
    }

    private void disconnect(int id) {
        players.remove(id);
        clients.remove(id);
    }

    public Queue<Message> initializeGrid() {
        return grid.initializeObstacles();
    }

    public boolean isReady() {
        if (clients.size() < 2) {
            return false;
        }
        boolean ready = true;
        for (boolean rdy : clients.values()) {
            if (!rdy) {
                ready = false;
            }
        }
        return ready;
    }

    public boolean isGameOver() {
        int alivePlayers = 0;
        for(Player player : players.values()){
            if(player.isAlive()){
                alivePlayers++;
            }
        }
        return alivePlayers <= 1;
    }

    public boolean isShutdown(){
        return clients.isEmpty();
    }

    public Queue<Message> processMessages(Queue<Message> messageQueue) {
        LinkedList<Message> output = new LinkedList<>();
        for (Message msg : messageQueue) {
            output.addAll(processMessage(msg));
        }
        return output;
    }

    private Queue<Message> processInput(int id, Input input) {
        Queue<Message> msgs = new LinkedList<>();
        if (input == null) {
            return msgs;
        }
        Player player = players.get(id);
        if(player.isAlive()) {
            if (input == Input.BOMB) {
                player.placeBomb();
            } else {
                msgs.addAll(player.move(input.getDirection()));
            }
        }
        return msgs;
    }

    private Queue<Message> processMessage(Message msg) {
        Queue<Message> messageQueue = new LinkedList<>();
        switch (msg.getType()) {
            case Create:
                break;
            case Destroy:
                break;
            case Ready:
                ReadyMessage rmsg = (ReadyMessage) msg;
                if (clients.containsKey(rmsg.id)) {
                    clients.put(rmsg.id, true);
                }
                break;
            case Input:
                InputMessage imsg = (InputMessage) msg;
                messageQueue.addAll(processInput(imsg.id, imsg.input));
                break;
            case Connected:
                ConnectedMessage cmsg = (ConnectedMessage) msg;
                if (cmsg.connected) {
                    clients.put(cmsg.id, false);
                } else {
                    disconnect(cmsg.id);
                }
                break;
        }
        return messageQueue;
    }

    @Override
    public Queue<Message> update() {
        return grid.update();
    }
}