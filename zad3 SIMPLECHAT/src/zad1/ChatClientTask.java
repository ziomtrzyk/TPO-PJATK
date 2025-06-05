package zad1;

import java.util.List;
import java.util.concurrent.*;

/**
 *
 *  @author Kąkol Bartłomiej S25698
 *
 */


public class ChatClientTask extends FutureTask<ChatClient> {
    public ChatClientTask(Callable<ChatClient> callable) {
        super(callable);
    }

    public static ChatClientTask create(ChatClient client, List<String> messages, int wait) {
        return new ChatClientTask(() -> {
            try {
                client.login();
                sleep(wait);
                for (String message : messages) {
                    client.send(message);
                    sleep(wait);
                }
                client.logout();

                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return client;
        });
    }

    private static void sleep(int waitTime) throws InterruptedException {
        if (waitTime == 0) {
            Thread.sleep(15);
            return;
        }
        Thread.sleep(waitTime);
    }


    public ChatClient getClient() {
        try {
            return this.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}