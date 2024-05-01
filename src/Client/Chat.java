package Client;

import org.json.JSONObject;

import java.util.ArrayList;

public class Chat {
    private final int ID;
    private ArrayList<Message> messages;
    private String chatName;

    protected Chat(int id, String chatName) {
        this.ID = id;
        messages = new ArrayList<>();
        this.chatName = chatName;
    }

    protected Chat(JSONObject chat) {
        this.ID = chat.getInt("chat_id");
        this.chatName = chat.getString("chat_name");
        this.messages = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public ArrayList<Message> getMessages() {
        return null; // TODO getMessages
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}
