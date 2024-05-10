package App.Networking;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class Client implements java.io.Closeable {
    private PrintWriter writer;
    private Socket socket;
    private ResponseManager responseManager;
    private ExecutorService executor;


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
            this.executor = Executors.newSingleThreadExecutor();
            this.responseManager = new ResponseManager(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            executor.submit(responseManager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
        this.executor.close();
    }

    /**
     * Registers a new user
     * @param username user's username
     * @param password user's password
     */
    public void signUp(String username, String password) {
        JSONObject request = new JSONObject() {{
            put("action", "SIGN_UP");
            put("username", username);
            put("password", password);
        }};
        this.writer.println(request);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a SIGN_UP request
     * The callback must expect a JSONObject object containing the server's response
     * @param callback a lambda expression of the callback
     */
    public void addSignUpOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addSignUpOnResponseCallback(callback);
    }

    /**
     * Logs a new user in
     * @param username user's username
     * @param password user's password
     */
    public void login(String username, String password) {
        JSONObject request = new JSONObject() {{
            put("action", "LOGIN");
            put("username", username);
            put("password", password);
        }};
        this.writer.println(request);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a LOGIN request
     * The callback must expect a JSONObject object containing the server's response
     * @param callback a lambda expression of the callback
     */
    public void addLoginOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addLoginOnResponseCallback(callback);
    }

    /**
     * Creates a new chat
     */
    public void createChat() {
        JSONObject request = new JSONObject() {{
            put("action", "CREATE_CHAT");
        }};
        this.writer.println(request);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a CREATE_CHAT request
     * The callback must expect a JSONObject object containing the server's response
     * The chat_id will be assigned by the server and returned in the response
     * under the key "chat_id"
     * @param callback a lambda expression of the callback
     */
    public void addCreateChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addCreateChatOnResponseCallback(callback);
    }

    /**
     * Joins the user to a chat with a given chat_id
     * @param chat_id id of the chat
     */
    public void joinChat(int chat_id) {
        JSONObject request = new JSONObject() {{
            put("action", "JOIN_CHAT");
            put("chat_id", chat_id);
        }};
        this.writer.println(request);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a JOIN_CHAT request
     * The callback must expect a JSONObject object containing the server's response
     * @param callback a lambda expression of the callback
     */
    public void addJoinChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addJoinChatOnResponseCallback(callback);
    }

    /**
     * Sends a message to the given chat
     * @param content the content of the message
     * @param timeStamp the time the message was sent
     * @param chatId the id of the chat
     */
    public void sendMessage(String content, String timeStamp, int chatId) {
        JSONObject request = new JSONObject() {{
            put("action", "SEND");
            put("content", content);
            put("time_stamp", timeStamp);
            put("chat_id", chatId);
        }};
        this.writer.println(request);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a SEND_MESSAGE request
     * The callback must expect a JSONObject object containing the server's response
     * @param callback a lambda expression of the callback
     */
    public void addSendMessageOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addSendMessageOnResponseCallback(callback);
    }

    /**
     * Setts a callback which will be called in case somebody
     * sent us a message
     * @param callback a lambda expression of the callback
     */
    public void addReceiveMessageCallback(Consumer<JSONObject> callback) {
        this.responseManager.addReceiveMessageCallback(callback);
    }

    public static void main(String[] args) {
        try(Client client = new Client()) {
            client.start("127.0.0.1", 8080);
            client.addSignUpOnResponseCallback(System.out::println);
            client.addLoginOnResponseCallback(System.out::println);
            client.addReceiveMessageCallback(System.out::println);
            client.addSendMessageOnResponseCallback(System.out::println);
            client.addCreateChatOnResponseCallback(System.out::println);
            client.addJoinChatOnResponseCallback(System.out::println);
            client.signUp("Connie", "1234");
            client.login("Connie", "1234");
            client.createChat();
            client.joinChat(0);
            try(Client client1 = new Client()) {
                TimeUnit.SECONDS.sleep(1);
                client1.start("127.0.0.1", 8080);
                client1.signUp("IVAN", "1234");
                client1.login("IVAN", "1234");
                client1.joinChat(0);
                client1.sendMessage("Hello there", "0:00", 0);
                TimeUnit.SECONDS.sleep(1);
            }
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void test() {
//        try (Client connie = new Client()) {
//            connie.start("127.0.0.1", 8080);
//            connie.writer.println(new JSONObject().put("action", "SIGN_UP").put("username", "Connie").put("password", "1234"));
//            System.out.println(connie.reader.readLine());
//            connie.writer.println(new JSONObject().put("action", "LOGIN").put("username", "Connie").put("password", "1234"));
//            System.out.println(connie.reader.readLine());
//            connie.writer.println(new JSONObject().put("action", "CREATE_CHAT"));
//            System.out.println(connie.reader.readLine());
//            int chat_id = 0;
//            try (Client ivan = new Client()) {
//                ivan.start("127.0.0.1", 8080);
//                ivan.writer.println(new JSONObject().put("action", "SIGN_UP").put("username", "Ivan").put("password", "1234"));
//                System.out.println(ivan.reader.readLine());
//                ivan.writer.println(new JSONObject().put("action", "LOGIN").put("username", "Ivan").put("password", "1234"));
//                System.out.println(ivan.reader.readLine());
//                ivan.writer.println(new JSONObject().put("action", "JOIN_CHAT").put("chat_id", chat_id));
//                System.out.println(ivan.reader.readLine());
//                ivan.writer.println(new JSONObject().put("action", "SEND").put("content", "Hello Connie")
//                        .put("time_stamp", "0:00").put("chat_id", chat_id));
//                System.out.println(ivan.reader.readLine());
//                System.out.println(connie.reader.readLine());
//                connie.writer.println(new JSONObject().put("action", "SEND").put("content", "Hello Ivan")
//                        .put("time_stamp", "0:00").put("chat_id", chat_id));
//                System.out.println(connie.reader.readLine());
//                System.out.println(ivan.reader.readLine());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
