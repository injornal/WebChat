package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;

import org.json.JSONObject;
import org.w3c.dom.Text;

import javax.swing.JPasswordField;
import App.Networking.Client;
import GUI.Components.Chat;
import GUI.Components.Person;
import Server.Message;

public class Test extends JFrame {
    private JPanel panel;
    private JPanel interactivePanel;
    private App.Networking.Client client;
    private Chat chat;
    private ArrayList<Message> messages;
    private Person person;
    private final String halfwaySpace = "                                                                        ";

    public Test(Client client, Chat chat, Person person) {
        this.client = client;
        client.start("127.0.0.1", 8080);
        this.chat = chat;
        this.messages = chat.getMessages();
        this.person = person;
        setLayout(new GridLayout(2, 1));

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(1,1));
        panel.setPreferredSize(new Dimension(600, 800));
        //panel.setMinimumSize(new Dimension(600, 800));
        //panel.setMaximumSize(new Dimension(600, 800));

        /*
        TextArea messages = new TextArea(parseMessages());
        messages.setPreferredSize(new Dimension(600, 800));
        messages.setEditable(false);
        messages.setMinimumSize(new Dimension(600, 800));
        messages.setMaximumSize(new Dimension(600, 800));
        panel.add(messages);

        interactivePanel = new JPanel();
        interactivePanel.setBorder(BorderFactory.createEmptyBorder(10,30,30,30));
        interactivePanel.setLayout(new GridLayout(1,2));
        interactivePanel.setMinimumSize(new Dimension(600, 183));
        interactivePanel.setMaximumSize(new Dimension(600, 183));

        TextArea newMsg = new TextArea();
        newMsg.setPreferredSize(new Dimension(600, 600));
        newMsg.setEditable(true);
        messages.setMinimumSize(new Dimension(600, 183));
        messages.setMaximumSize(new Dimension(600, 183));
        interactivePanel.add(newMsg);

*/

        Box b1 = Box.createHorizontalBox();
        b1.add(Box.createHorizontalStrut(10));
        b1.add(Box.createHorizontalStrut(10));
        b1.add(panel);

        setMinimumSize(new Dimension(600, 983));
        setMaximumSize(new Dimension(600, 983));
        //add(panel, BorderLayout.NORTH);
        //add(interactivePanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("" + chat.getChatID());
        pack();
        setVisible(true);
    }

    public String parseMessages() {
        String result = "";
        for (int i = 0; i < messages.size(); i++) {
            result += addMessage(messages.get(i));
        }
        return result;
    }
    private String splitMessage(String s, String spaceBefore) {
        String[] words = s.split(" ");    //empty string for space
        String result = spaceBefore;
        int lineLength = 0;
        for (String word : words) {
            if (word.length() < 21) {
                if (lineLength + word.length() < 21) {
                    lineLength += word.length() + 1;
                    result += (word + " ");
                }
                else {
                    lineLength = word.length() + 1;
                    result += ("\n" + spaceBefore + word + " ");
                }
            }
            else {
                while (word.length() > 20) {
                    String subWord = word.substring(0, 21);
                    word = word.substring(21);
                    if (lineLength > 0) {
                        result += "\n" + spaceBefore + subWord;
                    }
                    else {
                        result += subWord;
                    }
                }
                result += "\n" + spaceBefore + word;
            }
        }
        return result;
    }
    public String addMessage(Message m) {
        int i = messages.indexOf(m);
        if (i == -1) {
            messages.add(m);
            i = messages.size()-1;
        }
        String result = "";
        Message prevMessage = new Message("", "", "", -1);
        String prevSender = "";
        if (i != 0) {
            prevMessage = messages.get(i-1);
            prevSender = prevMessage.toJSON().getString("sender");
        }
        Message currMessage = messages.get(i);
        String content = currMessage.toJSON().getString("content");
        String sender = currMessage.toJSON().getString("sender");

        if (!person.getName().equals(sender)) {
            if (!prevSender.equals(sender)) {
                result += (sender + "\n");
            }
            if (content.length() < 26) {
                result += (content + "\n"); 
            }
            else {
                result += splitMessage(content, "") + "\n";
            }
        }
        else {
            if (content.length() < 22) {
                result += (halfwaySpace + content + "\n"); 
            }
            else {
                result += splitMessage(content, halfwaySpace) + "\n";
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
        Chat chat = new Chat(1);
        chat.receiveMessage(
            new Message("hi1", "chai", null, ABORT)
        );
        chat.receiveMessage(
            new Message("hiiiiiiiiiiiiiiiiiiiiiiiiiii2", "sanjana", null, ABORT)
        );
        chat.receiveMessage(
            new Message("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii3", "chai", null, ABORT)
        );
        chat.receiveMessage(
            new Message("hi iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii4", "sanjana", null, ABORT)
        );
        chat.receiveMessage(
            new Message("hi iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii5", "chai", null, ABORT)
        );
        chat.receiveMessage(
            new Message("hi iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii6", "chai", null, ABORT)
        );
        chat.receiveMessage(
            new Message("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii hiiiii7", "sanjana", null, ABORT)
        );
        
//        Person person = new Person("chai", true);
//        new ChatDisplay(new Client(), chat, person);
    }
}
