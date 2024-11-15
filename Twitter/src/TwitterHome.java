import javax.swing.*;
import java.awt.*;

public class TwitterHome extends JFrame {
    private CardLayout cardLayout;
    private JPanel centerPanel;

    // Buttons for bottom panel
    private JButton homeButton;
    private JButton bookmarkButton;
    private JButton searchButton;
    private JButton userButton;
    private JButton addPostButton;

    // Follow label
    private JLabel followLabel;

    public TwitterHome() {
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

        // Add Home and Bookmark Pages to Center Panel
        centerPanel.add(createHomePage(), "Home");
        centerPanel.add(new BookmarkPage(this), "Bookmark");


        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel (Fixed)
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
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
            updateBottomIcons("Home");
            updateFollowLabel("Follow"); // Update Follow label for Home
            cardLayout.show(centerPanel, "Home");
        });
        bottomPanel.add(homeButton);

        // Search Button (Placeholder)
        searchButton = createIconButton("icon/searchIcon.png");
        searchButton.addActionListener(e -> {
            updateBottomIcons("Search");
        });
        bottomPanel.add(searchButton);

        // addPost Button (Placeholder)
        addPostButton = createIconButton("icon/addPostIcon.png");
        addPostButton.addActionListener(e->{
            updateBottomIcons("addPost");
        });
        bottomPanel.add(addPostButton);

        // Bookmark Button
        bookmarkButton = createIconButton("icon/bookmarkIcon.png");
        bookmarkButton.addActionListener(e -> {
            updateBottomIcons("Bookmark");
            updateFollowLabel("Bookmarks"); // Update Follow label for Bookmark
            cardLayout.show(centerPanel, "Bookmark");
        });
        bottomPanel.add(bookmarkButton);

        //User Button
        userButton = createIconButton("icon/userIcon.png");
        userButton.addActionListener(e->{
            updateBottomIcons("User");
        });
        bottomPanel.add(userButton);

        return bottomPanel;
    }

    private JPanel createHomePage() {
        JPanel homePage = new JPanel();
        homePage.setLayout(new BoxLayout(homePage, BoxLayout.Y_AXIS));
        homePage.setBackground(new Color(230, 245, 255));

        for (int i = 1; i <= 20; i++) {
            homePage.add(Box.createVerticalStrut(10)); // Add spacing between posts
            homePage.add(new Post()); // Add post directly, using Post's size
        }

        JScrollPane scrollPane = new JScrollPane(homePage);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }


    // Separate method to create a single post panel
    private JPanel createPostPanel(int postNumber) {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BorderLayout());
        postPanel.setPreferredSize(new Dimension(320, 150)); // Adjusted width and height to fit better
        postPanel.setMaximumSize(new Dimension(320, 150));
        postPanel.setBackground(Color.WHITE);
        postPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        Post post = new Post();
        postPanel.add(post, BorderLayout.CENTER); // 'CENTER' 위치에 추가하여 올바르게 표시

        return postPanel;
    }


    private JButton createIconButton(String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image iconImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(iconImage);

        JButton button = new JButton(resizedIcon);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        return button;
    }

    public void updateBottomIcons(String currentPage) {
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
