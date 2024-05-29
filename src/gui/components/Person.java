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
    private ArrayList<Integer> chatIDs;

    /**
     * Creates a new person with their name
     * 
     * @param name name
     */
    public Person(String name) {
        this.name = name;
        chatIDs = new ArrayList<Integer>(7);
        for (int i = 0; i < 7; i++) {
            chatIDs.add(-1);
        }
    }

    /**
     * Sets the Person's chatIDs
     * 
     * @param id ids
     * @param index index that the id will be set at
     */
    public void setChatID(int id, int index) {
        chatIDs.set(index, id);
    }

    /**
     * Set chat ids
     * @param ids array of ids being set
     */
    public void setChatID(JSONArray ids) {
        ArrayList<Integer> newIDs = new ArrayList<Integer>(7);
        for (int i = 0; i < 7; i++) {
            if (i < ids.length()) {
                newIDs.add(ids.getInt(i));
            }
            else {
                newIDs.add(-1);
            }
        }
        chatIDs = newIDs;
    }

    /**
     * Get ChatIDs
     * 
     * @return Array of chatIDs
     */
    public ArrayList<Integer> getChatIds() {
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
        return (chatIDs.get(index) != -1);
    }

}