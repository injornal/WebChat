package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class Chats implements ActionListener{

    private JFrame frame;
    private JPanel panel;

    private JLabel uLabel;
    private JLabel pLabel;
    private JTextField user;
    private JPasswordField pass;

    private JButton button;

    public Chats() {
        JFrame frame = new JFrame();

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        button = new JButton("login");
        button.addActionListener(this);

        uLabel = new JLabel("username:");
        pLabel = new JLabel("password:");

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0,1));
        panel.add(uLabel);
        panel.add(user);
        panel.add(pLabel);
        panel.add(pass);
        panel.add(button);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("WebChat");
        frame.pack();
        frame.setVisible(true);
    }

    public void displayLogin(){
        new Chats();
    }

    public void displayListOfChats(){
        
    }
    public static void main(String[] args) {
        Chats m = new Chats();
        m.displayLogin();
    } 
    @Override
    public void actionPerformed(ActionEvent e) {
        String u = user.getText();
        char[] cList = pass.getPassword();
        String p = "";
        for (char c : cList) {
            p += c;
        }
        
    }
}
