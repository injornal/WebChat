package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A server for WebChat. To run, call start().
 * Protocol:
 * API is implemented using JSON objects
 * Each request must contain fields "action", "token", and "parameters"
 * with a sub-map of parameters
 * Possible actions:
 * 1. SIGN_UP (token should be empty)
 * parameters: username, password
 * creates a user on the server
 * 2. LOGIN (token should be empty)
 * parameters: username, password
 * returns a string token which shall be used until LOGOUT is called
 * 3. LOGOUT
 * resets the token, closes the connection
 * 4. GET_ALL
 * returns a JSONArray of all messages
 * see message signature in the Message class header
 * 5. SEND
 * parameters: receiver, message
 */
public class Server {
    private boolean running;
    private int port;
    private ArrayList<ServerConnection> serverConnections;
    private ExecutorService service;
    public static TreeMap<String, User> users = new TreeMap<>();

    public Server() {
        this.serverConnections = new ArrayList<>();
        service = Executors.newCachedThreadPool();
    }

    private void introduce() {
        System.out.println("\u001B[31m" + "Server.WebChat server");
        System.out.println("Running on " + this.port + "\u001B[0m");
    }

    public void start(int port) throws java.io.IOException {
        this.port = port;
        this.introduce();
        this.running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (running) {
                Socket conn = serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(conn);
                serverConnections.add(serverConnection);
                service.submit(serverConnection);
            }
        }
    }

    public void stop() {
        this.running = false;
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(8080);
        } catch (IOException e) {
            server.stop();
            System.out.println(e.getMessage());
        }
    }
}
