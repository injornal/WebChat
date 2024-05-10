package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import App.Networking.Client;

public class Login extends JFrame{
    private JPanel panel;
    //private MessengerWindow window;

    private JTextField user;
    private JPasswordField pass;
    private JButton button;
    private JPopupMenu popup;

    private App.Networking.Client client;

    public Login() {
        JFrame frame = new JFrame();
        user = new JTextField();
        pass = new JPasswordField();
        button = new JButton("login");
        button.addActionListener(new loginButton());
        popup = new JPopupMenu(",");
        //JButton signUpButton = new JButton("sign up");
        
        JLabel uLabel = new JLabel("username:");
        JLabel pLabel = new JLabel("password:");

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,1));
        frame.setMinimumSize(new Dimension(600, 300));
        frame.setMaximumSize(new Dimension(600, 300));
        panel.add(uLabel);
        panel.add(user);
        panel.add(pLabel);
        panel.add(pass);
        panel.add(button);
        panel.add(button);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("WebChat");
        frame.pack();
        frame.setVisible(true);

        client = new Client();
    }

    private class loginButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String u = user.getText();
            char[] cList = pass.getPassword();
            String p = "";
            for (char c : cList) {
                p += c;
            }
            client.login(u, p);
            client.addLoginOnResponseCallback((a) ->
            {
                String result = a.getString("result");
                if (!result.equals("SUCCESS")) {
                    //open chats
                }
                else {
                    panel.add(popup);
                }
            });
        }
    }
    public static void main(String[] args) {
        new Login();
    }
}
