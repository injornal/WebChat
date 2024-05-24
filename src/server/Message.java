package server;

import org.json.JSONObject;

public class Message {
    private final String content;
    private final String sender;
    private final String timeStamp;
    private final int chatId;

    public Message(String content, String sender, String timeStamp, int chatId) {
        this.content = content;
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.chatId = chatId;
    }

    public JSONObject toJSON() {
        return new JSONObject() {{
            put("content", content);
            put("sender", sender);
            put("time_stamp", timeStamp);
            put("chat_id", chatId);
        }};
    }
}
