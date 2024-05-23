package App.Networking;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

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
        this.callbacks.get(Client.RequestType.SIGN_UP).remove().accept(data);
    }

    protected void addSignUpOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.SIGN_UP, callback);
    }

    private void login(JSONObject data) {
        this.callbacks.get(Client.RequestType.LOGIN).remove().accept(data);
    }

    protected void addLoginOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.LOGIN, callback);
    }

    private void createChat(JSONObject data) {
        this.callbacks.get(Client.RequestType.CREATE_CHAT).remove().accept(data);
    }

    protected void addCreateChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.CREATE_CHAT, callback);
    }

    private void joinChat(JSONObject data) {
        this.callbacks.get(Client.RequestType.JOIN_CHAT).remove().accept(data);
    }

    protected void addJoinChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.JOIN_CHAT, callback);
    }

    private void sendMessage(JSONObject data) {
        this.callbacks.get(Client.RequestType.SEND_MESSAGE).remove().accept(data);
    }

    protected void addSendMessageOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.SEND_MESSAGE, callback);
    }

    private void receiveMessage(JSONObject data) {
        this.receiveMessageCallback.accept(data);
    }

    protected void setReceiveMessageCallback(Consumer<JSONObject> callback) {
        this.receiveMessageCallback = callback;
    }

    protected void getChats(JSONObject data) {
        this.callbacks.get(Client.RequestType.GET_CHATS).remove().accept(data);
    }

    protected void addGetChatsOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.GET_CHATS, callback);
    }

    protected void getMessages(JSONObject data) {
        this.callbacks.get(Client.RequestType.GET_MESSAGES).remove().accept(data);
    }

    protected void addGetMessagesOnResponseCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.GET_MESSAGES, callback);
    }

    protected void getQueuedMessages(JSONObject data) {
        this.callbacks.get(Client.RequestType.GET_QUEUED_MESSAGES).remove().accept(data);
    }

    protected void addGetQueuedMessagesCallback(Consumer<JSONObject> callback) {
        this.addCallback(Client.RequestType.GET_QUEUED_MESSAGES, callback);
    }

    protected void addCallback(Client.RequestType requestType, Consumer<JSONObject> callback)
    {
        if (!this.callbacks.containsKey(requestType))
            this.callbacks.put(requestType, new LinkedList<>());
        this.callbacks.get(requestType).add(callback);
    }


    @Override
    public void close() throws IOException {
        this.thread.interrupt();
    }
}
