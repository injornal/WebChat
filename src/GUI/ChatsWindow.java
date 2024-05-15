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
import GUI.Components.Chat;
import GUI.Components.Person;

public class ChatsWindow extends JFrame {
    private JPanel panel;
    private App.Networking.Client client;
    private Chat[] chats;
    private JButton[] JButtons;

    public ChatsWindow(Client client, Chat[] chats) {
        this.client = client;
        client.start("127.0.0.1", 8080);
        this.chats = chats;

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(2,4));
        setMinimumSize(new Dimension(600, 300));
        setMaximumSize(new Dimension(600, 300));
        JButton[] JButtons = new JButton[8];
        for(int i = 0; i < chats.length; i++){
            if (chats[i].exists()) {
                JButtons[i] = new JButton("Chat " + (i + 1));
            }
            else {
                JButtons[i] = new JButton("New Chat");
            }
            
            JButtons[i].addActionListener(new ClickChat(this, i));
            panel.add(JButtons[i]);
        }

        add(panel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WebChat");
        pack();
        setVisible(true);

        client = new Client();
        client.start("127.0.0.1", 8080);
    }

    private Chat[] getChats(){
        return chats;
    }

    private JButton[] getJButtons(){
        return JButtons;   
    }

    private class ClickChat implements ActionListener {
        private ChatsWindow frame;
        private int index;
        public ClickChat(ChatsWindow frame, int index) {
            this.frame = frame;
            this.index = index;
        }
        public void actionPerformed(ActionEvent e) {
            if(!chats[index].exists()){
                JButtons[index] = new JButton("Chat " + (index + 1));
                System.out.println("doesnt exist");
                //     success -> open chat, close this window
            }
            else {
                System.out.println("exists");
                // option 2 (if chat occupies tile): opens chat, close this window
            }
        }
    }
    public static void main(String[] args) {
        Client client = new Client();
        Chat[] chats = new Chat[8];
        chats[0] = new Chat(0);
        chats[0].joinChat(new Person(null, false));
        chats[1] = new Chat(1);
        chats[2] = new Chat(2);
        chats[2].joinChat(new Person(null, false));
        chats[3] = new Chat(3);
        chats[4] = new Chat(4);
        chats[5] = new Chat(5);
        chats[5].joinChat(new Person(null, false));
        chats[6] = new Chat(6);
        chats[7] = new Chat(7);

        new ChatsWindow(client, chats);
    }
}
