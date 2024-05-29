package server;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

/**
 * Server class
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class Server implements Closeable {
    private boolean running;
    private int port;
    protected static final Map<String, User> users = Collections.synchronizedMap(new TreeMap<>() {
        {
            put("TestUser", new User("TestUser", "TestPassword"));
        }
    });
    protected static final List<Chat> chats = Collections.synchronizedList(new ArrayList<>());
    private final ArrayList<ServerConnection> connections = new ArrayList<>();

    
    private void introduce() {
        System.out.println("\u001B[31m" + "Server.WebChat server");
        System.out.println("Running on " + this.port + "\u001B[0m");
    }

    /**
     * starts server with a port
     * @param port port
     */
    public void start(int port) {
        this.port = port;
        this.introduce();
        this.running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (running) {
                this.connections.add(new ServerConnection(serverSocket.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
    public static void main(String[] args) {
        try (Server server = new Server()) {
            server.start(8080);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the servers connections
     */
    @Override
    public void close() throws IOException {
        for (ServerConnection connection : connections) {
            connection.close();
        }
    }
}
