package App.Networking;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class ResponseManager implements Runnable, java.io.Closeable {
    private final BufferedReader reader;
    private final Thread thread;
    private final ArrayList<Consumer<JSONObject>> signUpOnResponseCallbacks = new ArrayList<>();
    private final ArrayList<Consumer<JSONObject>> loginOnResponseCallbacks = new ArrayList<>();
    private final ArrayList<Consumer<JSONObject>> createChatOnResponseCallbacks = new ArrayList<>();
    private final ArrayList<Consumer<JSONObject>> joinChatOnResponseCallbacks = new ArrayList<>();
    private final ArrayList<Consumer<JSONObject>> sendMessageOnResponseCallbacks = new ArrayList<>();
    private final ArrayList<Consumer<JSONObject>> receiveMessageCallbacks = new ArrayList<>();
    private final ArrayList<Consumer<JSONObject>> getChatCallbacks = new ArrayList<>();
    private final ArrayList<Consumer<JSONObject>> getMessagesCallbacks = new ArrayList<>();

    protected final Map<String, Integer> requestCounter = Collections.synchronizedMap(new TreeMap<>() {{
        put("SIGN_UP", 0);
        put("LOGIN", 0);
        put("CREATE_CHAT", 0);
        put("JOIN_CHAT", 0);
        put("SEND", 0);
        put("GET_CHATS", 0);
        put("GET_MESSAGES", 0);
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
        }
    }

    private void signUp(JSONObject data) {
        for (Consumer<JSONObject> callback : this.signUpOnResponseCallbacks) {
            callback.accept(data);
        }
    }

    protected void addSignUpOnResponseCallback(Consumer<JSONObject> callback) {
        this.signUpOnResponseCallbacks.add(callback);
    }

    private void login(JSONObject data) {
        for (Consumer<JSONObject> callback : this.loginOnResponseCallbacks) {
            callback.accept(data);
        }
    }

    protected void addLoginOnResponseCallback(Consumer<JSONObject> callback) {
        this.loginOnResponseCallbacks.add(callback);
    }

    private void createChat(JSONObject data) {
        for (Consumer<JSONObject> callback : this.createChatOnResponseCallbacks) {
            callback.accept(data);
        }
    }

    protected void addCreateChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.createChatOnResponseCallbacks.add(callback);
    }

    private void joinChat(JSONObject data) {
        for (Consumer<JSONObject> callback : this.joinChatOnResponseCallbacks) {
            callback.accept(data);
        }
    }

    protected void addJoinChatOnResponseCallback(Consumer<JSONObject> callback) {
        this.joinChatOnResponseCallbacks.add(callback);
    }

    private void sendMessage(JSONObject data) {
        for (Consumer<JSONObject> callback : this.sendMessageOnResponseCallbacks) {
            callback.accept(data);
        }
    }

    protected void addSendMessageOnResponseCallback(Consumer<JSONObject> callback) {
        this.sendMessageOnResponseCallbacks.add(callback);
    }

    private void receiveMessage(JSONObject data) {
        for (Consumer<JSONObject> callback : this.receiveMessageCallbacks) {
            callback.accept(data);
        }
    }

    protected void addReceiveMessageCallback(Consumer<JSONObject> callback) {
        this.receiveMessageCallbacks.add(callback);
    }

    protected void getChats(JSONObject data) {
        for (Consumer<JSONObject> callback : this.getChatCallbacks) {
            callback.accept(data);
        }
    }

    protected void addGetChatsOnResponseCallback(Consumer<JSONObject> callback) {
        this.getChatCallbacks.add(callback);
    }

    protected void getMessages(JSONObject data) {
        for (Consumer<JSONObject> callback : this.getMessagesCallbacks) {
            callback.accept(data);
        }
    }

    protected void addGetMessagesOnResponseCallback(Consumer<JSONObject> callback) {
        this.getMessagesCallbacks.add(callback);
    }

    @Override
    public void close() throws IOException {
        this.thread.interrupt();
    }
}
