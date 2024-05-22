package GUI.Components;

import java.util.ArrayList;

public class Person {
    private String name;
    private int[] chatIDs;

    public Person(String name) {
        this.name = name;
        chatIDs = new int[7];
        for(int i = 0; i < chatIDs.length; i++){
            chatIDs[i] = -1;
        }
    }
    
    public void setChatIDs(int[] ids) {
        System.out.println("called");
        for (int i = 0; i < 7; i++) {
            if (i < ids.length) {
                chatIDs[i] = ids[i];
            }
            else {
                chatIDs[i] = -1;
            }
        }
    }
    public void setChatID(int id, int i) {
        chatIDs[i] = id;
    }
    public int[] getChatIds() {
        return chatIDs;
    }
    public boolean exists(int index){
        return(chatIDs[index] != -1);
    }

    public String getName() {
        return name;
    }
}