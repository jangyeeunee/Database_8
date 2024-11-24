import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class createPost extends JFrame {
    private String userId;
    private String content;
    private int repost_id;
    private Timestamp createAt;

    public createPost(){
        Map<String,String> data = new HashMap<>();

        setTitle("create post");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        //create text filed
        JTextArea content = new JTextArea();
        content.setWrapStyleWord(true); // Enable word wrapping
        content.setLineWrap(true);
        content.setMaximumSize(new Dimension(300, 300));
        content.setFont(new Font("Arial", Font.PLAIN, 14));
        content.setBorder(BorderFactory.createTitledBorder("Enter your post content"));
        panel.add(content);

        //create hashtag filed
        JTextField hashtag = new JTextField(15);
        hashtag.setMaximumSize(new Dimension(300, 30));
        hashtag.setFont(new Font("Arial", Font.PLAIN, 14));
        hashtag.setBorder(BorderFactory.createTitledBorder("Hashtag"));
        panel.add(hashtag);

        //create upload button
        JButton loginButton = new JButton("upload post");
        loginButton.setBackground(new Color(29, 161, 242));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loginButton);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        loginButton.addActionListener(e -> {
            data.put("content", content.getText());
            data.put("hashtag", hashtag.getText());
            dbConnect db = dbConnect.getInstance();
            db.CreatePost(data,this);
        });

        add(panel);
        setVisible(true);
    }
    public Timestamp getCreateAt() {
        return createAt;
    }

    public static void main(String[] args) {
        createPost post = new createPost();
    }
}




