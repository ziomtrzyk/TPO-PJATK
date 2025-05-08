import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void main(String[] args) throws IOException, InterruptedException {
        ChatServer s = new ChatServer("localhost", 8080);
        Thread t1 = new Thread(()->{
            try {
                s.startServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(()->{
            for(int i= 0; i<10; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("dziala");
            }
            s.stopServer();
            System.out.println(s.getServerLog());
        });


        t1.start();
        t2.start();

    }
}
