import java.util.*;


class Chat {
    private final Set<User> users = Collections.synchronizedSet(new HashSet<>());
    private final List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    
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
