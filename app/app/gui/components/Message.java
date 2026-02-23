package app.gui.components;

/**
 * A message with its content, sender, and chatID
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class Message {
    private final String content;
    private final String sender;
    private final int chatId;

    /**
     * Creates a new message
     * 
     * @param content   content of the message
     * @param sender    sender of the message
     * @param chatId    chat the message will be sent to
     */
    public Message(String content, String sender, int chatId) {
        this.content = content;
        this.sender = sender;
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
     * Returns the chat's id that the message was sent to
     * @return chatid
     */
    public int getChatID() {
        return chatId;
    }
}