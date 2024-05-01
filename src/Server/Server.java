package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class Server {
    private boolean running;
    private int port;
    protected static TreeMap<String, User> users = new TreeMap<>();
    private final ExecutorService service;
    protected static ArrayList<Chat> chats = new ArrayList<>();

    Server() {
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
                service.submit(new ServerConnection(conn));
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
            System.out.println("\u001B[31m" + e.getMessage());
        }
    }
}
