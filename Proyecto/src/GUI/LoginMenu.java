package GUI;

import javax.swing.*;
import java.awt.*;

public class LoginMenu extends JFrame {
    public LoginMenu() {
        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton login = new JButton("Login");
        gbc.gridx = 4;
        gbc.gridy = 4;
        panel.add(login, gbc);

        JButton register = new JButton("Doesn't have an account? Register here!");
        gbc.gridx = 4;
        gbc.gridy = 5;
        gbc.gridwidth = 10;
        panel.add(register, gbc);

        JTextArea userName = new JTextArea();
        gbc.gridx = 5;
        gbc.gridy = 5;
        panel.add(userName, gbc);

        JPasswordField password = new JPasswordField();
        gbc.gridx = 5;
        gbc.gridy = 6;
        panel.add(password, gbc);

        frame.setVisible(true);

    }
}
