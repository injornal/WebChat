package GUI.Components;

import java.util.ArrayList;

public class Person {
    
    private String name;
    //private boolean loggedIn;
    private int[] chatIDs;
    //private Chat[] chats;

    public Person(String name) {
        this.name = name;
        //this.loggedIn = status;
        chatIDs = new int[7];
        for(int i = 0; i < chatIDs.length; i++){
            chatIDs[i] = 0;
        }
    }
    /*
    public boolean isLoggedIn(){
        return loggedIn;
    }
    public void active(){
        loggedIn = true;
    }
    public void inactive(){
        loggedIn = false;
    }
    */
    public void setChatIDs(ArrayList<Integer> IDs) {
        for (int i = 0; i < 7; i++) {
            if (i < IDs.size()) {
                chatIDs[i] = IDs.get(i);
            }
            else {
                chatIDs[i] = 0;
            }
        }
    }
    public int[] getChatIds() {
        return chatIDs;
    }
    public String getName() {
        return name;
    }

    public boolean exists(int index){
        return(chatIDs[index] == 0);
    }
    /*
    public Chat[] getChats() {
        return chats;
    }
    public void setChats(Chat[] chats) {
        this.chats = chats;
    }
    public void addChat(int index, int chatID) {
        chatIDs[index] = chatID;
        chats[index] = new Chat(chatID);
    }
    public void joinChat(int index, int chatID, Chat chat) {
        chatIDs[index] = chatID;
        chats[index] = chat;
    }
    */
}
