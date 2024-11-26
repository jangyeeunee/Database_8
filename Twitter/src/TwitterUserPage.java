import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TwitterUserPage extends JFrame {
    private String userId;
    private String userName;
    private int followingCount;
    private int followerCount;
    private boolean isCurrentUser;

    public TwitterUserPage() {
        UserInfo userInfo = UserInfo.getInstance();
        this.userId = userInfo.getUserId();
        this.userName = userInfo.getUserFirstName() + " " + userInfo.getUserLastName();
        this.followingCount = 10;  // 임시 값
        this.followerCount = 100;  // 임시 값

        setTitle("Twitter User Profile");
        setSize(400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Top Panel with user information (기존과 동일)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(Color.WHITE);

        JLabel displayNameLabel = new JLabel(userName);
        displayNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel userIdLabel = new JLabel("@" + userId);
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userIdLabel.setForeground(Color.GRAY);

        JLabel followingLabel = new JLabel("Following: " + followingCount + "  |  Followers: " + followerCount);
        followingLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        userInfoPanel.add(displayNameLabel);
        userInfoPanel.add(userIdLabel);
        userInfoPanel.add(followingLabel);

        topPanel.add(userInfoPanel, BorderLayout.CENTER);

        if (!isCurrentUser) {
            JButton followButton = new JButton("Follow");
            followButton.setBackground(new Color(29, 161, 242));
            followButton.setForeground(Color.WHITE);
            followButton.setFont(new Font("Arial", Font.BOLD, 14));
            topPanel.add(followButton, BorderLayout.EAST);
        }

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Tabs for My Posts and Liked Posts
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout());
        tabPanel.setBackground(Color.WHITE);

        JPanel tabButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton myPostsTab = new JButton("My Posts");
        JButton likedPostsTab = new JButton("Liked Posts");

        for (JButton tab : new JButton[]{myPostsTab, likedPostsTab}) {
            tab.setBackground(Color.WHITE);
            tab.setForeground(new Color(29, 161, 242));
            tab.setFont(new Font("Arial", Font.BOLD, 14));
            tab.setFocusPainted(false);
        }

        tabButtonsPanel.add(myPostsTab);
        tabButtonsPanel.add(likedPostsTab);
        tabPanel.add(tabButtonsPanel, BorderLayout.NORTH);

        // Panel to display posts (empty initially)
        JPanel postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);

        // Fetch My Posts initially
        UserPostPage myPostsPage = UserPostPage.getInstance();
        myPostsPage.displayPosts(postsPanel, userId); // My posts will be fetched and displayed

        // Event handling for tab switching
        myPostsTab.addActionListener(e -> {
            postsPanel.removeAll(); // Clear current posts
            UserPostPage.getInstance().displayPosts(postsPanel, userId); // Update with My Posts
        });

        likedPostsTab.addActionListener(e -> {
            postsPanel.removeAll(); // Clear current posts
            LikedPostPage.getInstance().displayPosts(postsPanel, userId); // Update with Liked Posts
        });

        // Add scrollable panel for posts
        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tabPanel, BorderLayout.CENTER);

        // Bottom Panel (for buttons or navigation if required)
        BottomPanel bottomPanel = BottomPanel.getInstance();
        mainPanel.add(bottomPanel.BottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TwitterUserPage());
    }
}

class UserPostPage {
    private static UserPostPage instance;
    private final JPanel postContainer;

    public UserPostPage() {
        instance = this;
        postContainer = new JPanel();
        postContainer.setLayout(new BoxLayout(postContainer, BoxLayout.Y_AXIS));
    }

    public static UserPostPage getInstance() {
        if (instance == null) {
            instance = new UserPostPage();
        }
        return instance;
    }

    public void displayPosts(JPanel postsPanel, String userId) {
        dbConnect db = dbConnect.getInstance();
        // Fetch user's posts
        List<Post> posts = db.getUserPosts(userId); 

        postsPanel.removeAll(); // Clear current posts

        if (posts == null || posts.isEmpty()) {
            JLabel noPostsLabel = new JLabel("No posts found.");
            noPostsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            noPostsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            postsPanel.add(noPostsLabel);
        } else {
            for (Post post : posts) {
                if (post != null) {
                    postsPanel.add(post);
                }
            }
        }

        postsPanel.revalidate(); // Refresh layout
        postsPanel.repaint();    // Redraw UI
    }
}

class LikedPostPage {
    private static LikedPostPage instance;
    private final JPanel postContainer;

    public LikedPostPage() {
        instance = this;
        postContainer = new JPanel();
        postContainer.setLayout(new BoxLayout(postContainer, BoxLayout.Y_AXIS));
    }

    public static LikedPostPage getInstance() {
        if (instance == null) {
            instance = new LikedPostPage();
        }
        return instance;
    }

    public void displayPosts(JPanel postsPanel, String userId) {
        dbConnect db = dbConnect.getInstance();
        // Fetch user's liked posts
        List<Post> posts = db.getUserLikedPosts(userId);

        postsPanel.removeAll(); // Clear current posts

        if (posts == null || posts.isEmpty()) {
            JLabel noPostsLabel = new JLabel("No liked posts found.");
            noPostsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            noPostsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            postsPanel.add(noPostsLabel);
        } else {
            for (Post post : posts) {
                if (post != null) {
                    postsPanel.add(post);
                }
            }
        }

        postsPanel.revalidate(); // Refresh layout
        postsPanel.repaint();    // Redraw UI
    }
}
