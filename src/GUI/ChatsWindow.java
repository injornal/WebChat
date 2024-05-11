package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import App.Networking.Client;

public class ChatsWindow extends JFrame {
    private JPanel panel;
    private App.Networking.Client client;
    //private Chat[] chats;

    public ChatsWindow(Client client) {
        this.client = client;
        client.start("127.0.0.1", 8080);

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(2,4));
        setMinimumSize(new Dimension(600, 300));
        setMaximumSize(new Dimension(600, 300));
        JButton[] chats = new JButton[8];
        int increment = 1;
        for (JButton chat : chats) {
            // if (chats[increment-1].exists) {
            //     chat = new JButton("Chat" + increment);
            // }
            // else {
            //     chat = new JButton("New Chat");
            // }
            increment++;
            chat.addActionListener(new ClickChat(this, increment-1));
            panel.add(chat);
        }

        add(panel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WebChat");
        pack();
        setVisible(true);

        client = new Client();
        client.start("127.0.0.1", 8080);
    }

    private class ClickChat implements ActionListener {
        private ChatsWindow frame;
        private int index;
        public ClickChat(ChatsWindow frame, int index) {
            this.frame = frame;
            this.index = index;
        }
        public void actionPerformed(ActionEvent e) {
            // option 1 (if chat doesnt occupy tile): create chat
            //   popup to enter list of people
            //   check if users exist, if user has less than 8 chats,
            //     fail -> add popup to panel
            //     fail -> delete addChat popup
            //     success -> update icon
            //     success -> open chat, close this window
            // option 2 (if chat occupies tile): opens chat, close this window
        }
    }
    public static void main(String[] args) {
        Client client = new Client();
        new ChatsWindow(client);
    }
}
