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
import Server.Message;

public class ChatsWindow extends JFrame {
    private JPanel panel;
    private App.Networking.Client client;
    private Chat[] chats;
    private JButton[] JButtons;
    private ChatDisplay[] displays;
    private Person person;

    public ChatsWindow(Client client, Chat[] chats, Person person) {
        this.client = client;
        client.start("127.0.0.1", 8080);
        this.chats = chats;
        this.person = person;

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(2,4));
        setMinimumSize(new Dimension(600, 300));
        setMaximumSize(new Dimension(600, 300));
        JButtons = new JButton[7];
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
            if (!chats[index].exists()) {
                frame.setVisible(false);
                JButtons[index] = new JButton("Chat " + (index + 1));

                Chat chat = new Chat(1);                        // generate chatID
                Person person = new Person("chai", true);  // figure out how to add person in LoginWindow
                displays[index] = new ChatDisplay(client, chat, person, frame);



                //     success -> open chat, close this window
            }
            else {
                System.out.println("exists");
                frame.setVisible(false);
                // option 2 (if chat occupies tile): opens chat, close this window
            }
        }
    }
}
