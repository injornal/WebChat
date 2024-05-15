package GUI.Components;
import Server.Message;

import java.util.*;


public class Chat {
    
    private int chatID;
    ArrayList<Person> users = new ArrayList<>();
    ArrayList<Message> messages = new ArrayList<>();

    public Chat(int chatID){
        this.chatID = chatID;
    }

    //new chat no paramters
    public int getChatID(){
        return chatID;
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
