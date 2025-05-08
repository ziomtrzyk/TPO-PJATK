import java.util.List;

/**
 *
 *  @author Kąkol Bartłomiej S25698
 *
 */


public class ChatClientTask {
    public static ChatClientTask create(ChatClient c, List<String> msgs, int wait) {
        return new ChatClientTask();
    }

    public ChatServer getClient() {
        return null;
    }
}
