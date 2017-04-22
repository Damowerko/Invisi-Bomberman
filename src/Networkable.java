import Server.Message;
public interface Networkable {
    Message createMessage();
    void update(Message msg);
    int getId();
}
