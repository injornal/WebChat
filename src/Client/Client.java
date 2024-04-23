package Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private PrintWriter writer;
    private BufferedReader reader;

    protected void start(String ip, int port) {
        try (Socket socket = new Socket(ip, port)) {
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    protected ArrayList<Chat> getChats () {
        return null; // TODO getChats
    }

    public static void main(String[] args) throws java.io.IOException{
        Client.test();
    }

    public static void test() throws java.io.IOException {
        try (Socket socket = new Socket("127.0.0.1", 8080)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("SIGNUP Kostiantyn 1234");
            System.out.println(reader.readLine());
            writer.println("LOGIN Kostiantyn 1234");
            System.out.println(reader.readLine());
            try (Socket socketLeonid = new Socket("127.0.0.1", 8080)) {
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(socketLeonid.getInputStream()));
                PrintWriter writer1 = new PrintWriter(socketLeonid.getOutputStream(), true);
                writer1.println("SIGNUP Ivan 1212");
                System.out.println(reader1.readLine());
                writer1.println("LOGIN Ivan 1212");
                System.out.println(reader1.readLine());
                writer1.println("GET_ALL");
                System.out.println(reader1.readLine());
                writer.println("SEND Ivan Hello there");
                System.out.println(reader.readLine());
                writer1.println("GET_ALL");
                System.out.println(reader1.readLine());
            }
        }
    }
}
