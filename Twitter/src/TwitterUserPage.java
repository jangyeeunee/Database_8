import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

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
        setSize(500, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createAndShowGUI();
    }private void createAndShowGUI() {
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
        dbConnect db = dbConnect.getInstance();
        List<PostDetails> myPostDetailsList = db.getUserPostsWithDetails(userId);

        // Method to populate the posts
        populatePosts(postsPanel, myPostDetailsList);

        // Event handling for tab switching
        myPostsTab.addActionListener(e -> {
            postsPanel.removeAll(); // Clear current posts
            List<PostDetails> myPosts = db.getUserPostsWithDetails(userId);
            populatePosts(postsPanel, myPosts); // Update with My Posts
        });

        likedPostsTab.addActionListener(e -> {
            postsPanel.removeAll(); // Clear current posts
            List<PostDetails> likedPosts = db.getUserLikedPosts(userId);
            populatePosts(postsPanel, likedPosts); // Update with Liked Posts
        });

        // Add scrollable panel for posts
        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tabPanel, BorderLayout.CENTER);

        // Bottom Panel (for buttons or navigation if required) - 기존 코드 그대로
        BottomPanel bottomPanel = BottomPanel.getInstance();
        mainPanel.add(bottomPanel.BottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    // Helper method to populate posts
    private void populatePosts(JPanel postsPanel, List<PostDetails> postDetailsList) {
        if (postDetailsList.isEmpty()) {
            postsPanel.add(new JLabel("No posts to display."));
        } else {
            for (PostDetails post : postDetailsList) {
                JPanel postPanel = new JPanel(new BorderLayout());
                postPanel.setBackground(Color.WHITE);
                postPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                // Display content from the post
                JTextArea postContentArea = new JTextArea(post.getContent());
                postContentArea.setFont(new Font("Arial", Font.PLAIN, 14));
                postContentArea.setEditable(false);
                postContentArea.setLineWrap(true);
                postContentArea.setWrapStyleWord(true);
                postContentArea.setBackground(Color.WHITE);

                // Panel for additional post details
                JPanel postDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                postDetailsPanel.setBackground(Color.WHITE);

                // Display the author, date, comment count, and like count
                JLabel postDetailsLabel = new JLabel("By: " + post.getUsername() + " | " +
                        "Date: " + post.getCreationDate() + " | " +
                        "Comments: " + post.getCommentCount() + " | " +
                        "Likes: " + post.getLikeCount());
                postDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                postDetailsLabel.setForeground(Color.GRAY);

                postDetailsPanel.add(postDetailsLabel);

                postPanel.add(postContentArea, BorderLayout.CENTER);
                postPanel.add(postDetailsPanel, BorderLayout.SOUTH);
                postsPanel.add(postPanel);
            }
        }
        postsPanel.revalidate();
        postsPanel.repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TwitterUserPage());
    }
}
