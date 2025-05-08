import java.io.*;
import java.net.*;

public class SimpleClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            out.write("Asiuolo Ideolo".getBytes());
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            System.out.println("Serwer: " + new String(buffer, 0, bytesRead));

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}