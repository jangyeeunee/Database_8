import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BookmarkPage extends JPanel {
    private static BookmarkPage instance;
    private List<Post> postList;
    private Container postContainer;

    private CardLayout cardLayout;
    private JPanel centerPanel;

    public BookmarkPage() {
        instance = this;

        postList = new ArrayList<>();

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Center Panel with CardLayout
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);

        // Create Post Container for Home Page
        postContainer = new JPanel();
        postContainer.setLayout(new BoxLayout(postContainer, BoxLayout.Y_AXIS));
        JScrollPane bookmarkScrollPane = new JScrollPane(postContainer);
        bookmarkScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bookmarkScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        centerPanel.add(bookmarkScrollPane, "Bookmark");

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    public static BookmarkPage getInstance() {
        if (instance == null) {
            instance = new BookmarkPage();
        }
        return instance;
    }

    public void addOrUpdatePost(int postId, String userId, String content, Timestamp createAt) {
        boolean postExists = false;

        for (Post post : postList) {
            if (post.getCreateAt().equals(createAt)) {
                post.postUpdate(userId, content, createAt);
                postExists = true;
                break;
            }
        }

//        if (!postExists) {
//            Post newPost = new Post(userId, content, createAt);
//            postList.add(newPost);
//            postContainer.add(Box.createVerticalStrut(10));
//            postContainer.add(newPost);
//        }

        postList.sort(Comparator.comparing(Post::getCreateAt).reversed());
        postContainer.revalidate();
        postContainer.repaint();
    }


}
