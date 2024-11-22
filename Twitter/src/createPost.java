import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class createPost extends JFrame {
    private String userId;
    private String content;
    private int repost_id;
    private Timestamp createAt;

    public createPost(){
        setTitle("create post");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        //create text filed
        JTextField content = new JTextField(15);
        content.setMaximumSize(new Dimension(300, 30));
        content.setFont(new Font("Arial", Font.PLAIN, 14));
        content.setBorder(BorderFactory.createTitledBorder("Phone, email, or username"));
        panel.add(content);

        //create hashtag filed
        JTextField hashtag = new JTextField(15);
        hashtag.setMaximumSize(new Dimension(300, 30));
        hashtag.setFont(new Font("Arial", Font.PLAIN, 14));
        hashtag.setBorder(BorderFactory.createTitledBorder("Hashtag"));
        panel.add(hashtag);

        //create upload button




    }
    public Timestamp getCreateAt() {
        return createAt;
    }

    public static void main(String[] args) {
        createPost post = new createPost();
    }
}




