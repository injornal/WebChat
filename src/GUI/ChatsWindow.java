package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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

import app.networking.Client;
import gui.components.Chat;
import gui.components.Person;

/**
 * Displays the different clickable chats
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class ChatsWindow extends JFrame implements Serializable {
    /**
     * 
     */
    private JPanel panel;
    /**
     * 
     */
    private app.networking.Client client;
    /**
     * 
     */
    private JButton[] JButtons;
    /**
     * 
     */
    private ChatDisplay[] displays = new ChatDisplay[7];
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
        JButtons = new JButton[7];
        for (int i = 0; i < 7; i++) {
            if (person.exists(i)) {
                JButtons[i] = new JButton("" + person.getChatIds()[i]);
            } else {
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
    
    public Client getClient() {
        return client;
    }
    public JPanel getPanel() {
        return panel;
    }
    public Person getPerson() {
        return person;
    }
    public JButton[] getJButtons() {
        return JButtons;
    }
    public ChatDisplay[] getDisplays() {
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
            System.out.println();
            System.out.println("clicked");
            if (!frame.getPerson().exists(index)) {
                JFrame clickChat = new JFrame();
                System.out.println("new chat option");
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
                System.out.println("alr a chat");
                frame.setVisible(false);
                frame.getDisplays()[index].setVisible(true);
                client.addGetMessagesOnResponseCallback((a) -> {
                    frame.getDisplays()[index].setChat(new Chat(index, a.getJSONArray("")));
                });
            }
        }
    }
    private class InputDetector implements KeyListener {
        private ChatsWindow frame;
        private JFrame inputFrame;
        private JTextField input;
        private int index;
        private final String nums = "0123456789";
        private final int[] numList = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        public InputDetector(ChatsWindow frame, JFrame inputFrame, JTextField input, int index) {
            this.frame = frame;
            this.inputFrame = inputFrame;
            this.input = input;
            this.index = index;
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
                    System.out.println("empty chat");
                    Chat chat = new Chat(-1);
                    client.addCreateChatOnResponseCallback((a) ->
                    {
                        int id = a.getInt("chat_id");
                        System.out.println("id: " + id);
                        person.setChatID(id, index);
                        chat.setChatID(id);
                        frame.getJButtons()[index] = new JButton("" + id);
                        frame.getJButtons()[index].addActionListener(new ClickChat(frame, index));
                    });
                    client.createChat();
                    frame.getDisplays()[index] = new ChatDisplay(client, chat, frame);
                    frame.setVisible(false);
                    JPanel panel = frame.getPanel();
                    panel.remove(index);
                    panel.add(frame.getJButtons()[index], index);
                    System.out.print("ids: ");
                    for (int id : person.getChatIds()) {
                        System.out.print(id + " ");
                    }
                    System.out.println();
                }
                else if (isNum(input.getText())) {
                    System.out.println("join chat");
                    int id = toNum(input.getText());
                    client.addJoinChatOnResponseCallback((b) -> {
                        if (b.getString("result").equals("SUCCESS")) {
                            System.out.println("success");
                            Chat chat = new Chat(id);
                            System.out.println("id: " + id);
                            person.setChatID(id, index);
                            frame.getJButtons()[index] = new JButton("" + id);
                            frame.getJButtons()[index].addActionListener(new ClickChat(frame, index));

                            frame.getDisplays()[index] = new ChatDisplay(client, chat, frame);
                            //retrieve messages
                            frame.setVisible(false);
                            JPanel panel = frame.getPanel();
                            panel.remove(index);
                            panel.add(frame.getJButtons()[index], index);
                            System.out.print("ids: ");
                            for (int ids : person.getChatIds()) {
                                System.out.print(ids + " ");
                            }
                            System.out.println();
                        }
                        else {
                            System.out.println("fail");
                            JOptionPane loginPane = new JOptionPane("Join Fail");
                            loginPane.showMessageDialog(null, "Join Fail");
                            loginPane.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
                        }
                    });
                    client.joinChat(id);
                }
                else {
                    System.out.println("fail");
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
                result += numList[nums.indexOf(s.charAt(i))] * (int)Math.pow(10, i);
            }
            return result;
        }
    }
    private class Logout implements ActionListener {
        private ChatsWindow frame;

        public Logout(ChatsWindow frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            System.out.println("logged out");
            try {
                // pass and quit app saveState(frame);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        private File saveState(ChatsWindow frame) throws Exception {
            File f = new File(frame.getPerson().getName() + ".txt");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(frame);
            return f;
        }
    }
}