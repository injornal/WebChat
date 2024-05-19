package Server;

import java.util.LinkedList;
import java.util.Queue;

class User implements Comparable<User>{
    private String username;
    private String password;
    private ServerConnection connection;

    protected User (String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected void receiveMessage(Message message) {
        if (this.connection != null)
            this.connection.receiveMessage(message);
    }

    protected String getPassword() {
        return this.password;
    }

    protected String getUsername() {
        return this.username;
    }

    protected void connect(ServerConnection connection) {
        this.connection = connection;
    }

    protected void disconnect() {
        this.connection = null;
    }

    @Override
    public int compareTo(User user) {
        return this.username.compareTo(user.getUsername());
    }
}
