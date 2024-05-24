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
class Message {
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
     * @param chatId    which chat the message will be sent to
     */
    public Message(String content, String sender, String timeStamp, int chatId) {
        this.content = content;
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.chatId = chatId;
    }

    /**
     * Creates a JSON object with the same info
     * 
     * @return JSON Object with messages info
     */
    public Message fromJSON(JSONObject obj) {
        return new Message(obj.getString("content"),obj.getString("sender"),obj.getString("time_stamp"),obj.getInt("chat_id"));
    }
}