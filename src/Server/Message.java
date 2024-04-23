package Server;

import java.util.Map;
import java.util.TreeMap;

public class Message {
    User sender;
    User receiver;
    String message;

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public void send() {
        this.receiver.receiveMessage(this);
    }

    @Override
    public String toString() {
        return this.sender + " " + this.receiver + " " + this.message;
    }

    public Map<String, String> toMap() {
        Map<String, String> msgMap = new TreeMap<String, String>();
        msgMap.put("sender", this.sender.getUsername());
        msgMap.put("receiver", this.receiver.getUsername());
        msgMap.put("message", this.message);
        return msgMap;
    }
}
