package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import Server.Components.Chat;


class Server {
    private boolean running;
    private int port;
    protected static final Map<String, User> users = Collections.synchronizedMap(new TreeMap<>());
    protected static final List<Chat> chats = Collections.synchronizedList(new ArrayList<>());


    private void introduce() {
        System.out.println("\u001B[31m" + "Server.WebChat server");
        System.out.println("Running on " + this.port + "\u001B[0m");
    }

    public void start(int port) {
        this.port = port;
        this.introduce();
        this.running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (running) {
                Socket conn = serverSocket.accept();
                Thread thread = new Thread(new ServerConnection(conn));
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(8080);
    }
}
