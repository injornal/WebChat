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

    public void addAccess(Person p){
        users.add(p);
    }

    public ArrayList<Person> authorizedUsers(){
        return users;
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }
    //chat.getUsers
    //chat.joinchat

}
