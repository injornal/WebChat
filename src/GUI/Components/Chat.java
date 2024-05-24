package gui.components;

import java.util.*;

import server.Message;

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
     * users
     */
    ArrayList<Person> users = new ArrayList<>();
    /**
     * messages
     */
    ArrayList<Message> messages = new ArrayList<>();

    /**
     * Make a new chat with chatID
     * 
     * @param chatID chatID
     */
    public Chat(int chatID) {
        this.chatID = chatID;
    }

    /**
     * Get ChatID
     * 
     * @return chatID
     */
    public int getChatID() {
        return chatID;
    }
    public void setChatID(int id) {
        chatID = id;
    }

    /**
     * Adds a Person to the chat
     * 
     * @param p Person being added
     */
    public void joinChat(Person p) {
        users.add(p);
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

    /**
     * Get users in a chat
     * 
     * @return List of users
     */
    public ArrayList<Person> getUsers() {
        return users;
    }

    /**
     * Check if user exists
     * 
     * @return true or false
     */
    public boolean exists() {
        return users.size() != 0;
    }

}
