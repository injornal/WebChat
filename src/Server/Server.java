package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        introduce();
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
