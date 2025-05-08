import java.io.*;
import java.net.*;

public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Serwer działa...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            out.write(("Echo: " + new String(buffer, 0, bytesRead)).getBytes());
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
