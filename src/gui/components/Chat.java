package gui.components;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A chat with its chatID list of users, and list of messages
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class Chat {
    private int chatID;
    /**
     * messages
     */
    ArrayList<Message> messages = new ArrayList<Message>();

    /**
     * Make a new chat with chatID
     * 
     * @param chatID chatID
     */
    public Chat(int chatID) {
        this.chatID = chatID;
    }
    public Chat(int chatID, JSONArray messages) {
        this.chatID = chatID;
        ArrayList<Message> msgs = new ArrayList<Message>();
        for (int i = 0; i < messages.length(); i++) {
            msgs.add(fromJSON(messages.getJSONObject(i)));
        }
        this.messages = msgs;
    }
    public int getChatID(){
        return chatID;
    }
    public void setChatID(int id) {
        chatID = id;
    }

    /**
     * Get messages in a chat
     * 
     * @return List of messages
     */
    public ArrayList<Message> getMessages() {
        return messages;
    }

    /**
     * Add messages to the chat
     * 
     * @param m message to be added
     */
    public void receiveMessage(Message m) {
        messages.add(m);
    }

    public Message fromJSON(JSONObject obj) {
        return new Message(obj.getString("content"),
        obj.getString("sender"),obj.getString("time_stamp"),
        obj.getInt("chat_id"));
    }
}
