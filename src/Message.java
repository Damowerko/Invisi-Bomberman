import java.awt.*;
import java.io.Serializable;

public abstract class Message implements Serializable {
    public abstract MessageType getType();

    public enum MessageType {
        Ready, Input, Create, Destroy, Connected, Move
    }
    public enum ObjClass{
        DesObj, Player
    }

    public static Message input(Input input){
        return new InputMessage(input);
    }

    public static Message create(int x, int y, Color color, ObjClass objClass){
        return new CreateMessage(x, y, color, objClass);
    }

    public static Message ready(int id){
        return new ReadyMessage(id);
    }

    public static Message connected(boolean connected, int id){
        return new ConnectedMessage(connected, id);
    }

    public static Message destory(int x, int y){
        return new DestroyMessage(x, y);
    }

    public static Message move(int ox, int oy, int nx, int ny){
        return new MoveMessage(ox, oy, nx, ny);
    }

    Message(){}
}

class ReadyMessage extends Message{
    final int id;
    public Message.MessageType getType(){return Message.MessageType.Ready;}
    ReadyMessage(int id){this.id = id;}
}

class InputMessage extends Message {
    public MessageType getType(){return MessageType.Input;}
    Input input;
    InputMessage(Input input){
        this.input = input;
    }
}

class PositionMessage extends Message{
    public MessageType getType(){return null;}
    final int x;
    final int y;
    PositionMessage(int x, int y){
        this.x = x;
        this.y = y;
    }
}

class CreateMessage extends Message {
    public MessageType getType(){return MessageType.Create;}
    final ObjClass objClass;
    PositionMessage position;
    Color color;
    CreateMessage(int x, int y, Color color, ObjClass objClass){
        position = new PositionMessage(x, y);
        this.objClass = objClass;
    }
}

class DestroyMessage extends Message {
    public MessageType getType(){return MessageType.Destroy;}
    final PositionMessage position;
    DestroyMessage(int x, int y){
        position = new PositionMessage(x, y);
    }
}

class ConnectedMessage extends Message {
    final boolean connected;
    final int id;
    public MessageType getType() {return MessageType.Connected;}
    ConnectedMessage(boolean connected, int id){
        this.connected = connected;
        this.id = id;
    }
}

class MoveMessage extends Message{
    public MessageType getType() {return MessageType.Move;}
    public final PositionMessage oldPos;
    public final PositionMessage newPos;
    MoveMessage(int ox, int oy, int nx, int ny){
        oldPos = new PositionMessage(ox, oy);
        newPos = new PositionMessage(nx, ny);
    }
}