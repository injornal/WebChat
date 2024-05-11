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

public class Login extends JFrame {
    private JPanel panel;
    //private MessengerWindow window;

    private JTextField user;
    private JPasswordField pass;
    private JButton loginButton;

    private App.Networking.Client client;

    public Login() {
        user = new JTextField();
        pass = new JPasswordField();
        loginButton = new JButton("login");
        JButton signupButton = new JButton("sign up");
        loginButton.addActionListener(new loginButton(this));
        signupButton.addActionListener(new signupButton(this));

        JLabel uLabel = new JLabel("username:");
        JLabel pLabel = new JLabel("password:");

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
        panel.add(signupButton);

        add(panel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WebChat");
        pack();
        setVisible(true);

        client = new Client();
        client.start("127.0.0.1", 8080);
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
    private class loginButton implements ActionListener {
        private Login frame;
        public loginButton(Login frame) {
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
                    System.out.println("login success");
                    //open chats
                }
                else {
                    System.out.println("login fail");
                    JOptionPane loginPane = new JOptionPane("login fail");
                    loginPane.showMessageDialog(null, "login fail");
                    loginPane.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
                    frame.getUserField().setText("");
                    frame.getPassField().setText("");
                }
            });
        }
    }
    private class signupButton implements ActionListener {
        private Login frame;
        public signupButton(Login frame) {
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            JOptionPane signup = new JOptionPane("");
            String u = user.getText();
            String p = "";
            char[] cList = pass.getPassword();
            for (char c : cList) {
                p += c;
            }

            if (u.length() < 5 || u.length() > 12 || !isAlpha(u)) {
                JOptionPane invalidUser = new JOptionPane("invalid user");
                invalidUser.showMessageDialog(null, "invalid user");
            }
            else if (p.length() < 5 || u.length() > 24) {
                JOptionPane invalidPassword = new JOptionPane("invalid password");
                invalidPassword.showMessageDialog(null, "invalid password");
            }
            else {
                client.signUp(u, p);
                client.addSignUpOnResponseCallback((a) ->
                {
                    String result = a.getString("result");
                    if (result.equals("SUCCESS")) {
                        System.out.println("signup success");
                        //open chats
                    }
                    else {
                        System.out.println("signup fail");
                        JOptionPane signupFail = new JOptionPane("user already exists");
                        signupFail.showMessageDialog(null, "user already exists");
                        frame.getUserField().setText("");
                        frame.getPassField().setText("");
                    }
                });
            }
        }
    }
    public static void main(String[] args) {
        new Login();
    }
}
