import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 *  @author Kąkol Bartłomiej S25698
 *
 */


public class ChatServer extends Thread {
    private String host;
    private int port;
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private boolean running = true;
    private String serverLog;
    private LocalTime start;
    private Map<String, SocketChannel> clientsList;
    private volatile boolean isReady =false;

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
        serverLog = "";
        clientsList = new HashMap<>();
    }

    public void startServer() throws IOException {
        this.start();
        while(!isReady){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        try {
            start = LocalTime.now();
            System.out.println("Server started\n");

            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            isReady = true;

            while (running && selector.isOpen()) {
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
                        int bytesRead;
                        try {
                            bytesRead = client.read(buffer);
                        } catch (IOException e) {
                            handleClientDisconnection(client);
                            continue;
                        }
                        if (bytesRead > 0) {
                            buffer.flip();
                            byte[] bytes = new byte[bytesRead];
                            buffer.get(bytes);
                            String message = new String(bytes);

                            checkMessage(message, client);
                        } else if (bytesRead == -1) {
                            handleClientDisconnection(client);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientDisconnection(SocketChannel client) {
        for (Iterator<Map.Entry<String, SocketChannel>> iterator = clientsList.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, SocketChannel> entry = iterator.next();
            if (entry.getValue() == client) {
                String id = entry.getKey();
                iterator.remove();
                broadcastMessage(id + "\tlogged out");
                break;
            }
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        running = false;
        if (selector != null && selector.isOpen()) {
            selector.wakeup();
        }
        try {
            for (SelectionKey key : selector.keys()) {
                Channel channel = key.channel();
                if (channel instanceof SocketChannel) {
                    channel.close();
                }
            }
            serverChannel.close();
            selector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server stopped");
    }
    public String getServerLog() {
        return serverLog;
    }

    public String getTime() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        String formattedTime = now.format(formatter);
        return formattedTime+" ";
    }

    public void SendClientsMessage(ByteBuffer buffer) {
        buffer.rewind();
        for (Iterator<Map.Entry<String, SocketChannel>> iterator = clientsList.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, SocketChannel> entry = iterator.next();
            SocketChannel channel = entry.getValue();
            if (channel.isOpen()) {
                try {
                    ByteBuffer tempBuffer = buffer.duplicate();
                    while (tempBuffer.hasRemaining()) {
                        channel.write(tempBuffer);
                    }
                } catch (IOException e) {

                    iterator.remove();
                    try {
                        channel.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                iterator.remove();
            }
        }
    }

    public void checkMessage(String message, SocketChannel client) {
        String[] elts = message.split("\t");
        if (elts.length != 2) {
            System.out.println("Invalid message format: " + message);
            return;
        }
        String id = elts[0];
        String content = elts[1];
        if (content.equals("logged in")) {
            serverLog += getTime() + id +" "+content+ "\n";
            clientsList.put(id, client);
            broadcastMessage(id + "\tlogged in");
        } else if (content.equals("logged out")) {
            serverLog += getTime() + id +" "+ content+ "\n";
            clientsList.remove(id);
            broadcastMessage(id + "\tlogged out");
        } else {
            serverLog += getTime() + id + ": "+ content+ "\n";
            broadcastMessage(id + ":\t" + content);
        }
    }

    private void broadcastMessage(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        SendClientsMessage(ByteBuffer.wrap(bytes));
    }
}