package app.networking;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Handles responses from client and server
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
class ResponseManager implements Runnable, java.io.Closeable {
    private final BufferedReader reader;
    private final Thread thread;
    private Consumer<JSONObject> receiveMessageCallback;

    Map<Client.RequestType, Queue<Consumer<JSONObject>>> callbacks = Collections.synchronizedMap(new TreeMap<>());

    protected final Map<String, Integer> requestCounter = Collections.synchronizedMap(new TreeMap<>() {{
        put("SIGN_UP", 0);
        put("LOGIN", 0);
        put("CREATE_CHAT", 0);
        put("JOIN_CHAT", 0);
        put("SEND", 0);
        put("GET_CHATS", 0);
        put("GET_MESSAGES", 0);
        put("GET_QUEUED_MESSAGES", 0);
    }});


    /**
     * Response manager
     * @param reader reads
     */
    protected ResponseManager(BufferedReader reader) {
        this.reader = reader;
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * run
     */
    @Override
    public void run() {
        try {
            String data;
            while (true) { // check corner cases
                data = reader.readLine();
                if (data == null) {
                    break;
                }
                this.parse(data);
            }
        } catch (IOException ignore) {
        }
    }

    /**
     * Parse
     * @param data data
     */
    private void parse(String data) {
        JSONObject dataJSON = new JSONObject(data);

        if (!dataJSON.getString("action").equals("RECEIVE")) {
            if (!this.requestCounter.containsKey(dataJSON.getString("action"))
                    || this.requestCounter.get(dataJSON.getString("action")) == 0) {
                System.out.println("ERROR: UNEXPECTED SERVER RESPONSE: " + data);
                return;
            }
            this.requestCounter.put(dataJSON.getString("action"),
                    this.requestCounter.get(dataJSON.getString("action")) - 1);
        }

        switch (dataJSON.getString("action")) {
            case "SIGN_UP":
                this.signUp(dataJSON);
                break;
            case "LOGIN":
                this.login(dataJSON);
                break;
            case "CREATE_CHAT":
                this.createChat(dataJSON);
                break;
            case "JOIN_CHAT":
                this.joinChat(dataJSON);
                break;
            case "SEND":
                this.sendMessage(dataJSON);
                break;
            case "RECEIVE":
                this.receiveMessage(dataJSON);
                break;
            case "GET_CHATS":
                this.getChats(dataJSON);
                break;
            case "GET_MESSAGES":
                this.getMessages(dataJSON);
                break;
            case "GET_QUEUED_MESSAGES":
                this.getQueuedMessages(dataJSON);
                break;
        }
    }

    /**
     * sign up
     * @param data data
     */
    private void signUp(JSONObject data) {
        this.callbacks.get(Client.RequestType.SIGN_UP).remove().accept(data);
    }

    /**
     * add sign up
     * @param callback callback
     */
    protected void addSignUpOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.SIGN_UP, callback);
    }

    /**
     * login
     * @param data data
     */
    private void login(JSONObject data) {
        this.callbacks.get(Client.RequestType.LOGIN).remove().accept(data);
    }

    /**
     * add login
     * @param callback callback
     */
    protected void addLoginOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.LOGIN, callback);
    }

    /**
     * creat chat
     * @param data data
     */
    private void createChat(JSONObject data) {
        this.callbacks.get(Client.RequestType.CREATE_CHAT).remove().accept(data);
    }

    /**
     * Add create chat
     * @param callback callback
     */
    protected void addCreateChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.CREATE_CHAT, callback);
    }

    /**
     * join chat
     * @param data data
     */
    private void joinChat(JSONObject data) {
        this.callbacks.get(Client.RequestType.JOIN_CHAT).remove().accept(data);
    }

    /**
     * add join chat
     * @param callback callback
     */
    protected void addJoinChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.JOIN_CHAT, callback);
    }

    /**
     * send message
     * @param data data
     */
    private void sendMessage(JSONObject data) {
        this.callbacks.get(Client.RequestType.SEND_MESSAGE).remove().accept(data);
    }

    /**
     * add send message
     * @param callback callback
     */
    protected void addSendMessageOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.SEND_MESSAGE, callback);
    }

    /**
     * recieve message
     * @param data data
     */
    private void receiveMessage(JSONObject data) {
        this.receiveMessageCallback.accept(data);
    }

    /**
     * Receive message callback
     * @param callback callback
     */
    protected void setReceiveMessageCallback(Consumer<JSONObject> callback) {
        this.receiveMessageCallback = callback;
    }

    /**
     * get chats
     * @param data data
     */
    protected void getChats(JSONObject data) {
        this.callbacks.get(Client.RequestType.GET_CHATS).remove().accept(data);
    }

    /**
     * add get chats
     * @param callback callback
     */
    protected void addGetChatsOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.GET_CHATS, callback);
    }

    /**
     * Get mesages
     * @param data data
     */
    protected void getMessages(JSONObject data) {
        this.callbacks.get(Client.RequestType.GET_MESSAGES).remove().accept(data);
    }

    /**
     * add get messages
     * @param callback callback
     */
    protected void addGetMessagesOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.GET_MESSAGES, callback);
    }

    /**
     * Get queued messages
     * @param data data
     */
    protected void getQueuedMessages(JSONObject data) {
        this.callbacks.get(Client.RequestType.GET_QUEUED_MESSAGES).remove().accept(data);
    }

    /**
     * add get queued mesages
     * @param callback callback
     */
    protected void addGetQueuedMessagesCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.GET_QUEUED_MESSAGES, callback);
    }

    protected void addCallback(Client.RequestType requestType, Consumer<JSONObject> callback)
    {
        if (!this.callbacks.containsKey(requestType))
            this.callbacks.put(requestType, new LinkedList<>());
        this.callbacks.get(requestType).add(callback);
    }

    /**
     * close
     */
    @Override
    public void close() throws IOException {
        this.thread.interrupt();
    }
}
