import java.util.*;

public class ServerModel {
    private Map<Integer, Boolean> playerReady;

    // Grid
    Grid grid;

    ServerModel(){
        playerReady = new TreeMap<>();
        reset();
    }

    public void reset(){
        grid = new Grid();
        for(int key : playerReady.keySet()){
            playerReady.put(key,false);
        }
    }

    public Queue<Message> initializeGrid(){
        return grid.initializeObstacles();
    }

    public boolean isReady(){
        if(playerReady.size() == 0){
            return false;
        }
        boolean ready = true;
        for(boolean rdy : playerReady.values()){
            if(!rdy){
                ready = false;
            }
        }
        return ready;
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
                if(playerReady.containsKey(rmsg.id)){
                    playerReady.put(rmsg.id, true);
                }
                break;
            case Input:
                //TODO
                break;
            case Connected:
                ConnectedMessage cmsg = (ConnectedMessage) msg;
                if(cmsg.connected){
                    playerReady.put(cmsg.id, false);
                } else {
                    playerReady.remove(cmsg.id);
                }
                break;
        }
        return messageQueue;
    }
}