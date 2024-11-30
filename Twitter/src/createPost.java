import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class createPost extends JFrame {
    private static createPost createpost;

    public createPost() {
        Map<String, String> data = new HashMap<>();
        setTitle("Add Post");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        //create text filed
        JTextArea content = new JTextArea();
        content.setWrapStyleWord(true); // Enable word wrapping
        content.setLineWrap(true);
        content.setFont(new Font("Arial", Font.PLAIN, 14));
        content.setBorder(BorderFactory.createTitledBorder("write your content"));
        JScrollPane scrollPane = new JScrollPane(content);
        add(scrollPane, BorderLayout.CENTER);
        panel.add(content);

        //create hashtag filed
        JTextField hashtag = new JTextField(15);
        hashtag.setMaximumSize(new Dimension(this.getWidth(), 10));
        hashtag.setFont(new Font("Arial", Font.PLAIN, 14));
        hashtag.setBorder(BorderFactory.createTitledBorder("Hashtag"));
        panel.add(hashtag);

        //create upload button
        JButton uploadpost = new JButton("upload post");
        uploadpost.setBackground(new Color(29, 161, 242));
        uploadpost.setForeground(Color.BLACK);
        uploadpost.setFont(new Font("Arial", Font.BOLD, 14));
        uploadpost.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(uploadpost);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        uploadpost.addActionListener(e -> {
            data.put("content", content.getText());
            data.put("hashtag", hashtag.getText());
            dbConnect db = dbConnect.getInstance();
            db.CreatePost(data,this);
        });

        add(panel);
        setVisible(true);
    }

    public static createPost getInstance() {
        if (createpost == null) {
            createpost = new createPost();
        }
        return createpost;
    }
}




