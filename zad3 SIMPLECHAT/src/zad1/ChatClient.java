package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 *
 *  @author Kąkol Bartłomiej S25698
 *
 */


public class ChatClient extends Thread {

    private String host;
    private int port;
    private String id;

    private SocketChannel channel;

    private boolean running = true;

    private String chatView;

    public ChatClient(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
        chatView = "=== "+id+" chat view\n";
    }

    public void login(){
        this.start();
    }

    @Override
    public void run() {

        try{
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port));

            while(!channel.finishConnect()){

            }

            send("logged in");

            while(running){
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int bytesRead = channel.read(buffer);
                if(bytesRead > 0){
                    buffer.flip();
                    byte[] bytes = new byte[bytesRead];
                    buffer.get(bytes);
                    String message = new String(bytes);
                    String[] elts = message.split("\t");
                    chatView+=elts[0]+" "+elts[1]+"\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(){
        send("logged out");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        running = false;
        try {
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        chatView+=id+" logged out\n";
    }

    public void send(String req)  {
        req = id+"\t"+req;
        ByteBuffer sendBuffer = ByteBuffer.wrap(req.getBytes(StandardCharsets.UTF_8));
        while(sendBuffer.hasRemaining()){
            try {
                channel.write(sendBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getChatView(){
        return chatView;
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 8080, "1234");
        Thread t1 = new Thread(()->{
            client.login();
        });
        Thread t2 = new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.send(client.id+"\tlog");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.send(client.id+"\tBajo Jajo");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.logout();
            System.out.println(client.getChatView());
        });


        t1.start();
        t2.start();
        System.out.println("wyslano");
    }
}
