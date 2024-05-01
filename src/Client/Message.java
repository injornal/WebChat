package Client;


import org.json.JSONObject;

public class Message {
    private final String content;
    private final String sender;

    protected Message(String message, String sender) {
        this.content = message;
        this.sender = sender;
    }

    public JSONObject toJSON() {
        return new JSONObject().put("sender", this.sender).put("content", this.content);
    }
}
