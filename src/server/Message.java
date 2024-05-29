package server;

import org.json.JSONObject;

public class Message {
    private final String content;
    private final String sender;
    private final String timeStamp;
    private final int chatId;

    /**
     * Makes a new message with its content, sender, timestamp, and chat id
     * @param content content of the message
     * @param sender sender of the message
     * @param timeStamp time at which the message was sent
     * @param chatId id of the chat that the message was sent to
     */
    public Message(String content, String sender, String timeStamp, int chatId) {
        this.content = content;
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.chatId = chatId;
    }

    /**
     * Makes a JSONObject with messsages info
     * @return JSONObject with matching info
     */
    public JSONObject toJSON() {
        return new JSONObject() {{
            put("content", content);
            put("sender", sender);
            put("time_stamp", timeStamp);
            put("chat_id", chatId);
        }};
    }
}
