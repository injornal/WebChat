package app.networking;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Provides the networking API for connecting and working with the Server
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class Client implements Closeable {
    private PrintWriter writer;
    private Socket socket;
    private ResponseManager responseManager;

    public enum RequestType {
        SIGN_UP, LOGIN, CREATE_CHAT,
        JOIN_CHAT, SEND_MESSAGE, GET_CHATS,
        GET_MESSAGES, GET_QUEUED_MESSAGES
    }

    /**
     * Start the client.
     * Connects to the server at the given ip and port.
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

    /**
     * close
     */
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
     * Registers a new user and automatically sets callback
     * 
     * @param username user's username
     * @param password user's password
     * @param callback function to be called after the server's response
     */
    public void signUp(String username, String password, Consumer<JSONObject> callback) {
        this.responseManager.addCallback(RequestType.SIGN_UP, callback);
        this.signUp(username, password);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a SIGN_UP request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * <p>Attributes in case of:</p>
     * <ul>
     *  <li>"SUCCESS":
     *   <ul>
     *    <li>null</li>
     *   </ul>
     *  </li>
     * </ul>
     * <ul>
     *  <li>"ERROR":
     *   <ul>
     *    <li>String message - error message</li>
     *   </ul>
     *  </li>
     * </ul>
     * @param callback a lambda expression of the callback
     */
    public void addSignUpOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addSignUpOnResponseCallback(callback);
    }

    /**
     * Logs in a new user
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
     * Logs in a new user and automatically sets callback
     * @param username user's username
     * @param password user's password
     * @param callback function to be called after the server's response
     */
    public void login(String username, String password, Consumer<JSONObject> callback) {
        this.responseManager.addCallback(RequestType.LOGIN ,callback);
        this.login(username, password);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a LOGIN request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * <p>Attributes in case of:</p>
     * <ul>
     *  <li>"SUCCESS":
     *   <ul>
     *    <li>null</li>
     *   </ul>
     *  </li>
     *  <li>"ERROR":
     *   <ul>
     *    <li>String message - error message</li>
     *   </ul>
     *  </li>
     * </ul>
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
     * Creates a new chat and automatically sets callback
     * @param callback function to be called after the server's response
     */
    public void createChat(Consumer<JSONObject> callback) {
        this.responseManager.addCallback(RequestType.CREATE_CHAT ,callback);
        this.createChat();
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a CREATE_CHAT request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * <p>Attributes in case of:</p>
     * <ul>
     *  <li>"SUCCESS":
     *   <ul>
     *    <li>int chat_id</li>
     *   </ul>
     *  </li>
     *  <li>"ERROR":
     *   <ul>
     *    <li>String message - error message</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param callback a lambda expression of the callback
     */
    public void addCreateChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addCreateChatOnResponseCallback(callback);
    }

    /**
     * Adds a user to a chat
     * @param chatID id of the chat that the user will be joining
     */
    public void joinChat(int chatID) {
        this.responseManager.requestCounter.put("JOIN_CHAT", this.responseManager.requestCounter.get("JOIN_CHAT") + 1);
        JSONObject request = new JSONObject() {{
            put("action", "JOIN_CHAT");
            put("chat_id", chatID);
        }};
        this.writer.println(request);
    }

    
    /**
     * Adds a user to and automatically sets callback
     * @param chatID id of the chat that the user joins
     * @param callback function to be called after the server's response
     */
    public void joinChat(int chatID, Consumer<JSONObject> callback) {
        this.responseManager.addCallback(RequestType.JOIN_CHAT, callback);
        this.joinChat(chatID);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a JOIN_CHAT request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * <p>Attributes in case of:</p>
     * <ul>
     *  <li>"SUCCESS":
     *   <ul>
     *    <li>null</li>
     *   </ul>
     *  </li>
     *  <li>"ERROR":
     *   <ul>
     *    <li>String message - error message</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param callback a lambda expression of the callback
     */
    public void addJoinChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addJoinChatOnResponseCallback(callback);
    }

    /**
     * Sends a message into the given chat
     * @param content content of the message
     * @param timeStamp time that the message was sent
     * @param chatID id of the chat that the message will be sent in
     */
    public void sendMessage(String content, String timeStamp, int chatID) {
        this.responseManager.requestCounter.put("SEND", this.responseManager.requestCounter.get("SEND") + 1);
        JSONObject request = new JSONObject() {{
            put("action", "SEND");
            put("content", content);
            put("time_stamp", timeStamp);
            put("chat_id", chatID);
        }};
        this.writer.println(request);
    }

    /**
     * Sends a message to given chat and automatically sets the callback
     * @param content content of the message
     * @param timeStamp time that the message was sent
     * @param chatID id of the chat that the mesage will be sent in
     * @param callback function to be called after the server's response
     */
    public void sendMessage(String content, String timeStamp, int chatID, Consumer<JSONObject> callback) {
        this.responseManager.addCallback(RequestType.SEND_MESSAGE, callback);
        this.sendMessage(content, timeStamp, chatID);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a SEND request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * <p>Attributes in case of:</p>
     * <ul>
     *  <li>"SUCCESS":
     *   <ul>
     *    <li>null</li>
     *   </ul>
     *  </li>
     *  <li>"ERROR":
     *   <ul>
     *    <li>String message - error message</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param callback a lambda expression of the callback
     */
    public void addSendMessageOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addSendMessageOnResponseCallback(callback);
    }

    /**
     * Requests all the chats that the user is in
     */
    public void getChats()
    {
        this.responseManager.requestCounter.put("GET_CHATS", this.responseManager.requestCounter.get("GET_CHATS") + 1);
        this.writer.println(new JSONObject().put("action", "GET_CHATS"));
    }

    /**
     * Requests all the chats that the user is in and automatically sets callback
     * @param callback function to be called after the servers response
     */
    public void getChats(Consumer<JSONObject> callback) {
        this.responseManager.addCallback(RequestType.GET_CHATS, callback);
        this.getChats();
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a GET_CHATS request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     * <p>Attributes in case of:</p>
     * <ul>
     *  <li>"SUCCESS":
     *   <ul>
     *    <li>JSONArray chats - integer array of ID's of all the accessible to user chats</li>
     *   </ul>
     *  </li>
     *  <li>"ERROR":
     *   <ul>
     *    <li>String message - error message</li>
     *   </ul>
     *  </li>
     * </ul>
     * @param callback callback
     */
    public void addGetChatsOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addGetChatsOnResponseCallback(callback);
    }

    /**
     * Requests all the messages in a given chat
     * @param chatID id of the chat
     */
    public void getMessages(int chatID)
    {
        this.responseManager.requestCounter.put("GET_MESSAGES", this.responseManager.requestCounter.get("GET_MESSAGES") + 1);
        this.writer.println(new JSONObject().put("action", "GET_MESSAGES").put("chat_id", chatID));
    }

    /**
     * Requests all the messages in a given chat and automatically sets the callback
     * @param chatID id of the chat where messages are being retrieved
     * @param callback function to be called after the servers response
     */
    public void getMessages(int chatID, Consumer<JSONObject> callback) {
        this.responseManager.addCallback(RequestType.GET_MESSAGES, callback);
        this.getMessages(chatID);
    }

    /**
     * Sets a callback function which will be called after the server's response
     * on a GET_MESSAGES request.
     * The callback must expect a JSONObject object containing the server's response.
     * The response has an attribute "result" which will return either "SUCCESS" or "ERROR".
     *
     * <p>Attributes in case of:</p>
     * <ul>
     *  <li>"SUCCESS":
     *   <ul>
     *    <li>JSONArray messages - JSONObject array of messages:
     *     <ul>
     *      <li>String time_stamp</li>
     *      <li>String sender</li>
     *      <li>String content</li>
     *      <li>int chat_id</li>
     *     </ul>
     *    </li>
     *   </ul>
     *  </li>
     *  <li>"ERROR":
     *   <ul>
     *    <li>String message - error message</li>
     *   </ul>
     *  </li>
     * </ul>
     * @param callback callback
     */
    public void addGetMessagesOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addGetMessagesOnResponseCallback(callback);
    }

    /**
     * Sets a callback function which will be called after when somebody sends the user a message
     * The callback must expect a JSONObject object containing the server's response.
     * Attributes:
     *      String time_stamp
     *      String sender
     *      String content
     *      int chat_id
     *
     * <p>Attributes:</p>
     * <ul>
     *  <li>String time_stamp</li>
     *  <li>String sender</li>
     *  <li>String content</li>
     *  <li>int chat_id</li>
     * </ul>
     * @param callback callback
     */
    public void setReceiveMessageCallback(Consumer<JSONObject> callback) {
        this.responseManager.setReceiveMessageCallback(callback);
    }

    /**
     * Requests messages queued while the user is inactive
     */
    public void getQueuedMessages() {
        this.responseManager.requestCounter.put("GET_QUEUED_MESSAGES",
                this.responseManager.requestCounter.get("GET_QUEUED_MESSAGES") + 1);
        this.writer.println(new JSONObject().put("action", "GET_QUEUED_MESSAGES"));
    }

    /**
     * Get queued messages and automatically sets callback
     * @param callback function to be called after the servers response
     */
    public void getQueuedMessages(Consumer<JSONObject> callback) {
        this.responseManager.addCallback(RequestType.GET_QUEUED_MESSAGES, callback);
        this.getQueuedMessages();
    }

   /**
    * Adds a server response callback on GET_QUEUED_MESSAGES API method
    * @param callback function to be called after the servers response
    */
    public void addGetQueuedMessagesOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addGetQueuedMessagesCallback(callback);
    }

    /**
     * Adds any server response callback
     * @param requestType specifying type with enum object
     * @param callback function to be called after the servers response
     */
    public void addCallback(RequestType requestType, Consumer<JSONObject> callback) {
        this.responseManager.addCallback(requestType, callback);
    }

    
    public static void main(String[] args) {
        try(Client client = new Client()) {
            client.start("127.0.0.1", 8080);
            client.setReceiveMessageCallback(System.out::println);
            client.signUp("Connie", "1234", System.out::println);
            client.login("Connie", "1234", System.out::println);
            client.createChat(System.out::println);
            client.joinChat(0, System.out::println);
            try(Client client1 = new Client()) {
                TimeUnit.SECONDS.sleep(1);
                client1.start("127.0.0.1", 8080);
                client1.setReceiveMessageCallback(System.out::println);
                client1.signUp("IVAN", "1234", System.out::println);
                client1.login("IVAN", "1234", System.out::println);
                client1.joinChat(0, System.out::println);
                client1.sendMessage("Hello there", "0:00", 0, System.out::println);
                TimeUnit.SECONDS.sleep(1);
            }
            client.createChat(System.out::println);
            client.createChat(System.out::println);
            client.sendMessage("Hi Ivan", "0:00", 0, System.out::println);
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try(Client client = new Client()) {
            client.start("127.0.0.1", 8080);
            client.setReceiveMessageCallback(System.out::println);
            client.login("IVAN", "1234", System.out::println);
            client.getQueuedMessages(System.out::println);
            client.createChat(System.out::println);
            client.getChats(System.out::println);
            client.getMessages(0, System.out::println);
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
