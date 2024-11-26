import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TwitterUserPage extends JPanel {
    private String userId;
    private String userName;
    private int followingCount;
    private int followerCount;
    private boolean isCurrentUser;
    private JPanel postsPanel; // 게시물 표시 패널

    public TwitterUserPage() {
        UserInfo userInfo = UserInfo.getInstance();
        this.userId = userInfo.getUserId();
        this.userName = userInfo.getUserFirstName() + " " + userInfo.getUserLastName();
        this.followingCount = 10;  // 임시 값
        this.followerCount = 100;  // 임시 값
        this.isCurrentUser = true; // 현재 로그인한 사용자인지 여부 (예시)

        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top Panel with user information
        JPanel topPanel = new JPanel(new BorderLayout());
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

        add(topPanel, BorderLayout.NORTH);

        // Tabs for My Posts and Liked Posts
        JPanel tabPanel = new JPanel(new BorderLayout());
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

        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);

        // 초기 화면: My Posts를 로드
        loadMyPosts();

        // Tab Switching
        myPostsTab.addActionListener(e -> {
            postsPanel.removeAll();
            loadMyPosts();
        });

        likedPostsTab.addActionListener(e -> {
            postsPanel.removeAll();
            loadLikedPosts();
        });

        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        add(tabPanel, BorderLayout.CENTER);

        // Bottom Panel
        BottomPanel bottomPanel = BottomPanel.getInstance();
        add(bottomPanel.BottomPanel(), BorderLayout.SOUTH);
    }

    private void loadMyPosts() {
        dbConnect db = dbConnect.getInstance();
        List<Post> posts = db.getUserPosts(userId); // 유저의 게시물 가져오기

        displayPosts(posts);
    }

    private void loadLikedPosts() {
        dbConnect db = dbConnect.getInstance();
        List<Post> posts = db.getUserLikedPosts(userId); // 유저가 좋아요한 게시물 가져오기

        displayPosts(posts);
    }

    private void displayPosts(List<Post> posts) {
        postsPanel.removeAll(); // 기존 게시물 제거

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

        postsPanel.revalidate();
        postsPanel.repaint();
    }
}
