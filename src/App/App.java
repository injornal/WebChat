package App;

import App.Networking.Client;

public class App {
    private Client client;

    public App(Client client) {
        this.client = client;
        client.start("127.0.0.1", 8080);
        // Initialize Swing
        // Run GUI app
    }

    public static void main(String[] args) {
        App app = new App(new Client());
    }
}
