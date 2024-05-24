package GUI.Components;

import java.util.*;

import org.json.JSONArray;

public class Chat {
    private int chatID;
    private ArrayList<Message> messages = new ArrayList<Message>();

    public Chat(int chatID) {
        this.chatID = chatID;
    }
    public Chat(int chatID, JSONArray messages) {
        this.chatID = chatID;
        ArrayList<Message> msgs = new ArrayList<Message>();
        for (int i = 0; i < messages.length(); i++) {
            msgs.add((Message) messages.getJSONObject(i));
        }
        this.messages = msgs;
    }
    public int getChatID(){
        return chatID;
    }
    public void setChatID(int id) {
        chatID = id;
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }
    public void receiveMessage(Message m) {
        messages.add(m);
    }
}