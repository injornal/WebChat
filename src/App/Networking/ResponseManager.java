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
    private final Queue<Consumer<JSONObject>> signUpOnResponseCallbacks = new LinkedList<>();
    private final Queue<Consumer<JSONObject>> loginOnResponseCallbacks = new LinkedList<>();
    private final Queue<Consumer<JSONObject>> createChatOnResponseCallbacks = new LinkedList<>();
    private final Queue<Consumer<JSONObject>> joinChatOnResponseCallbacks = new LinkedList<>();
    private final Queue<Consumer<JSONObject>> sendMessageOnResponseCallbacks = new LinkedList<>();
    private final Queue<Consumer<JSONObject>> getChatCallbacks = new LinkedList<>();
    private final Queue<Consumer<JSONObject>> getMessagesCallbacks = new LinkedList<>();
    private final Queue<Consumer<JSONObject>> getQueuedMessagesCallbacks = new LinkedList<>();
    private Consumer<JSONObject> receiveMessageCallback;

    /**
     * Synchronized map
     */
    protected final Map<String, Integer> requestCounter = Collections.synchronizedMap(new TreeMap<>() {
        {
            put("SIGN_UP", 0);
            put("LOGIN", 0);
            put("CREATE_CHAT", 0);
            put("JOIN_CHAT", 0);
            put("SEND", 0);
            put("GET_CHATS", 0);
            put("GET_MESSAGES", 0);
            put("GET_QUEUED_MESSAGES", 0);
        }
    });

    /**
     * Response manager
     * @param reader reads
     */
    protected ResponseManager(BufferedReader reader) {
        this.reader = reader;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    /**
     * run
     */
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
        this.signUpOnResponseCallbacks.remove().accept(data);
    }

    /**
     * add sign up
     * @param callback callback
     */
    protected void addSignUpOnResponseCallback(Consumer<JSONObject> callback) {
        this.signUpOnResponseCallbacks.add(callback);
    }

    /**
     * login
     * @param data data
     */
    private void login(JSONObject data) {
        this.loginOnResponseCallbacks.remove().accept(data);
    }

    /**
     * add login
     * @param callback callback
     */
    protected void addLoginOnResponseCallback(Consumer<JSONObject> callback) {
        this.loginOnResponseCallbacks.add(callback);
    }

    /**
     * creat chat
     * @param data data
     */
    private void createChat(JSONObject data) {
        this.createChatOnResponseCallbacks.remove().accept(data);
    }

    /**
     * Add create chat
     * @param callback callback
     */
    protected void addCreateChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.createChatOnResponseCallbacks.add(callback);
    }

    /**
     * join chat
     * @param data data
     */
    private void joinChat(JSONObject data) {
        this.joinChatOnResponseCallbacks.remove().accept(data);
    }

    /**
     * add join chat
     * @param callback callback
     */
    protected void addJoinChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.joinChatOnResponseCallbacks.add(callback);
    }

    /**
     * send message
     * @param data data
     */
    private void sendMessage(JSONObject data) {
        this.sendMessageOnResponseCallbacks.remove().accept(data);
    }

    /**
     * add send message
     * @param callback callback
     */
    protected void addSendMessageOnResponseCallback(Consumer<JSONObject> callback) {
        this.sendMessageOnResponseCallbacks.add(callback);
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
        this.getChatCallbacks.remove().accept(data);
    }

    /**
     * add get chats
     * @param callback callback
     */
    protected void addGetChatsOnResponseCallback(Consumer<JSONObject> callback) {
        this.getChatCallbacks.add(callback);
    }

    /**
     * Get mesages
     * @param data data
     */
    protected void getMessages(JSONObject data) {
        this.getMessagesCallbacks.remove().accept(data);
    }

    /**
     * add get messages
     * @param callback callback
     */
    protected void addGetMessagesOnResponseCallback(Consumer<JSONObject> callback) {
        this.getMessagesCallbacks.add(callback);
    }

    /**
     * Get queued messages
     * @param data data
     */
    protected void getQueuedMessages(JSONObject data) {
        this.getQueuedMessagesCallbacks.remove().accept(data);
    }

    /**
     * add get queued mesages
     * @param callback callback
     */
    protected void addGetQueuedMessagesCallback(Consumer<JSONObject> callback) {
        this.getQueuedMessagesCallbacks.add(callback);
    }

    @Override
    /**
     * close
     */
    public void close() throws IOException {
        this.thread.interrupt();
    }
}
