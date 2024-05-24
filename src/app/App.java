package app;

import app.networking.Client;

/**
 * App class to start the program
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class App {
    private Client client;

    /**
     * Starts the client
     * 
     * @param client client
     */
    public App(Client client) {
        this.client = client;
        client.start("127.0.0.1", 8080);
        new LoginWindow(client);
    }

    /**
     * main 
     * @param args args
     */
    public static void main(String[] args) {
        new App(new Client());
    }
}