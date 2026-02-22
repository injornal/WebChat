package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.components.Chat;
import gui.components.Person;
import networking.Client;

/**
 * Displays the different clickable chats
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class ChatsWindow extends JFrame {
    
    private JPanel panel;
    private Client client;
    private ArrayList<JButton> JButtons;
    private ArrayList<ChatDisplay> displays = new ArrayList<ChatDisplay>(7);
    private Person person;

    /**
     * Creates a new window
     * 
     * @param client client
     * @param person Person using the window
     */
    public ChatsWindow(Client client, Person person) {
        this.client = client;
        this.person = person;
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(2, 4));
        setMinimumSize(new Dimension(600, 300));
        setMaximumSize(new Dimension(600, 300));
        JButtons = new ArrayList<JButton>(7);
        for (int i = 0; i < 7; i++) {
            displays.add(null);
        }
        for (int i = 0; i < 7; i++) {
            if (person.exists(i)) {
                JButtons.add(new JButton("" + person.getChatIds().get(i)));
            }
            else {
                JButtons.add(new JButton("New Chat"));
            }
            JButtons.get(i).addActionListener(new ClickChat(this, i));
            panel.add(JButtons.get(i));
        }

        JButton logout = new JButton("Logout");
        logout.addActionListener(new Logout(this, this.client));
        panel.add(logout);

        add(panel, BorderLayout.CENTER);
        setTitle("WebChat: " + person.getName());
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    /**
     * Get client
     * @return client
     */
    public Client getClient() {
        return client;
    }
    /**
     * Get panel
     * @return panel
     */
    public JPanel getPanel() {
        return panel;
    }
    /**
     * Get person
     * @return person
     */
    public Person getPerson() {
        return person;
    }
    /**
     * Get Jbuttons
     * @return Array of Jbuttons
     */
    public ArrayList<JButton> getJButtons() {
        return JButtons;
    }
    /**
     * Get displays
     * @return Array of displays
     */
    public ArrayList<ChatDisplay> getDisplays() {
        return displays;
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
                JFrame clickChat = new JFrame();
                clickChat.setMinimumSize(new Dimension(400, 50));
                clickChat.setMaximumSize(new Dimension(400, 50));
                JPanel textPanel = new JPanel();
                JTextField input = new JTextField(20);
                input.addKeyListener(new InputDetector(frame, clickChat, input, index));
                textPanel.add(input);
                clickChat.setTitle("New Chat: Press Enter  /  Join Chat: Enter ID");
                clickChat.add(textPanel);
                clickChat.pack();
                clickChat.setVisible(true);

            }
            else {
                frame.setVisible(false);
                client.addGetMessagesOnResponseCallback((a) -> {
                    frame.getDisplays().set(index, new ChatDisplay(client, new Chat(index, a.getJSONArray("messages")), frame, person));
                });
                client.getMessages(frame.getPerson().getChatIds().get(index));
            }
        }
    }
    private class InputDetector implements KeyListener {
        private ChatsWindow frame;
        private JFrame inputFrame;
        private JTextField input;
        private int index;
        private final String nums = "0123456789";
        private ArrayList<Integer> numList;
        public InputDetector(ChatsWindow frame, JFrame inputFrame, JTextField input, int index) {
            this.frame = frame;
            this.inputFrame = inputFrame;
            this.input = input;
            this.index = index;
            numList = new ArrayList<Integer>();
            for (int i = 0; i < 10; i++) {
                numList.add(i);
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {    
        }
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                if (input.getText().equals("")) {
                    Chat chat = new Chat(-1);
                    client.addCreateChatOnResponseCallback((a) -> {
                        int id = a.getInt("chat_id");
                        person.setChatID(id, index);
                        chat.setChatID(id);
                        frame.getJButtons().set(index, new JButton("" + id));
                        frame.getJButtons().get(index).addActionListener(new ClickChat(frame, index));

                        frame.getDisplays().set(index, new ChatDisplay(client, chat, frame, person));
                        frame.setVisible(false);
                        JPanel panel = frame.getPanel();
                        panel.remove(index);
                        panel.add(frame.getJButtons().get(index), index);
                    });
                    client.createChat();
                }
                else if (isNum(input.getText())) {
                    int id = toNum(input.getText());
                    client.addJoinChatOnResponseCallback((b) -> {
                        if (b.getString("result").equals("SUCCESS")) {
                            Chat chat = new Chat(id);
                            person.setChatID(id, index);
                            frame.getJButtons().set(index, new JButton("" + id));
                            frame.getJButtons().get(index).addActionListener(new ClickChat(frame, index));

                            frame.getDisplays().set(index, new ChatDisplay(client, chat, frame, person));
                            frame.setVisible(false);
                            JPanel panel = frame.getPanel();
                            panel.remove(index);
                            panel.add(frame.getJButtons().get(index), index);
                        }
                        else {
                            JOptionPane loginPane = new JOptionPane("Join Fail");
                            loginPane.showMessageDialog(null, "Join Fail");
                            loginPane.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
                        }
                    });
                    client.joinChat(id);
                }
                else {
                    JOptionPane loginPane = new JOptionPane("Join Fail");
                    loginPane.showMessageDialog(null, "Join Fail");
                    loginPane.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
                }
                inputFrame.dispose();
            }
        }
        private boolean isNum(String s) {
            for (int i = 0; i < s.length(); i++) {
                if (nums.indexOf(s.charAt(i)) == -1) {
                    return false;
                }
            }
            return true;
        }
        private int toNum(String s) {
            int result = 0;
            for (int i = 0; i < s.length(); i++) {
                result += numList.get(nums.indexOf(s.charAt(i))) * (int)Math.pow(10, i);
            }
            return result;
        }
    }
    private class Logout implements ActionListener {
        private ChatsWindow frame;
        private Client client;

        public Logout(ChatsWindow frame, Client client) {
            this.frame = frame;
            this.client = client;
        }

        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            new LoginWindow(this.client);
        }
    }
}