package Server;

import java.util.ArrayList;
import java.util.TreeSet;

class Chat {
    private final TreeSet<User> users = new TreeSet<User>();
    public TreeSet<User> getUsers() {
        return users;
    }

    public void join(User user) {
        this.users.add(user);
    }

    public boolean containsUser(User user) {
        return this.users.contains(user);
    }
}
