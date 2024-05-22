package App.Networking;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

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


    protected ResponseManager(BufferedReader reader) {
        this.reader = reader;
        this.thread = new Thread(this);
        this.thread.start();
    }

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
        } catch (IOException ignore) {}
    }

    private void parse(String data) {
        JSONObject dataJSON = new JSONObject(data);

        if(!dataJSON.getString("action").equals("RECEIVE")) {
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

    private void signUp(JSONObject data) {
        this.signUpOnResponseCallbacks.remove().accept(data);
    }

    protected void addSignUpOnResponseCallback(Consumer<JSONObject> callback) {
        this.signUpOnResponseCallbacks.add(callback);
    }

    private void login(JSONObject data) {
        this.loginOnResponseCallbacks.remove().accept(data);
    }

    protected void addLoginOnResponseCallback(Consumer<JSONObject> callback) {
        this.loginOnResponseCallbacks.add(callback);
    }

    private void createChat(JSONObject data) {
        this.createChatOnResponseCallbacks.remove().accept(data);
    }

    protected void addCreateChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.createChatOnResponseCallbacks.add(callback);
    }

    private void joinChat(JSONObject data) {
        this.joinChatOnResponseCallbacks.remove().accept(data);
    }

    protected void addJoinChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.joinChatOnResponseCallbacks.add(callback);
    }

    private void sendMessage(JSONObject data) {
        this.sendMessageOnResponseCallbacks.remove().accept(data);
    }

    protected void addSendMessageOnResponseCallback(Consumer<JSONObject> callback) {
        this.sendMessageOnResponseCallbacks.add(callback);
    }

    private void receiveMessage(JSONObject data) {
        this.receiveMessageCallback.accept(data);
    }

    protected void setReceiveMessageCallback(Consumer<JSONObject> callback) {
        this.receiveMessageCallback = callback;
    }

    protected void getChats(JSONObject data) {
        this.getChatCallbacks.remove().accept(data);
    }

    protected void addGetChatsOnResponseCallback(Consumer<JSONObject> callback) {
        this.getChatCallbacks.add(callback);
    }

    protected void getMessages(JSONObject data) {
        this.getMessagesCallbacks.remove().accept(data);
    }

    protected void addGetMessagesOnResponseCallback(Consumer<JSONObject> callback) {
        this.getMessagesCallbacks.add(callback);
    }

    protected void getQueuedMessages(JSONObject data) {
        this.getQueuedMessagesCallbacks.remove().accept(data);
    }

    protected void addGetQueuedMessagesCallback(Consumer<JSONObject> callback) {
        this.getQueuedMessagesCallbacks.add(callback);
    }


    @Override
    public void close() throws IOException {
        this.thread.interrupt();
    }
}
