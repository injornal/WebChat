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
import GUI.Components.Chat;
import GUI.Components.Person;
import App.Networking.Client;

public class LoginWindow extends JFrame {
    private JPanel panel;
    private JTextField user;
    private JPasswordField pass;
    private JButton loginButton;
    private ChatsWindow chatsWindow;
    private Person person;
    private App.Networking.Client client;

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
    
    public void setChatsWindow(ChatsWindow chatsWindow) {
        this.chatsWindow = chatsWindow;
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
    private void setPerson(Person person) {
        this.person = person;
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
            client.login(u, p);
            client.addLoginOnResponseCallback((a) ->
            {
                String result = a.getString("result");
                if (result.equals("SUCCESS")) {
                    frame.setVisible(false);

                    Chat[] chats = client.getChats(u);


                    person = new Person(u, true);
                    person.setChats(chats);
                    frame.setChatsWindow(new ChatsWindow(client, chats, person));
                }
                else {
                    JOptionPane loginPane = new JOptionPane("Login Fail");
                    loginPane.showMessageDialog(null, "Login Fail");
                    loginPane.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
                    frame.getUserField().setText("");
                    frame.getPassField().setText("");
                }
            });
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
            }
            else if (p.length() < 5 || u.length() > 24) {
                JOptionPane invalidPassword = new JOptionPane("Invalid Password");
                invalidPassword.showMessageDialog(null, "Invalid Password");
            }
            else {
                client.signUp(u, p);
                client.addSignUpOnResponseCallback((a) ->
                {
                    String result = a.getString("result");
                    if (result.equals("SUCCESS")) {
                        JOptionPane signUpPane = new JOptionPane("Sign Up Success");
                        signUpPane.showMessageDialog(null, "Sign Up Success");
                        signUpPane.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
                    
                    }
                    else {
                        JOptionPane signUpFail = new JOptionPane("User Already Exists");
                        signUpFail.showMessageDialog(null, "User Already Exists");
                        frame.getUserField().setText("");
                        frame.getPassField().setText("");
                    }
                });
            }
        }
    }
    public static void main(String[] args) {
        new LoginWindow(new Client());
    }
}
