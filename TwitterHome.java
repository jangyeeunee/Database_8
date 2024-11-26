import javax.swing.*;
import java.awt.*;

public class TwitterHome extends JPanel {
    private final JPanel postContainer;
    static TwitterHome instance;

    public TwitterHome() {
        setLayout(new BorderLayout());

        // Top Panel
        TopPanel topPanel = new TopPanel();
        JPanel topPanelUI = topPanel.topPanel("Follow");
        add(topPanelUI, BorderLayout.NORTH);

        // Post Container
        postContainer = new JPanel();
        postContainer.setLayout(new BoxLayout(postContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(postContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        displayPosts();
    }
    public static TwitterHome getInstance() {
        if (instance == null) {
            instance = new TwitterHome();
        }
        return instance;
    }

    private void displayPosts() {
        dbConnect db = dbConnect.getInstance();
        Post[] posts = db.getFollowingPost();

        postContainer.removeAll(); // Clear existing posts

        if (posts == null || posts.length == 0) {
            JLabel noPostsLabel = new JLabel("No posts found.");
            noPostsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            noPostsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            postContainer.add(noPostsLabel);
        } else {
            for (Post post : posts) {
                if (post != null) {
                    // Add Post object to postContainer
                    postContainer.add(post);
                }
            }
        }

        postContainer.revalidate();
        postContainer.repaint();
    }
}