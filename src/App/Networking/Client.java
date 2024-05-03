package App.Networking;

import App.Message;
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
    private ArrayList<MessageReceiver> receivers;


    public void start(String ip, int port) {
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


    public Response sign_up(String username, String password) {
        return null;
    }

    public Response login(String username, String password) {
        return null;
    }

    public Response createChat() {
        return null;
    }

    public Response joinChat(int chat_id) {
        return null;
    }

    public Response sendMessage(Message message, int chat_id) {
        return null;
    }

    public static void test() {
        try (Client connie = new Client()) {
            connie.start("127.0.0.1", 8080);
            connie.writer.println(new JSONObject().put("action", "SIGN_UP").put("username", "Connie").put("password", "1234"));
            System.out.println(connie.reader.readLine());
            connie.writer.println(new JSONObject().put("action", "LOGIN").put("username", "Connie").put("password", "1234"));
            System.out.println(connie.reader.readLine());
            connie.writer.println(new JSONObject().put("action", "CREATE_CHAT"));
            System.out.println(connie.reader.readLine());
            int chat_id = 0;
            try (Client ivan = new Client()) {
                ivan.start("127.0.0.1", 8080);
                ivan.writer.println(new JSONObject().put("action", "SIGN_UP").put("username", "Ivan").put("password", "1234"));
                System.out.println(ivan.reader.readLine());
                ivan.writer.println(new JSONObject().put("action", "LOGIN").put("username", "Ivan").put("password", "1234"));
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
