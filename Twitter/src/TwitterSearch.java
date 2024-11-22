import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TwitterSearch extends JFrame {
    private CardLayout cardLayout;
    private JPanel centerPanel;
    private JTextField searchField;
    private JPanel resultPanel;
    private JPanel topPanel;

    // Bottom panel buttons
    private JButton homeButton;
    private JButton bookmarkButton;
    private JButton searchButton;
    private JButton userButton;
    private JButton addPostButton;

    public TwitterSearch() {
        setTitle("Twitter Search");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // 상단 검색창 패널
        topPanel = createSearchBar();
        topPanel.setVisible(false);
        add(topPanel, BorderLayout.NORTH);

        // CardLayout과 centerPanel 초기화
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);

        centerPanel.add(createHomePanel(), "Home");
        centerPanel.add(createResultPanel(), "Search");
        centerPanel.add(createBookmarkPanel(), "Bookmark");
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createSearchBar() {
        JPanel searchPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(230, 230, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
            }
        };

        searchPanel.setLayout(new BorderLayout());
        searchPanel.setPreferredSize(new Dimension(250, 40));
        searchPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("🔍");
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(40, 40));

        searchField = new JTextField("Search");
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // 여백 설정
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setOpaque(false);

        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
                animateExpansion(250, 400); // 검색창 확장
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search");
                    searchField.setForeground(Color.GRAY);
                }
                animateExpansion(400, 250); // 검색창 축소
            }
        });

        // 스페이스바를 눌렀을 때 검색 실행
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 스페이스바가 눌렸을 때만 검색을 수행
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    performSearch(searchField.getText().trim());  // 검색어의 공백을 제거하고 검색
                }
            }
        });

        searchPanel.add(iconLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        return searchPanel;
    }

    private void animateExpansion(int startWidth, int endWidth) {
        Timer timer = new Timer(5, null); // 5ms 간격으로 동작
        timer.addActionListener(e -> {
            int currentWidth = searchField.getWidth();
            if (startWidth < endWidth) { // 확장
                if (currentWidth < endWidth) {
                    searchField.setPreferredSize(new Dimension(currentWidth + 5, searchField.getHeight()));
                    searchField.revalidate();
                } else {
                    timer.stop();
                }
            } else { // 축소
                if (currentWidth > endWidth) {
                    searchField.setPreferredSize(new Dimension(currentWidth - 5, searchField.getHeight()));
                    searchField.revalidate();
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(new Color(240, 248, 255));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        JLabel welcomeLabel = new JLabel("Follow");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(29, 161, 242)); // Twitter 기본 색상
        headerPanel.add(welcomeLabel);

        homePanel.add(headerPanel, BorderLayout.NORTH);

        return homePanel;
    }

    private JPanel createResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setBackground(new Color(240, 248, 255));
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Search results will appear here...");
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(label);

        return resultPanel;
    }

    private JPanel createBookmarkPanel() {
        JPanel bookmarkPanel = new JPanel();
        bookmarkPanel.setBackground(new Color(240, 248, 255));
        JLabel label = new JLabel("Your Bookmarks");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        bookmarkPanel.add(label);
        return bookmarkPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 5));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(400, 50));

        homeButton = createIconButton("icon/homePressed.png", "Home");
        homeButton.addActionListener(e -> {
            updateBottomIcons("Home");
            cardLayout.show(centerPanel, "Home");
            topPanel.setVisible(false);
        });
        bottomPanel.add(homeButton);

        searchButton = createIconButton("icon/searchIcon.png", "Search");
        searchButton.addActionListener(e -> {
            updateBottomIcons("Search");
            cardLayout.show(centerPanel, "Search");
            topPanel.setVisible(true);
        });
        bottomPanel.add(searchButton);

        addPostButton = createIconButton("icon/addPostIcon.png", "Add");
        bottomPanel.add(addPostButton);

        bookmarkButton = createIconButton("icon/bookmarkIcon.png", "Bookmark");
        bookmarkButton.addActionListener(e -> {
            updateBottomIcons("Bookmark");
            cardLayout.show(centerPanel, "Bookmark");
            topPanel.setVisible(false); // 검색창 숨기기
        });
        bottomPanel.add(bookmarkButton);

        userButton = createIconButton("icon/userIcon.png", "User");
        bottomPanel.add(userButton);

        return bottomPanel;
    }

    private JButton createIconButton(String iconPath, String fallbackText) {
        JButton button;
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            button = new JButton(new ImageIcon(img));
        } catch (Exception e) {
            button = new JButton(fallbackText);
        }
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private void performSearch(String query) {
        if (query.isEmpty() || query.equals("Search")) {
            resultPanel.removeAll();
            JLabel noResultsLabel = new JLabel("No results found.");
            noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultPanel.add(noResultsLabel);
            resultPanel.revalidate();
            resultPanel.repaint();
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter", "root", "5268");
             PreparedStatement stmt = conn.prepareStatement(
                     query.startsWith("#") ? "SELECT * FROM post WHERE content LIKE ?" : "SELECT * FROM post WHERE user_id = ?")) {

            if (query.startsWith("#")) {
                stmt.setString(1, "%" + query + "%"); // 해시태그 검색
            } else {
                stmt.setString(1, query); // 사용자 ID 검색
            }

            ResultSet rs = stmt.executeQuery();
            resultPanel.removeAll(); // 기존 결과 삭제

            if (rs.next()) {
                JLabel resultTitle = new JLabel(query.startsWith("#") ? "Search Results for Hashtag:" : "Posts by User:");
                resultTitle.setFont(new Font("Arial", Font.BOLD, 14));
                resultTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
                resultPanel.add(resultTitle);

                do {
                    String userId = rs.getString("user_id");
                    String content = rs.getString("content");
                    JPanel postPanel = new JPanel();
                    postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
                    postPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    JLabel userLabel = new JLabel("User: " + userId);
                    userLabel.setFont(new Font("Arial", Font.BOLD, 12));
                    JLabel contentLabel = new JLabel(content);
                    contentLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                    postPanel.add(userLabel);
                    postPanel.add(contentLabel);
                    resultPanel.add(postPanel);
                } while (rs.next());
            } else {
                JLabel noResultsLabel = new JLabel("No results found.");
                noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                resultPanel.add(noResultsLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }


    private void updateBottomIcons(String activeTab) {
        homeButton.setIcon(new ImageIcon("icon/home" + (activeTab.equals("Home") ? "Active.png" : "Inactive.png")));
        searchButton.setIcon(new ImageIcon("icon/search" + (activeTab.equals("Search") ? "Active.png" : "Inactive.png")));
        bookmarkButton.setIcon(new ImageIcon("icon/bookmark" + (activeTab.equals("Bookmark") ? "Active.png" : "Inactive.png")));
    }


    public static void main(String[] args) {
        new TwitterSearch();
    }
}

