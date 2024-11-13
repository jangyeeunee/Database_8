import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TwitterHome extends JFrame {
    ImageIcon home = new ImageIcon("icon/homeIcon.png");
    ImageIcon homePressed = new ImageIcon("icon/homePressed.png");
    ImageIcon twitterIcon = new ImageIcon("icon/twitterIcon.png");
    ImageIcon search = new ImageIcon("icon/searchIcon.png");
    ImageIcon searchPressed = new ImageIcon("icon/searchPressed.png");
    ImageIcon bookmark = new ImageIcon("icon/bookmarkIcon.png");
    ImageIcon bookmarkPressed = new ImageIcon("icon/bookmarkPressed.png");
    ImageIcon addPost = new ImageIcon("icon/addPostIcon.png");

    public TwitterHome() {
        setTitle("Twitter Feed");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(230, 245, 255));

        // Top panel (white background)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(400, 50));
        topPanel.setLayout(new BorderLayout());

        Image img = twitterIcon.getImage();
        Image changeImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon changeIcon = new ImageIcon(changeImg);

        JLabel centerImageLabel = new JLabel();
        centerImageLabel.setIcon(changeIcon);
        centerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerImageLabel.setSize(5, 5);

        topPanel.add(centerImageLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Feed panel
        JPanel feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        feedPanel.setBackground(new Color(230, 245, 255));

        for (int i = 0; i < 20; i++) {
            JPanel emptySection = new JPanel();
            emptySection.setPreferredSize(new Dimension(400, 100));
            emptySection.setBackground(new Color(230, 245, 255));
            feedPanel.add(emptySection);
        }

        JScrollPane scrollPane = new JScrollPane(feedPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel (white background with GridLayout for navigation buttons)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(400, 70));
        bottomPanel.setLayout(new GridLayout(1, 4));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Home Button
        Image homeImg = home.getImage();
        Image changeHome = homeImg.getScaledInstance(38, 38, Image.SCALE_SMOOTH);
        ImageIcon changeHomeIcon = new ImageIcon(changeHome);
        JButton homeButton = new JButton(changeHomeIcon);
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(false);
        bottomPanel.add(homeButton);

        Image pressedHomeImg = homePressed.getImage();
        Image changeHomePressedImg = pressedHomeImg.getScaledInstance(38, 38, Image.SCALE_SMOOTH);
        ImageIcon changePressedIcon = new ImageIcon(changeHomePressedImg);
        homeButton.setPressedIcon(changePressedIcon);

        // Add ActionListener for Home Button
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Navigating to Home Feed...");
                // Here you could implement navigation logic to show the Home Feed
            }
        });

        // Search Button
        Image searchImg = search.getImage();
        Image changeSearch = searchImg.getScaledInstance(38, 38, Image.SCALE_SMOOTH);
        ImageIcon changeSearchIcon = new ImageIcon(changeSearch);
        JButton searchButton = new JButton(changeSearchIcon);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setContentAreaFilled(false);
        bottomPanel.add(searchButton);

        Image pressedSearchImg = searchPressed.getImage();
        Image changeSearchPressedImg = pressedSearchImg.getScaledInstance(38, 38, Image.SCALE_SMOOTH);
        ImageIcon changeSearchPressedIcon = new ImageIcon(changeSearchPressedImg);
        searchButton.setPressedIcon(changeSearchPressedIcon);

        // Add ActionListener for Search Button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Navigating to Search...");
                // Implement navigation logic for Search
            }
        });

        // Bookmark Button
        Image bookmarkImg = bookmark.getImage();
        Image changeBookmark = bookmarkImg.getScaledInstance(35, 38, Image.SCALE_SMOOTH);
        ImageIcon changeBookmarkIcon = new ImageIcon(changeBookmark);
        JButton bookmarkButton = new JButton(changeBookmarkIcon);
        bookmarkButton.setBorderPainted(false);
        bookmarkButton.setFocusPainted(false);
        bookmarkButton.setContentAreaFilled(false);
        bottomPanel.add(bookmarkButton);

        Image pressedBookmarkImg = bookmarkPressed.getImage();
        Image changeBookmarkPressedImg = pressedBookmarkImg.getScaledInstance(35, 38, Image.SCALE_SMOOTH);
        ImageIcon changeBookmarkPressedIcon = new ImageIcon(changeBookmarkPressedImg);
        bookmarkButton.setPressedIcon(changeBookmarkPressedIcon);

        // Add ActionListener for Bookmark Button
        bookmarkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Navigating to Bookmarks...");
                // Implement navigation logic for Bookmarks
            }
        });

        // Add Post Button
        Image addPostImg = addPost.getImage();
        Image changeAddPost = addPostImg.getScaledInstance(38, 38, Image.SCALE_SMOOTH);
        ImageIcon changeAddPostIcon = new ImageIcon(changeAddPost);
        JButton addPostButton = new JButton(changeAddPostIcon);
        addPostButton.setBorderPainted(false);
        addPostButton.setFocusPainted(false);
        addPostButton.setContentAreaFilled(false);
        bottomPanel.add(addPostButton);

        // Add ActionListener for Add Post Button
        addPostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Navigating to Add Post...");
                // Implement navigation logic to show Add Post screen
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TwitterHome());
    }
}
