import java.io.IOException;
import java.nio.channels.*;
import java.net.*;
import java.nio.ByteBuffer;

public class NonBlockingClient {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("localhost", 8080));

        while (!channel.finishConnect()) {
            System.out.println("Łączenie...");
        }

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("Witaj!".getBytes());
        buffer.flip();
        channel.write(buffer);

        buffer.clear();
        int bytesRead = channel.read(buffer);
        if (bytesRead > 0) {
            buffer.flip();
            System.out.println("Odebrano: " + new String(buffer.array(), 0, bytesRead));
        }

        channel.close();
    }
}
