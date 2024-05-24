package server;

import java.util.*;

/**
 * Chat with a list of users and messages
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
class Chat {
    private final Set<User> users = new HashSet<>();
    private final List<Message> messages = new ArrayList<>();

    /**
     * get users
     * @return set
     */
    protected Set<User> getUsers() {
        return users;
    }

    /**
     * join user
     * @param user user
     */
    protected void joinUser(User user) {
        this.users.add(user);
    }

    /**
     * contains
     * @param user user
     * @return true or false
     */
    protected boolean containsUser(User user) {
        return this.users.contains(user);
    }

    /**
     * receive messages
     * @param message message
     */
    protected void receiveMessage(Message message) {
        this.messages.add(message);
    }

    /**
     * get messages
     * @return messsages
     */
    protected List<Message> getMessages() {
        return this.messages;
    }
}
