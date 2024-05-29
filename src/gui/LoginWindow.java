package gui;
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
import gui.components.Person;
import app.networking.Client;

/**
 * Displays a login window that prompts the user to login or signup 
 * by entering their username or password
 * 
 * @author Chaitanya
 * @author Kostiantyn
 * @author Pranav
 * @version 1.0
 */
public class LoginWindow extends JFrame {
    
    private JPanel panel;
    private JTextField user;
    private JPasswordField pass;
    private JButton loginButton;
    private app.networking.Client client;

    /**
     * Creates a new window
     * @param client client
     */
    public LoginWindow(Client client) {
        this.client = client;
        client.start("127.0.0.1", 8080);

        user = new JTextField();
        pass = new JPasswordField();
        loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        loginButton.addActionListener(new LoginButton(this));
        signUpButton.addActionListener(new SignUpButton(this));
        JLabel uLabel = new JLabel("Username:");
        JLabel pLabel = new JLabel("Password:");

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,1));
        setMinimumSize(new Dimension(600, 300));
        setMaximumSize(new Dimension(600, 300));
        panel.add(uLabel);
        panel.add(user);
        panel.add(pLabel);
        panel.add(pass);
        panel.add(loginButton);
        panel.add(signUpButton);

        add(panel, BorderLayout.CENTER);
        setTitle("WebChat");
        pack();
        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private boolean isValid(String s) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789._";
        for (int c = 0; c < s.length(); c++) {
            boolean valid = false;
            for (int a = 0; a < characters.length(); a++) {
                if (s.charAt(c) == characters.charAt(a)) {
                    valid = true;
                }
            }
            if (!valid) {
                return false;
            }
        }
        return true;
    }
    private JTextField getUserField() {
        return user;
    }
    private JPasswordField getPassField() {
        return pass;
    }
    
    private void login(LoginWindow frame) {
        String u = user.getText();
        String p = "";
        char[] cList = pass.getPassword();
        for (char c : cList) {
            p += c;
        }
        client.addLoginOnResponseCallback((a) ->
        {
            String loginResult = a.getString("result");
            System.out.println("login result: " + loginResult);
            if (loginResult.equals("SUCCESS")) {
//                frame.setVisible(false);
                frame.dispose();
                new ChatsWindow(client, new Person(frame.getUserField().getText()));
            }
            else {
                JOptionPane loginPane = new JOptionPane("Login Fail");
                loginPane.showMessageDialog(null, "Login Fail");
                loginPane.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
                frame.getUserField().setText("");
                frame.getPassField().setText("");
            }
        });
        client.login(u, p);
    }
    private class LoginButton implements ActionListener {
        private LoginWindow frame;
        public LoginButton(LoginWindow frame) {
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            String u = user.getText();
            String p = "";
            char[] cList = pass.getPassword();
            for (char c : cList) {
                p += c;
            }
            client.addLoginOnResponseCallback((a) -> {
                String loginResult = a.getString("result");
                System.out.println("login result: " + loginResult);
                if (loginResult.equals("SUCCESS")) {
//                    frame.setVisible(false);
                    frame.dispose();
                    Person person = new Person(u);
                    System.out.println("ids when person is created: " + person.out());
                    client.addGetChatsOnResponseCallback((b) -> {
                        for (int i = 0; i < b.getJSONArray("chats").length(); i++) {
                            person.setChatID(b.getJSONArray("chats").getInt(i), i);
                        }
                        new ChatsWindow(client, person);
                        System.out.println("ids while person is being created: " + person.out());
                    });
                    client.getChats();
                    System.out.println("ids after person is created: " + person.out());
                }
                else {
                    JOptionPane loginPane = new JOptionPane("Login Fail");
                    loginPane.showMessageDialog(null, "Login Fail");
                    loginPane.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
                    frame.getUserField().setText("");
                    frame.getPassField().setText("");
                }
            });
            client.login(u, p);
        }   
    }
    private class SignUpButton implements ActionListener {
        private LoginWindow frame;
        public SignUpButton(LoginWindow frame) {
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            String u = user.getText();
            String p = "";
            char[] cList = pass.getPassword();
            for (char c : cList) {
                p += c;
            }
            if (u.length() < 5 || u.length() > 12 || !isValid(u)) {
                JOptionPane invalidUser = new JOptionPane("Invalid User");
                invalidUser.showMessageDialog(null, "Invalid User");
                frame.getUserField().setText("");
                frame.getPassField().setText("");
            }
            else if (p.length() < 5 || u.length() > 24) {
                JOptionPane invalidPassword = new JOptionPane("Invalid Password");
                invalidPassword.showMessageDialog(null, "Invalid Password");
                frame.getUserField().setText("");
                frame.getPassField().setText("");
            }
            else {
                client.addSignUpOnResponseCallback((a) ->
                {
                    String result = a.getString("result");
                    System.out.println("sign up result: " + result);
                    if (!result.equals("SUCCESS")) {
                        JOptionPane signUpFail = new JOptionPane("User Already Exists");
                        signUpFail.showMessageDialog(null, "User Already Exists");
                        frame.getUserField().setText("");
                        frame.getPassField().setText("");
                    }
                    else {
                        login(frame);
                    }
                });
                client.signUp(u, p);
            }
        }
    }
}