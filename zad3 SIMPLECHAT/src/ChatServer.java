import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Set;

/**
 *
 *  @author Kąkol Bartłomiej S25698
 *
 */


public class ChatServer {
    private String host;
    private int port;

    private Selector selector;
    private ServerSocketChannel serverChannel;

    private boolean running = true;

    private String serverLog;

    private LocalTime start;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
        serverLog = "=== Server log ===\n";
    }

    public void startServer() throws IOException {
        start = LocalTime.now();

        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (running) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (key.isAcceptable()) {
                    SocketChannel client = serverChannel.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = client.read(buffer);
                    if (bytesRead > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[bytesRead];
                        buffer.get(bytes);
                        String message = new String(bytes);
                        System.out.println(message);
                        serverLog += getTime()+  message + "\n";
                        buffer.flip();
                        client.write(buffer);
                    }
                }
            }
        }
    }

    public void stopServer() {
        running = false;

        try{
            for(SelectionKey key : selector.keys()){
                Channel channel = key.channel();
                if(channel instanceof SocketChannel){
                    try{
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            serverChannel.close();
            selector.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getServerLog() {
        return serverLog;
    }
    public String getTime(){
        LocalTime end = LocalTime.now();

        Duration duration = Duration.between(start, end);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        long millis = duration.toMillisPart();

        String formattedTime = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);

        return formattedTime + " ";
    }
}
