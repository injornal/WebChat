package Server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Protocol
 * 1. SIGN_UP username password
 * 2. LOGIN username password
 * 3. JOIN_CHAT chat_id
 * 4. CREATE_CHAT
 *  return chat_id
 *  joins the chat automatically
 * 5. SEND content sender time_stamp chat_id
 */
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
                this.execute(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void execute(String data) {
        JSONObject dataJSON = new JSONObject(data);
        switch (dataJSON.getString("action")) {
            case "SEND":
                this.sendMessage(new Message(dataJSON.getString("content"), dataJSON.getString("sender"),
                        dataJSON.getString("time_stamp")), Server.chats.get(dataJSON.getInt("chat_id")));
                break;
            case "SIGN_UP":
                this.signUp(dataJSON);
                break;
            case "LOGIN":
                this.login(dataJSON);
                break;
            case "CREATE_CHAT":
                this.createChat();
                break;
            case "JOIN_CHAT":
                this.joinChat(dataJSON.getInt("chat_id"));
                break;
        }
    }

    private void createChat() {
        Chat chat = new Chat();
        Server.chats.add(chat);
        this.joinChat(chat);
        writer.println(new JSONObject().put("chat_id", Server.chats.indexOf(chat))); // TODO think about the structure to reduce O(N) calls
    }

    private void joinChat(int chat_id) {
        this.joinChat(Server.chats.get(chat_id));
    }

    private void joinChat(Chat chat) {
        chat.join(this.user);
    }

    private void sendMessage(Message message, Chat chat) {
        for (User user : chat.getUsers()) {
            user.receiveMessage(message, chat);
        }
    }

    public void receiveMessage(Message message, Chat chat) { // TODO think about the multithreading part
        writer.println(message.toJSON().put("chat_id", Server.chats.indexOf(chat)));
    }

    private void signUp(JSONObject data) {
        String username = data.getString("username");
        String password = data.getString("password");
        if (Server.users.containsKey(username)) {
            writer.println("ERROR USER_ALREADY_EXISTS");
            return;
        }
        User user = new User(username, password);
        Server.users.put(username, user);
    }

    private void login(JSONObject data) {
        String username = data.getString("username");
        if (Server.users.containsKey(username)) {
            User user = Server.users.get(username);
            if (user.getPassword().equals(data.getString("password"))) {
                this.user = user;
                this.user.connect(this);
            } else writer.println("ERROR WRONG_PASSWORD");
        }
        else writer.println("ERROR USER_DOES_NOT_EXIST");
    }
}
