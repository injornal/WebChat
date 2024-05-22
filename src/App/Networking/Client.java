package App.Networking;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Provides the networking API for connecting and working with the Server
 */
public class Client implements java.io.Closeable {
    private PrintWriter writer;
    private Socket socket;
    private ResponseManager responseManager;


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
     * Joins the user to a chat
     * @param chatID id of the chat
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
     * @param content the content of the message
     * @param timeStamp the time the message was sent
     * @param chatID the id of the chat
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

    public void addGetQueuedMessagesOnResponseCallback(Consumer<JSONObject> callback) {
        this.responseManager.addGetQueuedMessagesCallback(callback);
    }


    public static void main(String[] args) {
        try(Client client = new Client()) {
            client.start("127.0.0.1", 8080);
            client.addSignUpOnResponseCallback(System.out::println);
            client.addLoginOnResponseCallback(System.out::println);
            client.setReceiveMessageCallback(System.out::println);
            client.addSendMessageOnResponseCallback(System.out::println);
            client.addCreateChatOnResponseCallback(System.out::println);
            client.addJoinChatOnResponseCallback((a) -> System.out.println("HERE"));
            client.signUp("Connie", "1234");
            client.login("Connie", "1234");
            client.createChat();
            client.joinChat(10);
            try(Client client1 = new Client()) {
                TimeUnit.SECONDS.sleep(1);
                client1.start("127.0.0.1", 8080);
                client1.addSignUpOnResponseCallback(System.out::println);
                client1.signUp("IVAN", "1234");
                client1.addLoginOnResponseCallback(System.out::println);
                client1.login("IVAN", "1234");
                client1.addJoinChatOnResponseCallback(System.out::println);
                client1.joinChat(0);
                client1.addSendMessageOnResponseCallback(System.out::println);
                client1.sendMessage("Hello there", "0:00", 0);
                TimeUnit.SECONDS.sleep(1);
            }
            client.addCreateChatOnResponseCallback(System.out::println);
            client.createChat();
            client.addCreateChatOnResponseCallback(System.out::println);
            client.createChat();
            client.addSendMessageOnResponseCallback(System.out::println);
            client.sendMessage("Hi Ivan", "0:00", 0);
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try(Client client = new Client()) {
            client.start("127.0.0.1", 8080);
            client.addSignUpOnResponseCallback(System.out::println);
            client.addLoginOnResponseCallback(System.out::println);
            client.setReceiveMessageCallback(System.out::println);
            client.addSendMessageOnResponseCallback(System.out::println);
            client.addCreateChatOnResponseCallback(System.out::println);
            client.addJoinChatOnResponseCallback(System.out::println);
            client.addGetChatsOnResponseCallback(System.out::println);
            client.addGetMessagesOnResponseCallback(System.out::println);
            client.addGetQueuedMessagesOnResponseCallback(System.out::println);
            client.login("IVAN", "1234");
            client.getQueuedMessages();
            client.createChat();
            client.getChats();
            client.getMessages(0);
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
