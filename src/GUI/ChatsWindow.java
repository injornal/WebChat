package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONArray;

import javax.swing.JPasswordField;

import App.Networking.Client;
import GUI.Components.Chat;
import GUI.Components.Person;
import Server.Message;

public class ChatsWindow extends JFrame implements Serializable {
    private JPanel panel;
    private App.Networking.Client client;
    private JButton[] JButtons;
    private ChatDisplay[] displays;
    private Person person;

    public ChatsWindow(Client client, Person person) {
        this.client = client;
        this.person = person;

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(2,4));
        setMinimumSize(new Dimension(600, 300));
        setMaximumSize(new Dimension(600, 300));
        JButtons = new JButton[7];
        for (int i = 0; i < 7; i++) {
            if (person.exists(i)) {
                JButtons[i] = new JButton("" + person.getChatIds()[i]);
            }
            else {
                JButtons[i] = new JButton("New Chat");
            }
            JButtons[i].addActionListener(new ClickChat(this, i));
            panel.add(JButtons[i]);
        }

        JButton logout = new JButton("Logout");
        logout.addActionListener(new Logout(this));
        panel.add(logout);

        add(panel, BorderLayout.CENTER);
        setTitle("WebChat");
        pack();
        setVisible(true);
    }

    private JButton[] getJButtons() {
        return JButtons;   
    }
    private Person getPerson() {
        return person;
    }
    
    private class ClickChat implements ActionListener {
        private ChatsWindow frame;
        private int index;
        public ClickChat(ChatsWindow frame, int index) {
            this.frame = frame;
            this.index = index;
        }
        public void actionPerformed(ActionEvent e) {
            if (!frame.getPerson().exists(index)) {
                frame.setVisible(false);
                JButtons[index] = new JButton("Chat " + (index + 1));

                Chat chat = new Chat(1);
                displays[index] = new ChatDisplay(client, chat, person, frame);
            }
            else {
                System.out.println("exists");
                frame.setVisible(false);
            }
        }
    }
    private class Logout implements ActionListener {
        private ChatsWindow frame;
        public Logout(ChatsWindow frame) {
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            System.out.println("logged out");
            //go into save state
            //status logged out
        }
    }
}
