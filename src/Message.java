import java.io.Serializable;

public abstract class Message implements Serializable {
    public abstract MessageType getType();

    public enum MessageType {
        Ready, Input, Create, Destroy, Connected
    }
    public enum ObjClass{
        DesObj
    }

    public static Message input(Input input){
        return new InputMessage(input);
    }

    public static Message create(int x, int y, ObjClass objClass){
        return new CreateMessage(x, y, objClass);
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
    Message(){}
}

class ReadyMessage extends Message{
    public final int id;
    public Message.MessageType getType(){return Message.MessageType.Ready;}
    ReadyMessage(int id){this.id = id;}
}

class InputMessage extends Message {
    public MessageType getType(){return MessageType.Input;}
    public Input input;
    InputMessage(Input input){
        this.input = input;
    }
}

class PositionMessage extends Message{
    public MessageType getType(){return null;}
    public final int x;
    public final int y;
    PositionMessage(int x, int y){
        this.x = x;
        this.y = y;
    }
}

class CreateMessage extends Message {
    public MessageType getType(){return MessageType.Create;}
    public final ObjClass objClass;
    PositionMessage position;
    CreateMessage(int x, int y, ObjClass objClass){
        position = new PositionMessage(x, y);
        this.objClass = objClass;
    }
}

class DestroyMessage extends Message {
    public MessageType getType(){return MessageType.Destroy;}
    public final PositionMessage position;
    DestroyMessage(int x, int y){
        position = new PositionMessage(x, y);
    }
}

class ConnectedMessage extends Message {
    public final boolean connected;
    public final int id;
    public MessageType getType() {return MessageType.Connected;}
    ConnectedMessage(boolean connected, int id){
        this.connected = connected;
        this.id = id;
    }
}