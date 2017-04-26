import java.util.Queue;

public interface Destructible {
    Queue<Message> destroy();
}
