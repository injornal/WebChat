package Server;

import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ServerConnection implements Runnable {
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket conn;
    private User user;

    ServerConnection(Socket conn) throws java.io.IOException{
        this.conn = conn;
        this.writer = new PrintWriter(conn.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (conn.isConnected()) {
                String data = this.reader.readLine();
                if (data == null)
                    break;
                System.out.println(data);
                writer.println(this.respond(data));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String respond(String req) {
        Request request = new Request(req);
        return switch (request.type) {
            case LOGIN ->
                    this.login(request.getData()) ? "SUCCESS" : "ERROR WRONG_USERNAME_OR_PASSWORD";
            case SIGNUP ->
                this.signUp(request.getData()) ? "SUCCESS" : "ERROR USER_DOES_NOT_EXIST";
            case LOGOUT -> this.isLoggedIn() ? this.logout() ? "SUCCESS" : "ERROR" : "ERROR NOT_LOGGED_IN";
            case GET_ALL -> this.isLoggedIn() ? this.getAllMessages().toString() : "ERROR NOT_LOGGED_IN";
            case SEND -> this.isLoggedIn() ? this.sendMessage(request.getData()) ? "SUCCESS" : "ERROR" : "ERROR NOT_LOGGED_IN";
        };
    }

    private JSONArray getAllMessages() {
        return this.user.getAllMessages();
    }

    private boolean sendMessage(String[] data) {
        if (!Server.users.containsKey(data[1])) return false;
        StringBuilder messageText = new StringBuilder();
        for (int i = 2; i < data.length; i++) {
            messageText.append(data[i]).append(" ");
        }
        if (!messageText.isEmpty()) {
            messageText.deleteCharAt(messageText.length() - 1);
        }
        Message message = new Message(this.user, Server.users.get(data[1]), messageText.toString());
        message.send();
        return true;
    }

    private boolean login(String[] data) {
        if (isLoggedIn()) return true;
        if (Server.users.containsKey(data[1])) {
            if (Server.users.get(data[1]).login(data[2])) {
                this.user = Server.users.get(data[1]);
                return true;
            }
        }
        return false;
    }

    private boolean signUp(String[] data) {
        if (!Server.users.containsKey(data[1])) {
            Server.users.put(data[1], new User(data[1], data[2]));
            return true;
        }
        return false;
    }

    private boolean isLoggedIn() {
        return this.user != null;
    }

    private boolean logout() {
        this.user = null;
        return true;
    }
}
