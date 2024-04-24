package Server;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;

public class User {
    private final String username;
    private ArrayList<Message> messages;
    private final Password password;

    protected User(String username, String password) {
        this.username = username;
        this.messages = new ArrayList<>();
        this.password = new Password(password);
    }

    public void receiveMessage(Message message) {
        messages.add(message);
    }

    public JSONArray getAllMessages() {
        ArrayList<Message> messagesList = this.messages;
        ArrayList<Map<String, String>> messageMapList = new ArrayList<>();
        for (Message msg : messagesList) {
            messageMapList.add(msg.toMap());
        }
        return new JSONArray(messageMapList);
    }

    public boolean login(String password) {
        return this.password.equals(new Password(password));
    }

    @Override
    public String toString() {
        return this.username;
    }

    public String getUsername() {
        return username;
    }
}
