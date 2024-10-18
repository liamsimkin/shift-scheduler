package shiftSchedulerApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class LoginDisplay implements ActionListener {

    HashMap<String, String[]> loginInfo = new HashMap<String, String[]>();
    JFrame frame = new JFrame("Login");
    JButton loginButton = new JButton("Login");
    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JLabel usernameLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel messageLabel = new JLabel();

    public LoginDisplay(HashMap<String, String[]> loginInfo){
        this.loginInfo = loginInfo;

        usernameLabel.setBounds(25,100,100,35);
        passwordLabel.setBounds(25,150,100,35);
        usernameLabel.setFont(new Font(null,Font.BOLD,16));
        passwordLabel.setFont(new Font(null,Font.BOLD,16));
        messageLabel.setBounds(125,250,250,40);
        messageLabel.setFont(new Font(null,Font.ITALIC,20));

        usernameField.setBounds(125,100,200,35);
        passwordField.setBounds(125,150,200,35);

        loginButton.setBounds(125,200,100,35);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        frame.add(usernameLabel);
        frame.add(passwordLabel);
        frame.add(messageLabel);
        frame.add(usernameField);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        //frame.setBackground(Color.ORANGE);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.getRootPane().setDefaultButton(loginButton); //makes it so enter activates the button

    }

    /**
     * logs in if the details are valid
     * @param e the user's action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());

            if (loginInfo.containsKey(username) && (loginInfo.get(username)[0].equals(password))) {
                frame.dispose();
                try {
                    String[] args = {};
                    AfterLogin afterLogin = new AfterLogin(username,args);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else if (loginInfo.containsKey(username)) {
                messageLabel.setText("Incorrect password");
            }
            else {
                messageLabel.setText("Invalid username");
            }
        }

    }
}