package Message;

public class Message {
    
    String msg;
    int timeStamp;
    Person sender;
    int chatID;

    public Message(String text, int timeStamp, Person sender, int chatID) {
        this.msg = text;
        this.timeStamp = timeStamp;
        this.sender = sender;
        this.chatID = chatID;
    }
}
