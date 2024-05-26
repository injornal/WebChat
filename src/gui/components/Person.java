package gui.components;

import java.util.ArrayList;

import org.json.JSONArray;

/**
 * Person with a name and what chats they are in
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class Person {

    private String name;
    // private boolean loggedIn;
    private int[] chatIDs;
    // private Chat[] chats;

    /**
     * Creates a new person with their name
     * 
     * @param name name
     */
    public Person(String name) {
        this.name = name;
        // this.loggedIn = status;
        chatIDs = new int[7];
        for (int i = 0; i < chatIDs.length; i++) {
            chatIDs[i] = -1;
        }
    }
    /*
     * public boolean isLoggedIn(){
     * return loggedIn;
     * }
     * public void active(){
     * loggedIn = true;
     * }
     * public void inactive(){
     * loggedIn = false;
     * }
     */

    /**
     * Sets the Person's chatIDs
     * 
     * @param IDs ids
     */
    public void setChatID(int id, int index) {
        chatIDs[index] = id;
    }

    public void setChatID(JSONArray ids) {
        System.out.println();
        int[] newIDs = new int[7];
        for (int i = 0; i < 7; i++) {
            if (i < ids.length()) {
                newIDs[i] = ids.getInt(i);
            }
            else {
                newIDs[i] = -1;
            }
        }
        chatIDs = newIDs;
    }

    /**
     * Get ChatIDs
     * 
     * @return Array of chatIDs
     */
    public int[] getChatIds() {
        return chatIDs;
    }

    /**
     * Get name
     * 
     * @return Person's name
     */
    public String getName() {
        return name;
    }

    /**
     * Check if user exists
     * 
     * @param index index to be checked
     * @return chat index
     */
    public boolean exists(int index) {
        return (chatIDs[index] != -1);
    }

    public String out() {
        String result = "";
        for (int i : chatIDs) {
            result += (i + " ");
        }
        return result;
    }
    /*
     * public Chat[] getChats() {
     * return chats;
     * }
     * public void setChats(Chat[] chats) {
     * this.chats = chats;
     * }
     * public void addChat(int index, int chatID) {
     * chatIDs[index] = chatID;
     * chats[index] = new Chat(chatID);
     * }
     * public void joinChat(int index, int chatID, Chat chat) {
     * chatIDs[index] = chatID;
     * chats[index] = chat;
     * }
     */
}
