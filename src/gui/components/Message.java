package gui.components;

import org.json.JSONObject;

/**
 * A message with its content, sender, timestamp, and chatID
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class Message {
    private final String content;
    private final String sender;
    private final String timeStamp;
    private final int chatId;

    /**
     * Creates a new message
     * 
     * @param content   content of the message
     * @param sender    sender of the message
     * @param timeStamp time at which the message was sent
     * @param chatId    chat the message will be sent to
     */
    public Message(String content, String sender, String timeStamp, int chatId) {
        this.content = content;
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.chatId = chatId;
    }
    
    /**
     * Returns a messages content
     * @return content
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Returns a messages sender
     * @return sender
     */
    public String getSender() {
        return sender;
    }
    
    /**
     * Returns a messages timestamp
     * @return timestamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }
    
    /**
     * Returns the chat's id that the message was sent to
     * @return chatid
     */
    public int getChatID() {
        return chatId;
    }
    
    
    public void out() {
        System.out.println("content: " + content + "\nsender: " + sender + "\nchatID: " + chatId);
    }
}
