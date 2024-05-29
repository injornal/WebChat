package app;

import app.networking.Client;
import gui.LoginWindow;

/**
 * Class to start the program
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class App {
    /**
     * Starts the client
     * @param client client
     */
    public App(Client client) {
        client.start("127.0.0.1", 8080);
        new LoginWindow(client);
    }

    public static void main(String[] args) {
        new App(new Client());
    }
}