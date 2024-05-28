package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private ArrayList<Message> messages = new ArrayList<Message>();
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
    private AffineTransform affinetransform = new AffineTransform();     
    private FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
    private Font font;
    private final String HALFWAY_SPACE = "                                                                        ";
    private final double LINE_LENGTH;

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
        this.person = person;
        setLayout(new GridLayout(2, 1));

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(1, 1));
        panel.setPreferredSize(new Dimension(500, 800));
        panel.setMinimumSize(new Dimension(500, 800));
        panel.setMaximumSize(new Dimension(500, 800));
        panel.addKeyListener(new Tab(this));
        panel.addKeyListener(new Quit(this));

        TextArea existingMsg = new TextArea();
        existingMsg.setEditable(false);
        // existingMsg.setPreferredSize(new Dimension(600, 800));
        // existingMsg.setMinimumSize(new Dimension(600, 800));
        // existingMsg.setMaximumSize(new Dimension(600, 800));
        panel.add(existingMsg);
        this.existingMsgs = existingMsg;
        font = existingMsg.getFont();
        LINE_LENGTH = width(21);

        interactivePanel = new JPanel();
        // interactivePanel.setBorder(BorderFactory.createEmptyBorder(10,30,30,30));
        interactivePanel.setLayout(new GridLayout(1, 2));
        interactivePanel.addKeyListener(new Tab(this));
        interactivePanel.addKeyListener(new Quit(this));
        // interactivePanel.setMinimumSize(new Dimension(600, 183));
        // interactivePanel.setMaximumSize(new Dimension(600, 183));

        newMsg = new TextArea();
        // newMsg.setPreferredSize(new Dimension(600, 600));
        // newMsg.setMinimumSize(new Dimension(600, 183));
        // newMsg.setMaximumSize(new Dimension(600, 183));
        interactivePanel.add(newMsg);
        newMsg.addKeyListener(new Send(this));
        newMsg.addKeyListener(new Tab(this));
        newMsg.addKeyListener(new Quit(this));

        JPanel buttons = new JPanel();
        interactivePanel.setBorder(BorderFactory.createEmptyBorder(10,30,30,30));
        interactivePanel.setLayout(new GridLayout(2,1));
        //interactivePanel.setMinimumSize(new Dimension(600, 183))
        //interactivePanel.setMinimumSize(new Dimension(600, 183));
        //interactivePanel.setMaximumSize(new Dimension(600, 183));
        interactivePanel.add(buttons);

        JButton quit = new JButton("Quit");
        quit.addActionListener(new QuitButton(this));
        buttons.add(quit);

        setMinimumSize(new Dimension(600, 983));
        setMaximumSize(new Dimension(600, 983));
        add(panel, BorderLayout.NORTH);
        add(interactivePanel, BorderLayout.SOUTH);
        addKeyListener(new Tab(this));
        addKeyListener(new Quit(this));

        System.out.println("ids: " + person.out()); //  < 0  1  2 >
        String title = "";
        for (int id : person.getChatIds()) {
            if (id != -1) {
                title += (" " + id + " ");
            }
        }
        title = title.replace(" " + chat.getChatID() + " ", " (" + chat.getChatID() + ") ");
        title = title.replaceAll("  ", " / ");
        setTitle(title);
        System.out.println("set title: " + title);


        pack();
        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        client.addGetMessagesOnResponseCallback((a) -> {
            JSONArray msgs = a.getJSONArray("messages");
            for (int i = 0; i < msgs.length(); i++) {
                JSONObject msg = msgs.getJSONObject(i);
                getExistingMsgs().setText(getExistingMsgs().getText() + addMessage(chat.fromJSON(msg)));
            }
        });
        client.getMessages(chat.getChatID());

        client.setReceiveMessageCallback((a) -> {
            getExistingMsgs().setText(getExistingMsgs().getText() + addMessage(chat.fromJSON(a)));
        });
    }

    private class Tab implements KeyListener {
        private ChatDisplay frame;

        public Tab(ChatDisplay frame) {
            this.frame = frame;
        }
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 9) {
                frame.getNewMSG().setText(frame.getNewMSG().getText().replace("\t", ""));
            }
            if (e.getKeyCode() == 9 && frame.getTitle().length() != 5) {
                System.out.println("-----------------");
                frame.setVisible(false);
                String title = frame.getTitle();
                int id = frame.getTitle().indexOf(" (" + frame.getChat().getChatID() + ") ");
                System.out.println("index: " + id);
                if (id + 7 > title.length()) {
                    System.out.println("last in list");
                    System.out.println("title: <" + title + ">");
                    System.out.println("index: " + id);
                    id = nextID(title,0);
                }
                else {
                    System.out.println("not last in list");
                    System.out.println("title: <" + title + ">");
                    System.out.println("index: " + id);
                    id = nextID(title, id+2);
                }
                System.out.println("next id: " + id);
                for (ChatDisplay d : window.getDisplays()) {
                    if (d != null && d.getChat().getChatID() == id) {
                        System.out.println("happens");
                        d.setVisible(true);
                        String t = "";
                        for (int chatID : person.getChatIds()) {
                            if (chatID != -1) {
                                t += (" " + chatID + " ");
                            }
                        }
                        t = t.replace(" " + id + " ", " (" + id + ") ");
                        t = t.replaceAll("  ", " / ");
                        d.setTitle(t);
                        System.out.println("new title: " + title);
                    }
                }
                frame.getNewMSG().setText("");
                System.out.println("-----------------");
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyCode() == 9) {
                frame.getNewMSG().setText(frame.getNewMSG().getText().replace("\t", ""));
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 9) {
                frame.getNewMSG().setText(frame.getNewMSG().getText().replace("\t", ""));
            }
        }
        private int nextID(String s, int i) {
            i++;
            while (!isNum(s.charAt(i))) {
                i++;
            }
            int id = toNum(s.charAt(i));
            System.out.println(id);
            if (isNum(s.charAt(i+1))) {
                id = id*10 + toNum(s.charAt(i+1));
            }
            return id;
        }
        private boolean isNum(char c) {
            String nums = "0123456789";
            for (int i = 0; i < nums.length(); i++) {
                if (c == nums.charAt(i)) {
                    return true;
                }
            }
            return false;
        }
        private int toNum(char c) {
            String nums = "0123456789";
            int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            for (int i = 0; i < nums.length(); i++) {
                if (c == nums.charAt(i)) {
                    return values[i];
                }
            }
            return -1;
        }
    }
    private class Send implements KeyListener {
        private ChatDisplay frame;
        private TextArea newMsg;

        public Send(ChatDisplay frame) {
            this.frame = frame;
            newMsg = frame.getNewMSG();
        }
        public void keyPressed(KeyEvent e) {
            frame.getNewMSG().setText(frame.getNewMSG().getText().replace("\t", ""));
            if (e.getKeyCode() == 10 && !newMsg.getText().equals("") && !newMsg.getText().contains("\n")) {
                client.addSendMessageOnResponseCallback((a) -> {});
                client.sendMessage(newMsg.getText(), "", chat.getChatID());
                Message m = new Message(newMsg.getText(), person.getName(), "", frame.getChat().getChatID());
                getExistingMsgs().setText(getExistingMsgs().getText() + frame.addMessage(m));
                frame.getNewMSG().setText("");
                System.out.println("sent");
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
    private class Quit implements KeyListener {
        private ChatDisplay frame;

        public Quit(ChatDisplay frame) {
            this.frame = frame;
        }
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 27) {
                quit(frame);
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
    private class QuitButton implements ActionListener{
        private ChatDisplay frame;

        public QuitButton(ChatDisplay frame) {
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
        messages.add(m);
        int i = messages.size() - 1;
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

        System.out.println((int)(font.getStringBounds(content, frc).getWidth()));
        if (!person.getName().equals(sender)) {
            if (!prevSender.equals(sender)) {
                if (messages.indexOf(m) != 0) {
                    result += "\n";
                }
                result += (sender + ":" + "\n");
            }
            if (font.getStringBounds(content, frc).getWidth() <= LINE_LENGTH) {
                result += (content + "\n");
            }
            else {
                result += splitMessage(content, "") + "\n";
            }
        }
        else {
            if (!prevSender.equals(sender)) {
                if (messages.indexOf(m) != 0) {
                    result += "\n";
                }
            }
            if (font.getStringBounds(content, frc).getWidth() <= LINE_LENGTH) {
                result += (HALFWAY_SPACE + content + "\n");
            }
            else {
                result += splitMessage(content, HALFWAY_SPACE) + "\n";
            }
        }
        System.out.println("<" + result + ">");
        return result;
    }
    private String splitMessage(String s, String spaceBefore) {
        String[] words = s.split(" ");    //empty string for space
        String result = spaceBefore;
        double lineLength = 0;
        for (String word : words) {
            if (width(word) <= LINE_LENGTH) {
                if (lineLength + width(word + " ") < LINE_LENGTH) {
                    lineLength += width(word + " ");
                    result += (word + " ");
                }
                else if (lineLength + width(word) < LINE_LENGTH) {
                    lineLength += width(word);
                    result += (word);
                }
                else {
                    lineLength = width(word + " ");
                    result += ("\n" + spaceBefore + word + " ");
                }
            }
            else {
                while (width(word) > LINE_LENGTH) {
                    String subWord = sub(word, LINE_LENGTH);
                    word = word.substring(subWord.length());
                    if (lineLength > 0) {
                        result += "\n" + spaceBefore + subWord;
                    }
                    else {
                        result += subWord;
                        lineLength += width(subWord);
                    }
                }
                result += "\n" + spaceBefore + word;
            }
        }
        System.out.println("-----------------------");
        return result;
    }
   
    private String sub(String word, double LINE_LENGTH) {
        String result = "";
        double width = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            double w = font.getStringBounds(c+"", frc).getWidth();
            width += w;
            if (width > LINE_LENGTH) {
                return result;
            }
            result += c;
        }
        return result;
    }
    private double width(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s += "W";
        }
        return font.getStringBounds(s, frc).getWidth();
    }
    private double width(String word) {
        return font.getStringBounds(word, frc).getWidth();
    }
}