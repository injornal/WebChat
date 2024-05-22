package GUI.Components;
import Server.Message;

import java.util.*;


public class Chat {
    
    private int chatID;
    ArrayList<Person> users = new ArrayList<Person>();
    ArrayList<Message> messages = new ArrayList<Message>();

    public Chat(int chatID) {
        this.chatID = chatID;
    }

    public int getChatID(){
        return chatID;
    }
    public void setChatID(int id) {
        chatID = id;
    }

    public void joinChat(Person p){
        users.add(p);
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }

    public void receiveMessage(Message m) {
        messages.add(m);
    }

    public ArrayList<Person> getUsers(){
        return users;
    }
    
    public boolean exists(){
        return users.size() != 0;
    }

}
