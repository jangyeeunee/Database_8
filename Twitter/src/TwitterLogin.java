import javax.swing.*;
import java.awt.*;

public class TwitterLogin extends JFrame {
    public TwitterLogin() {
        // Set up the frame
        setTitle("Log in to Twitter");
        setSize(600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);


        // Create a main container panel with BoxLayout for vertical alignment
        JPanel postsContainer = new JPanel();
        postsContainer.setBackground(Color.WHITE);
        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS)); // Vertical BoxLayout

        // Add multiple Post panels to the container with customized width
        for (int i = 0; i < 3; i++) {
            Post post = new Post();
            post.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align each Post panel in the container
            post.setMaximumSize(new Dimension(410, 185)); // Set fixed width and height
            postsContainer.add(post);
            postsContainer.add(Box.createRigidArea(new Dimension(0, 3))); // Space between posts
        }

        // Add the posts container to the frame
        add(postsContainer, BorderLayout.CENTER);

        setVisible(true);
    }


}
