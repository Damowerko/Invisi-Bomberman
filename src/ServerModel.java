import com.sun.xml.internal.ws.api.model.MEP;

import java.awt.*;
import java.util.*;

public class ServerModel {
    private Map<Integer, Boolean> clients;
    private Map<Integer, Player> players;
    Color[] playerColors = {Color.orange, Color.blue, Color.green, Color.magenta};

    private volatile boolean gameOver;

    // Grid
    Grid grid;

    ServerModel(){
        clients = new HashMap<>();
        reset();
    }

    public Queue<Message> reset(){
        Queue<Message> msgs = new LinkedList<>();
        grid = new Grid();
        for(int key : clients.keySet()){
            clients.put(key,false);
        }
        players = new HashMap<>();
        for(int id : clients.keySet()){
            msgs.add(spawnPlayer(id));
        }
        return  msgs;
    }

    private Message spawnPlayer(int id){
        Player player = new Player(0,0, playerColors[players.size()], grid);
        players.put(id, player);
        return player.spawn();
    }

    private void disconnect(int id){
        players.remove(id);
        clients.remove(id);
        if(clients.isEmpty()){
            gameOver = true;
        }
    }

    public Queue<Message> initializeGrid(){
        return grid.initializeObstacles();
    }

    public boolean isReady(){
        if(clients.size() == 0){
            return false;
        }
        boolean ready = true;
        for(boolean rdy : clients.values()){
            if(!rdy){
                ready = false;
            }
        }
        return ready;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public Queue<Message> processMessages(Queue<Message> messageQueue){
        LinkedList<Message> output = new LinkedList<>();
        for(Message msg : messageQueue){
            output.addAll(processMessage(msg));
        }
        return output;
    }

    private Queue<Message> processMessage(Message msg){
        Queue<Message> messageQueue = new LinkedList<>();
        switch(msg.getType()){
            case Create:
                break;
            case Destroy:
                break;
            case Ready:
                ReadyMessage rmsg = (ReadyMessage) msg;
                if(clients.containsKey(rmsg.id)){
                    clients.put(rmsg.id, true);
                }
                break;
            case Input:
                //TODO
                break;
            case Connected:
                ConnectedMessage cmsg = (ConnectedMessage) msg;
                if(cmsg.connected){
                    clients.put(cmsg.id, false);
                } else {
                    disconnect(cmsg.id);
                }
                break;
        }
        return messageQueue;
    }
}