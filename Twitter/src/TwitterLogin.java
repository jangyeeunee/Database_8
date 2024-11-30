import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

public class TwitterLogin extends JFrame {
    public TwitterLogin() {
        // Set up the frame
        setTitle("Log in to Twitter");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Twitter logo
        JLabel logoLabel = new JLabel(new ImageIcon("twitter_logo.png")); // Add a Twitter logo here if available
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel);

        // Title label
        JLabel titleLabel = new JLabel("Log in to Twitter");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Username field
        JTextField usernameField = new JTextField(15);
        usernameField.setMaximumSize(new Dimension(300, 30));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createTitledBorder("Phone, email, or username"));
        panel.add(usernameField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password field
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        panel.add(passwordField);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Login button
        JButton loginButton = new JButton("Log in");
        loginButton.setBackground(new Color(29, 161, 242));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loginButton);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        //sign-up links
        JPanel linkPanel = new JPanel();
        linkPanel.setBackground(Color.WHITE);
        linkPanel.setLayout(new FlowLayout());

        JLabel signUp = new JLabel("Sign up for Twitter");
        signUp.setForeground(new Color(29, 161, 242));
        signUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkPanel.add(signUp);

        panel.add(linkPanel);

        // Add action listener for login button
        loginButton.addActionListener(e -> {
            String id = usernameField.getText();
            String password = new String(passwordField.getPassword());
            dbConnect db = dbConnect.getInstance();
            db.loginDB( id, password, this);
        });

        signUp.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        // 새 창 열기
                                        try {
                                            new twitterSignUp();
                                        } catch (ParseException e1) {
                                        }
                                        dispose(); // 현재 창 닫기
                                    }
                                }
        );

        // Add the main panel to the frame
        add(panel);
        setVisible(true);
    }
}
