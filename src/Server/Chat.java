package Server;

import java.util.ArrayList;

public class Chat {
    private final ArrayList<User> users = new ArrayList<User>();
    public ArrayList<User> getUsers() {
        return users;
    }

    public void join(User user) {
        this.users.add(user);
    }
}
