package Server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


class ServerConnection implements Runnable {
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final Socket conn;
    private User user;

    ServerConnection(Socket conn) {
        try {
            this.conn = conn;
            this.writer = new PrintWriter(conn.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (conn.isConnected()) {
                String data = this.reader.readLine();
                if (data == null)
                    break;
                System.out.println(data);
                JSONObject response = this.execute(data);
                this.writer.println(response);
                System.out.println(response);
            }
            if (this.user != null) this.user.disconnect();
            this.user = null;
        } catch (Exception e) {
            try {
                conn.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (this.user != null) this.user.disconnect();
            this.user = null;
            throw new RuntimeException(e);
        }
    }

    private JSONObject execute(String data) {
        JSONObject dataJSON = new JSONObject(data);
        JSONObject result = switch (dataJSON.getString("action")) {
            case "SEND" -> this.sendMessage(dataJSON);
            case "SIGN_UP" -> this.signUp(dataJSON);
            case "LOGIN" -> this.login(dataJSON);
            case "CREATE_CHAT" -> this.createChat();
            case "JOIN_CHAT" -> this.joinChat(dataJSON.getInt("chat_id"));
            default -> new JSONObject().put("result", "EXCEPTION").put("message", "WRONG_ACTION");
        };
        return result.put("action", dataJSON.getString("action"));
    }

    private JSONObject createChat() {
        if (isNotLoggedIn()) return new JSONObject().put("result", "ERROR").put("message", "NOT_LOGGED_IN");
        Chat chat = new Chat();
        Server.chats.add(chat);
        this.joinChat(chat);
        int chat_id;
        chat_id = Server.chats.indexOf(chat);
        return new JSONObject().put("result", "SUCCESS").put("chat_id", chat_id);
        // TODO think about the structure to reduce O(N) calls
    }

    private JSONObject joinChat(int chat_id) {
        if (isNotLoggedIn()) return new JSONObject().put("result", "ERROR").put("message", "NOT_LOGGED_IN");
        return this.joinChat(Server.chats.get(chat_id));
    }

    private JSONObject joinChat(Chat chat) {
        if (isNotLoggedIn()) return new JSONObject().put("result", "ERROR").put("message", "NOT_LOGGED_IN");
        if (chat == null)
            return new JSONObject().put("result", "ERROR").put("message", "CHAT_DOES_NOT_EXIST");
        chat.join(this.user);
        return new JSONObject().put("result", "SUCCESS");
    }

    private JSONObject sendMessage(JSONObject data) {
        if (Server.chats.isEmpty()) return new JSONObject().put("result", "ERROR").put("message", "CHAT_DOES_NOT_EXIST");
        Chat chat = Server.chats.get(data.getInt("chat_id"));
        if (chat == null) return new JSONObject().put("result", "ERROR").put("message", "CHAT_DOES_NOT_EXIST");
        if (isNotLoggedIn()) return new JSONObject().put("result", "ERROR").put("message", "NOT_LOGGED_IN");
        Message message = new Message(
                data.getString("content"), this.user.getUsername(), data.getString("time_stamp"),
                data.getInt("chat_id")
        );
        for (User user : chat.getUsers()) {
            if (user != this.user) user.receiveMessage(message);
        }
        return new JSONObject().put("result", "SUCCESS");
    }

    public void receiveMessage(Message message) {
        writer.println(message.toJSON().put("action", "RECEIVE"));
    }

    private JSONObject signUp(JSONObject data) {
        String username = data.getString("username");
        String password = data.getString("password");
        if (Server.users.containsKey(username)) {
            return new JSONObject().put("result", "ERROR").put("message", "USER_ALREADY_EXISTS");
        }
        User user = new User(username, password);
        Server.users.put(username, user);
        return new JSONObject().put("result", "SUCCESS");
    }

    private JSONObject login(JSONObject data) {
        String username = data.getString("username");
        User user;
        if (Server.users.containsKey(username)) {
            user = Server.users.get(username);
        } else {
            return new JSONObject().put("result", "ERROR").put("message", "USER_DOES_NOT_EXIST");
        }
        if (user.getPassword().equals(data.getString("password"))) {
            this.user = user;
            this.user.connect(this);
        } else return new JSONObject().put("result", "ERROR").put("message", "WRONG_PASSWORD");
        return new JSONObject().put("result", "SUCCESS");
    }

    private boolean isNotLoggedIn() {
        return this.user == null;
    }
}