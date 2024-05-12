package Server;

import java.util.TreeSet;

class Chat {
    private final TreeSet<User> users;

    protected Chat() {
        this.users = new TreeSet<>();
    }

    public TreeSet<User> getUsers() {
        return users;
    }

    synchronized public void join(User user) {
        this.users.add(user);
    }
}
