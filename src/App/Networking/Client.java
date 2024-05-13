package App.Networking;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class Client implements java.io.Closeable {
    private PrintWriter writer;
    private Socket socket;
    private ResponseManager responseManager;


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
            this.responseManager = new ResponseManager(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
        this.responseManager.close();
    }

    /**
     * Registers a new user
     * @param username user's username
     * @param password user's password
     */
    public void signUp(String username, String password) {
        this.responseManager.requestCounter.put("SIGN_UP", this.responseManager.requestCounter.get("SIGN_UP") + 1);
        JSONObject request = new JSONObject() {{
            put("action", "SIGN_UP");
            put("username", username);
            put("password", password);
        }};
        this.writer.println(request);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a SIGN_UP request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * Attributes in case of:
     * "SUCCESS":null
     * "ERROR":
     *      String message - error message
     *
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
        this.responseManager.requestCounter.put("LOGIN", this.responseManager.requestCounter.get("LOGIN") + 1);
        JSONObject request = new JSONObject() {{
            put("action", "LOGIN");
            put("username", username);
            put("password", password);
        }};
        this.writer.println(request);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a LOGIN request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * Attributes in case of:
     * "SUCCESS": null
     * "ERROR":
     *      String message - error message
     *
     * @param callback a lambda expression of the callback
     */
    public void addLoginOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addLoginOnResponseCallback(callback);
    }

    /**
     * Creates a new chat
     */
    public void createChat() {
        this.responseManager.requestCounter.put("CREATE_CHAT", this.responseManager.requestCounter.get("CREATE_CHAT") + 1);
        JSONObject request = new JSONObject() {{
            put("action", "CREATE_CHAT");
        }};
        this.writer.println(request);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a CREATE_CHAT request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * Attributes in case of:
     * "SUCCESS":
     *      int chat_id
     * "ERROR":
     *      String message - error message
     *
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
        this.responseManager.requestCounter.put("JOIN_CHAT", this.responseManager.requestCounter.get("JOIN_CHAT") + 1);
        JSONObject request = new JSONObject() {{
            put("action", "JOIN_CHAT");
            put("chat_id", chat_id);
        }};
        this.writer.println(request);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a JOIN_CHAT request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * Attributes in case of:
     * "SUCCESS": null
     * "ERROR":
     *      String message - error message
     *
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
        this.responseManager.requestCounter.put("SEND", this.responseManager.requestCounter.get("SEND") + 1);
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
     * on a SEND request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * Attributes in case of:
     * "SUCCESS": null
     * "ERROR":
     *      String message - error message
     *
     * @param callback a lambda expression of the callback
     */
    public void addSendMessageOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addSendMessageOnResponseCallback(callback);
    }

    /**
     * Receives the messages sent to the user while disconnected
     */
    public void getQueuedMessages() {
        this.responseManager.requestCounter.put("GET_QUEUED_MESSAGES", this.responseManager.requestCounter.get("GET_QUEUED_MESSAGES") + 1);
        this.writer.println(new JSONObject().put("action", "GET_QUEUED_MESSAGES"));
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a GET_QUEUED_MESSAGES request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * Attributes in case of:
     * "SUCCESS":
     *      JSONArray messages - array of messages:
     *          String time_stamp
     *          String sender
     *          String content
     *          int chat_id
     * "ERROR":
     *      String message - error message
     * <p>
     *     Note: must be called after logging in
     * </p>
     *
     * @param callback a lambda expression of the callback
     */
    public void addGetQueuedMessagesOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addGetQueuedMessagesCallback(callback);
    }

    /**
     * Sets a callback function which will be called after when somebody sends the user a message
     * The callback must expect a JSONObject object containing the server's response.
     * Attributes:
     *      String time_stamp
     *      String sender
     *      String content
     *      int chat_id
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
            client.sendMessage("Hi Ivan", "0:00", 0);
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try(Client client = new Client()) {
            client.start("127.0.0.1", 8080);
            client.addSignUpOnResponseCallback(System.out::println);
            client.addLoginOnResponseCallback(System.out::println);
            client.addReceiveMessageCallback(System.out::println);
            client.addSendMessageOnResponseCallback(System.out::println);
            client.addCreateChatOnResponseCallback(System.out::println);
            client.addJoinChatOnResponseCallback(System.out::println);
            client.addGetQueuedMessagesOnResponseCallback(System.out::println);
            client.login("IVAN", "1234");
            client.getQueuedMessages();
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
