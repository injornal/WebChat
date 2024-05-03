package App.Networking;

import App.Message;
import App.Chat;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import org.json.JSONObject;

// TODO Add a separate thread which will read messages continuously and write them into chats.


public class Client implements AutoCloseable {
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private String username;
    private ArrayList<MessageReceiver> receivers;


    /**
     * Start the client
     * Connects to the server at the given ip and port
     * @param ip server ip
     * @param port server port
     */
    public void start(String ip, int port) {
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

    /**
     * Registers a new user
     * @param username user's username
     * @param password user's password
     * @return true if success, false otherwise
     */
    public boolean sign_up(String username, String password) {
        return true;
    }

    /**
     * Logs a new user in
     * @param username user's username
     * @param password user's password
     * @return true if success, false otherwise
     */
    public boolean login(String username, String password) {
        return true;
    }

    /**
     * Creates a new chat
     * @return a Chat instance if success, null otherwise
     */
    public Chat createChat() {
        return null;
    }

    /**
     * Joins the user to a chat with a given chat_id
     * @param chat_id id of the chat
     * @return true if success, false otherwise
     */
    public boolean joinChat(int chat_id) {
        return true;
    }

    /**
     * Sends a message into a chat
     * @param message message to send
     * @param chat_id id of the chat
     * @return true if success, false otherwise
     */
    public boolean sendMessage(Message message, int chat_id) {
        return true;
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
