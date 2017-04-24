import java.io.Serializable;

public abstract class Message implements Serializable {
    public abstract MessageType getType();

    public enum MessageType {
        Ready, Input, Create, Destroy
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

    public static Message ready(){
        return new ReadyMessage();
    }

    public static Message destory(int x, int y){
        return new DestroyMessage(x, y);
    }
    Message(){}
}

class ReadyMessage extends Message{
    public Message.MessageType getType(){return Message.MessageType.Ready;}
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
    public int x;
    public int y;
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
    PositionMessage position;
    DestroyMessage(int x, int y){
        position = new PositionMessage(x, y);
    }
}