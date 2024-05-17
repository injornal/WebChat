package GUI.Components;

public class Person {
    
    private String name;
    private boolean loggedIn;
    private int[] chatIDs;
    private Chat[] chats;

    public Person(String name, boolean status){
        this.name = name;
        this.loggedIn = status;
        chatIDs = new int[8];
        for(int i = 0; i < chatIDs.length; i++){
            chatIDs[i] = -1;
        }
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public void active(){
        loggedIn = true;
    }

    public void inactive(){
        loggedIn = false;
    }

    public String getName(){
        return name;
    }

    public boolean exists(int index){
        return(-1 == chatIDs[index]);
    }

    public Chat[] getChats() {
        return chats;
    }

    public void addChat(int index, int chatID) {
        chatIDs[index] = chatID;
        chats[index] = new Chat(chatID);
    }
    
    public void joinChat(int index, int chatID, )
}
