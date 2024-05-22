package App;

import App.Networking.Client;
import GUI.LoginWindow;

public class App {
    private Client client;

    public App(Client client) {
        this.client = client;
        client.start("127.0.0.1", 8080);
        new LoginWindow(client);
    }

    public static void main(String[] args) {
        new App(new Client());
    }
}
