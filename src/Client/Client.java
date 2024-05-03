package Client;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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

    @Override
    public void close() throws Exception {
        this.socket.close();
    }

    public static void main(String[] args) {
        try (Client connie = new Client()) {
            connie.start("127.0.0.1", 8080, "Connie");
            connie.writer.println(new JSONObject().put("action", "SIGN_UP").put("username", connie.username).put("password", "1234"));
            System.out.println(connie.reader.readLine());
            connie.writer.println(new JSONObject().put("action", "LOGIN").put("username", connie.username).put("password", "1234"));
            System.out.println(connie.reader.readLine());
            connie.writer.println(new JSONObject().put("action", "CREATE_CHAT"));
            System.out.println(connie.reader.readLine());
            int chat_id = 0;
            try (Client ivan = new Client()) {
                ivan.start("127.0.0.1", 8080, "Ivan");
                ivan.writer.println(new JSONObject().put("action", "SIGN_UP").put("username", ivan.username).put("password", "1234"));
                System.out.println(ivan.reader.readLine());
                ivan.writer.println(new JSONObject().put("action", "LOGIN").put("username", ivan.username).put("password", "1234"));
                System.out.println(ivan.reader.readLine());
                ivan.writer.println(new JSONObject().put("action", "JOIN_CHAT").put("chat_id", chat_id));
                System.out.println(ivan.reader.readLine());
                ivan.writer.println(new JSONObject().put("action", "SEND").put("content", "Hello Connie")
                        .put("time_stamp", "0:00").put("chat_id", chat_id));
                System.out.println(ivan.reader.readLine());
                System.out.println(connie.reader.readLine());
                connie.writer.println(new JSONObject().put("action", "SEND").put("content", "Hello Ivan")
                        .put("time_stamp", "0:00").put("chat_id", chat_id));
                System.out.println(connie.reader.readLine());
                System.out.println(ivan.reader.readLine());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
