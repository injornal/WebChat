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

import org.json.JSONArray;

import javax.swing.JPasswordField;
import gui.components.Person;
import app.networking.Client;

import app.networking.Client;
import gui.components.Chat;
import gui.components.Person;

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
    /**
     * 
     */
    private JPanel panel;
    /**
     * 
     */
    private JTextField user;
    /**
     * 
     */
    private JPasswordField pass;
    /**
     * 
     */
    private JButton loginButton;
    /**
     * 
     */
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
    }
    
    private boolean isAlpha(String s) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int c = 0; c < s.length(); c++) {
            boolean alpha = false;
            for (int a = 0; a < alphabet.length(); a++) {
                if (s.charAt(c) == alphabet.charAt(a)) {
                    alpha = true;
                }
            }
            if (!alpha) {
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
                frame.setVisible(false);
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
            frame.setVisible(false);
            Person person = new Person(frame.getUserField().getText());

            client.addGetChatsOnResponseCallback((a) -> {
                person.setChatID(a.getJSONArray("chats"));
            });
            client.getChats();
            new ChatsWindow(client, person);
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
            if (u.length() < 5 || u.length() > 12 || !isAlpha(u)) {
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
                });
                client.signUp(u, p);
                login(frame);
            }
        }
    }
}