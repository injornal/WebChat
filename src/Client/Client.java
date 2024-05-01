package Client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.TreeMap;

// TODO Add a separate thread which will read messages continuously and write them into chats.


public class Client implements AutoCloseable {
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private String username;
    ArrayList<Message> history = new ArrayList<>();

    protected void start(String ip, int port, String username) {
        this.username = username;
        try {
            this.socket = new Socket(ip, port);
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void sendMessage(String content) throws IOException {
        Message message = new Message(content, this.username);
        JSONObject request = new JSONObject();
        request.put("action", "SEND");
        request.put("sender", this.username);
        request.put("content", content);

        history.add(message);
        writer.println(request);
    }

    @Override
    public void close() throws Exception {
        this.socket.close();
    }

    public static void main(String[] args) {
        try (Client client = new Client()) {
            client.start("127.0.0.1", 8080, "Connie");
            try (Client client1 = new Client()) {
                client1.start("127.0.0.1", 8080, "Connie1");
                client.sendMessage("Hello there");
                System.out.println(client1.reader.readLine());
                client1.sendMessage("Hello there mate");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(client.reader.readLine());
            System.out.println(client.reader.readLine());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
