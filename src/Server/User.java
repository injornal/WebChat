package server;

import java.util.LinkedList;
import java.util.Queue;

/** 
 * A user with a username and password
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
class User implements Comparable<User>{
    /**
     * 
     */
    private String username;
    /**
     * 
     */
    private String password;
    /**
     * 
     */
    private ServerConnection connection;
    /**
     * 
     */
    private Queue<Message> queuedMessages = new LinkedList<>();


    /**
     * @param username username
     * @param password password
     */
    protected User (String username, String password) {
        this.username = username;
        this.password = password;
    }

    /** 
     * @param message message
     */
    protected void receiveMessage(Message message) {
        if (this.connection != null)
            this.connection.receiveMessage(message);
        else {
            this.queuedMessages.add(message);
        }
    }

    /** 
     * @return pwd
     */
    protected String getPassword() {
        return this.password;
    }

    /** 
     * @return pwd
     */
    protected String getUsername() {
        return this.username;
    }

    /** 
     * @param connection Sets the servers connection
     */
    protected void connect(ServerConnection connection) {
        this.connection = connection;
    }

    /** 
     * 
     */
    protected void disconnect() {
        this.connection = null;
    }

    /** 
     * @return Queue of Messages
     */
    protected Queue<Message> getQueuedMessages() {
        Queue<Message> messages = this.queuedMessages;
        this.queuedMessages = new LinkedList<>();
        return messages;
    }

    @Override
    /** 
     * @param user other user that the user being compared to
     * @return int
     */
    public int compareTo(User user) {
        return this.username.compareTo(user.getUsername());
    }
}
