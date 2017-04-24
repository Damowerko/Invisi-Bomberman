public interface Syncable {
    int getId();
    Message createMessage();
    void sync(Message msg);
}