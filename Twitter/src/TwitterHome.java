import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TwitterHome extends JFrame {
    private static TwitterHome instance;
    private List<Post> postList;
    private CardLayout cardLayout;
    private JPanel centerPanel;
    private JPanel postContainer;

    // Buttons for bottom panel
    public JButton homeButton;
    private JButton bookmarkButton;
    private JButton searchButton;
    private JButton userButton;
    private JButton addPostButton;

    // Follow label
    private JLabel followLabel;

    public TwitterHome() {
        instance = this;

        postList = new ArrayList<>();
        setTitle("Twitter Feed");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(230, 245, 255));

        // Top Panel (Fixed)
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel with CardLayout
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);

        // Create Post Container for Home Page
        postContainer = new JPanel();
        postContainer.setLayout(new BoxLayout(postContainer, BoxLayout.Y_AXIS));
        JScrollPane homeScrollPane = new JScrollPane(postContainer);
        homeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        homeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        centerPanel.add(homeScrollPane, "Home");
        centerPanel.add(new BookmarkPage(this), "Bookmark");

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel (Fixed)
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public static TwitterHome getInstance() {
        if (instance == null) {
            instance = new TwitterHome();
        }
        return instance;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(400, 80));

        // Twitter Icon
        JLabel twitterIconLabel = new JLabel();
        ImageIcon twitterIcon = new ImageIcon("icon/twitterIcon.png");
        Image twitterImage = twitterIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        twitterIconLabel.setIcon(new ImageIcon(twitterImage));
        twitterIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(twitterIconLabel, BorderLayout.CENTER);

        // Follow Label
        JPanel followPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);

                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);
            }
        };
        followPanel.setPreferredSize(new Dimension(400, 30));
        followPanel.setOpaque(false);

        followLabel = new JLabel("Follow"); // Dynamic label for updates
        followLabel.setForeground(Color.DARK_GRAY);
        followLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 13));
        followPanel.add(followLabel);

        topPanel.add(followPanel, BorderLayout.SOUTH);
        return topPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 5));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(400, 50));

        // Home Button
        homeButton = createIconButton("icon/homePressed.png");
        homeButton.addActionListener(e -> {
            updateIcons("Home");
            updateFollowLabel("Follow"); // Update Follow label for Home
            cardLayout.show(centerPanel, "Home");
            buttonAction action = new buttonAction("Following Post",UserInfo.getInstance().getUserId());
            action.actionPerformed(null);

            // 스크롤을 맨 위로 이동
            JScrollPane homeScrollPane = (JScrollPane) centerPanel.getComponent(0);
            JViewport viewport = homeScrollPane.getViewport();
            viewport.setViewPosition(new Point(0, 0));

        });

        bottomPanel.add(homeButton);

        // Search Button (Placeholder)
        searchButton = createIconButton("icon/searchIcon.png");
        searchButton.addActionListener(e -> {
            updateIcons("Search");
        });
        bottomPanel.add(searchButton);

        // addPost Button (Placeholder)
        addPostButton = createIconButton("icon/addPostIcon.png");
        addPostButton.addActionListener(e->{
            updateIcons("addPost");
        });
        bottomPanel.add(addPostButton);

        // Bookmark Button
        bookmarkButton = createIconButton("icon/bookmarkIcon.png");
        bookmarkButton.addActionListener(e -> {
            updateIcons("Bookmark");
            updateFollowLabel("Bookmarks");
            cardLayout.show(centerPanel, "Bookmark");

            buttonAction action = new buttonAction("Bookmark",UserInfo.getInstance().getUserId());
            action.actionPerformed(null);

            // 스크롤을 맨 위로 이동
            JScrollPane homeScrollPane = (JScrollPane) centerPanel.getComponent(0);
            JViewport viewport = homeScrollPane.getViewport();
            viewport.setViewPosition(new Point(0, 0));
        });
        bottomPanel.add(bookmarkButton);

        //User Button
        userButton = createIconButton("icon/userIcon.png");
        userButton.addActionListener(e->{
            updateIcons("User");
        });
        bottomPanel.add(userButton);

        return bottomPanel;
    }


    public void addPostToHome(Post post) {
        postList.add(post); // Post 리스트에 추가
        postList.sort(Comparator.comparing(Post::getCreateAt).reversed()); // 최신순으로 정렬

        postContainer.removeAll(); // 기존 UI 초기화

        for (Post sortedPost : postList) {
            postContainer.add(Box.createVerticalStrut(10)); // Add spacing
            postContainer.add(sortedPost); // 정렬된 Post 추가
        }

        postContainer.revalidate(); // UI 갱신
        postContainer.repaint();
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

        if (!postExists) {
            Post newPost = new Post(userId, content, createAt);
            postList.add(newPost);
            postContainer.add(Box.createVerticalStrut(10));
            postContainer.add(newPost);
        }

        postList.sort(Comparator.comparing(Post::getCreateAt).reversed());
        postContainer.revalidate();
        postContainer.repaint();
    }



    public static JButton createIconButton(String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image iconImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(iconImage);

        JButton button = new JButton(resizedIcon);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        return button;
    }

    public void updateIcons(String currentPage) {
        // Update Home Button Icon
        ImageIcon homeIcon = new ImageIcon(currentPage.equals("Home") ? "icon/homePressed.png" : "icon/homeIcon.png");
        Image homeImage = homeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        homeButton.setIcon(new ImageIcon(homeImage));

        // Update Bookmark Button Icon
        ImageIcon bookmarkIcon = new ImageIcon(currentPage.equals("Bookmark") ? "icon/bookmarkPressed.png" : "icon/bookmarkIcon.png");
        Image bookmarkImage = bookmarkIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        bookmarkButton.setIcon(new ImageIcon(bookmarkImage));

        //Update Search Button Icon
        ImageIcon searchIcon = new ImageIcon(currentPage.equals("Search") ? "icon/searchPressed.png" : "icon/searchIcon.png");
        Image searchImage = searchIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        searchButton.setIcon(new ImageIcon(searchImage));

        //Update User Button Icon
        ImageIcon userIcon = new ImageIcon(currentPage.equals("User") ? "icon/userPressed.png" : "icon/userIcon.png");
        Image userImage = userIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        userButton.setIcon(new ImageIcon(userImage));

    }

    public void updateFollowLabel(String text) {
        followLabel.setText(text); // Update the Follow label dynamically
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TwitterHome::new);
    }
}
