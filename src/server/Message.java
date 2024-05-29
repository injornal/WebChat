package server;

import org.json.JSONObject;

class Message {
    private final String content;
    private final String sender;
    private final int chatId;

    /**
     * Makes a new message with its content, sender, and chat id
     * @param content content of the message
     * @param sender sender of the message
     * @param chatId id of the chat that the message was sent to
     */
    public Message(String content, String sender, int chatId) {
        this.content = content;
        this.sender = sender;
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
            put("chat_id", chatId);
        }};
    }
}
