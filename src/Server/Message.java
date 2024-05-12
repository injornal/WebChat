package Server;

import org.json.JSONObject;

class Message {
    private final String content;
    private final String sender;
    private final String timeStamp;
    private final int chatId;

    protected Message(String content, String sender, String timeStamp, int chatId) {
        this.content = content;
        this.sender = sender;
        this.timeStamp = timeStamp;
        this.chatId = chatId;
    }

    public JSONObject toJSON() {
        return new JSONObject().put("content", content).put("sender", sender).put("time_stamp", this.timeStamp).put("chat_id", chatId);
    }
}
