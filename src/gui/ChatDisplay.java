package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import gui.components.Message;
import app.networking.Client;
import gui.components.Chat;
import gui.components.Person;

/**
 * Displays the chat with messages and users
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class ChatDisplay extends JFrame {
    /**
     * 
     */
    private JPanel panel;
    /**
     * 
     */
    private JPanel interactivePanel;
    /**
     * 
     */
    private app.networking.Client client;
    /**
     * 
     */
    private Chat chat;
    /**
     * 
     */
    private ArrayList<Message> messages;
    /**
     * 
     */
    private Person person;
    /**
     * 
     */
    private TextArea existingMsgs;
    /**
     * 
     */
    private TextArea newMsg;
    /**
     * 
     */
    private ChatsWindow window;
    /**
     * 
     */
    private final String halfwaySpace = "                                                                        ";

    /**
     * Creates a new display with a chat
     * 
     * @param client client
     * @param chat   chat that is being displayed
     * @param person Person in the chat
     * @param window window
     */
    public ChatDisplay(Client client, Chat chat, ChatsWindow window, Person person) {
        this.window = window;
        this.client = client;
        this.chat = chat;
        this.messages = chat.getMessages();
        this.person = person;
        setLayout(new GridLayout(2, 1));

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(1, 1));
        panel.setPreferredSize(new Dimension(500, 800));
        panel.setMinimumSize(new Dimension(500, 800));
        panel.setMaximumSize(new Dimension(500, 800));

        TextArea existingMsg = new TextArea(parseMessages());
        existingMsg.setEditable(false);
        // existingMsg.setPreferredSize(new Dimension(600, 800));
        // existingMsg.setMinimumSize(new Dimension(600, 800));
        // existingMsg.setMaximumSize(new Dimension(600, 800));
        panel.add(existingMsg);
        this.existingMsgs = existingMsg;

        interactivePanel = new JPanel();
        // interactivePanel.setBorder(BorderFactory.createEmptyBorder(10,30,30,30));
        interactivePanel.setLayout(new GridLayout(1, 2));
        // interactivePanel.setMinimumSize(new Dimension(600, 183));
        // interactivePanel.setMaximumSize(new Dimension(600, 183));

        newMsg = new TextArea();
        // newMsg.setPreferredSize(new Dimension(600, 600));
        // newMsg.setMinimumSize(new Dimension(600, 183));
        // newMsg.setMaximumSize(new Dimension(600, 183));
        interactivePanel.add(newMsg);
        newMsg.addKeyListener(new Send(this));

        JPanel buttons = new JPanel();
        interactivePanel.setBorder(BorderFactory.createEmptyBorder(10,30,30,30));
        interactivePanel.setLayout(new GridLayout(2,1));
        //interactivePanel.setMinimumSize(new Dimension(600, 183))
        //interactivePanel.setMinimumSize(new Dimension(600, 183));
        //interactivePanel.setMaximumSize(new Dimension(600, 183));
        interactivePanel.add(buttons);

        JButton quit = new JButton("Quit");
        quit.addActionListener(new Quit(this));
        buttons.add(quit);

        setMinimumSize(new Dimension(600, 983));
        setMaximumSize(new Dimension(600, 983));
        add(panel, BorderLayout.NORTH);
        add(interactivePanel, BorderLayout.SOUTH);
        setTitle("Chat ID: " + chat.getChatID());
        pack();
        setVisible(true);
    }

    private class Send implements KeyListener {
        private ChatDisplay frame;
        private TextArea newMsg;

        public Send(ChatDisplay frame) {
            this.frame = frame;
            newMsg = frame.getNewMSG();
        }
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10 && !newMsg.getText().equals("") && !newMsg.getText().contains("\n")) {
                client.addSendMessageOnResponseCallback((a) -> {});
                client.sendMessage(newMsg.getText(), "", chat.getChatID());
                getExistingMsgs().setText(getExistingMsgs().getText() + frame.addMessage(
                    new Message(newMsg.getText(), person.getName(), "", frame.getChat().getChatID())
                ));
                frame.getNewMSG().setText("");
                System.out.println("sent");
            }
            if (e.getKeyCode() == 27) {
                quit(frame);
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyCode() == 10 || frame.getNewMSG().getText().contains("\n")) {
                frame.getNewMSG().setText("");
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 10 || frame.getNewMSG().getText().contains("\n")) {
                frame.getNewMSG().setText("");
            }
        }
    }

    private class Quit implements ActionListener {
        private ChatDisplay frame;

        public Quit(ChatDisplay frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            quit(frame);
        }
    }
    private void quit(ChatDisplay frame) {
        System.out.println("quit out of chat");
        frame.setVisible(false);
        frame.getWindow().setVisible(true);
        frame.getNewMSG().setText("");
    }

    private ChatsWindow getWindow() {
        return window;
    }

    private TextArea getExistingMsgs() {
        return existingMsgs;
    }

    private Chat getChat() {
        return chat;
    }
    public void setChat(Chat chat) {
        this.chat = chat;
    }
    private TextArea getNewMSG() {
        return newMsg;
    }
   
    public String addMessage(Message m) {
        int i = messages.indexOf(m);
        if (i == -1) {
            messages.add(m);
            i = messages.size() - 1;
        }
        String result = "";
        Message prevMessage = new Message("", "", "", -1);
        String prevSender = "";
        if (i != 0) {
            prevMessage = messages.get(i - 1);
            prevSender = prevMessage.getSender();
        }
        Message currMessage = messages.get(i);
        String content = currMessage.getContent();
        String sender = currMessage.getSender();

        if (!person.getName().equals(sender)) {
            if (!prevSender.equals(sender)) {
                result += (sender + "\n");
            }
            if (content.length() < 26) {
                result += (content + "\n");
            } else {
                result += splitMessage(content, "") + "\n";
            }
        } else {
            if (content.length() < 22) {
                result += (halfwaySpace + content + "\n");
            } else {
                result += splitMessage(content, halfwaySpace) + "\n";
            }
        }
        return result;
    }
    private String parseMessages() {
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
                        lineLength += 22;
                    }
                }
                result += "\n" + spaceBefore + word;
            }
        }
        return result;
    }
}