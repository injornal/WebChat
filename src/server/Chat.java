package server;

import java.util.*;


class Chat {
    private final Set<User> users = new HashSet<>();
    private final List<Message> messages = new ArrayList<>();

    
    protected Set<User> getUsers() {
        return users;
    }

    
    protected void joinUser(User user) {
        this.users.add(user);
    }

    
    protected boolean containsUser(User user) {
        return this.users.contains(user);
    }

    
    protected void receiveMessage(Message message) {
        this.messages.add(message);
    }

    protected List<Message> getMessages() {
        return this.messages;
    }
}
