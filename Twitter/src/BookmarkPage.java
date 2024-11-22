import javax.swing.*;
import java.awt.*;

public class BookmarkPage extends JPanel {
    private static BookmarkPage instance;
    private final JPanel postContainer;

    public BookmarkPage() {
        instance = this;

        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = TopPanel.getInstance().topPanel("Bookmark");
        add(topPanel, BorderLayout.NORTH);

        // Post Container
        postContainer = new JPanel();
        postContainer.setLayout(new BoxLayout(postContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(postContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        displayPosts();
    }

    public static BookmarkPage getInstance() {
        if (instance == null) {
            instance = new BookmarkPage();
        }
        return instance;
    }

    private void displayPosts() {
        dbConnect db = dbConnect.getInstance();
        Post[] posts = db.getBookmarkPost();

        postContainer.removeAll(); // 기존 게시물 제거

        if (posts == null || posts.length == 0) {
            JLabel noPostsLabel = new JLabel("No bookmarked posts found.");
            noPostsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            noPostsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            postContainer.add(noPostsLabel);
        } else {
            for (Post post : posts) {
                if (post != null) {
                    // Post 객체를 postContainer에 추가
                    postContainer.add(post);
                }
            }
        }

        postContainer.revalidate();
        postContainer.repaint();
    }

}
