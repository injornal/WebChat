package Client;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private PrintWriter writer;
    private BufferedReader reader;
    private String token;

    protected void start(String ip, int port) {
        try (Socket socket = new Socket(ip, port)) {
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Logs in a user
     * Sets the token field which allows the other requests
     * @param username user's username
     * @param password user's password
     * @return token or null if exception
     */
    protected String login(String username, String password) throws IOException {
        JSONObject login = new JSONObject();
        login.put("action", "LOGIN");
        login.put("username", username);
        login.put("password", password);
        writer.println(login.toString());
        return reader.readLine();
    }

    /**
     * Return all the chats available for that person
     * @return the chats the user has
     */
    protected ArrayList<Chat> getChats () {
        return null; // TODO getChats
    }

    protected ArrayList<Message> getMessages(Chat chat) {
        return null; // TODO getMessages
    }

    protected ArrayList<Message> getNewMessages(Chat chat) {
        return null; // TODO getNewMessages
    }

    protected void sendMessage() throws IOException {
        // TODO sendMessage
    }

    protected void startChat() {
        // TODO startChat
    }

    public static void main(String[] args) throws java.io.IOException{
        Client.test();
    }

    public static void test() throws java.io.IOException {
        try (Socket socket = new Socket("127.0.0.1", 8080)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("SIGNUP Kostiantyn 1234");
            System.out.println(reader.readLine());
            writer.println("LOGIN Kostiantyn 1234");
            System.out.println(reader.readLine());
            try (Socket socketLeonid = new Socket("127.0.0.1", 8080)) {
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(socketLeonid.getInputStream()));
                PrintWriter writer1 = new PrintWriter(socketLeonid.getOutputStream(), true);
                writer1.println("SIGNUP Ivan 1212");
                System.out.println(reader1.readLine());
                writer1.println("LOGIN Ivan 1212");
                System.out.println(reader1.readLine());
                writer1.println("GET_ALL");
                System.out.println(reader1.readLine());
                writer.println("SEND Ivan Hello there");
                System.out.println(reader.readLine());
                writer1.println("GET_ALL");
                System.out.println(reader1.readLine());
            }
        }
    }
}
