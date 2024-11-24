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

        topPanel = createSearchBar();
        topPanel.setVisible(false);
        add(topPanel, BorderLayout.NORTH);

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
                
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search");
                    searchField.setForeground(Color.GRAY);
                }
              
            }
        });

        // Searching by enterkey
        searchField.addActionListener(e -> performSearch(searchField.getText().trim()));

        searchPanel.add(iconLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        return searchPanel;
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(new Color(240, 248, 255));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        JLabel welcomeLabel = new JLabel("Follow");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(29, 161, 242)); 
        headerPanel.add(welcomeLabel);

        homePanel.add(headerPanel, BorderLayout.NORTH);

        return homePanel;
    }

    private JPanel createResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setBackground(Color.WHITE);
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

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter", "root", "5268")) {
            resultPanel.removeAll(); // Clear previous results

            if (query.startsWith("#")) {
                // Hashtag search
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT user_id, content FROM post WHERE content LIKE ?")) {
                    stmt.setString(1, "%" + query + "%");
                    ResultSet rs = stmt.executeQuery();

                    // If results exist
                    if (rs.isBeforeFirst()) {
                      
                        JLabel resultTitle = new JLabel("Search Results: " + query);
                        resultTitle.setFont(new Font("Arial", Font.BOLD, 14));
                        resultTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                        resultPanel.add(resultTitle);
                        resultPanel.add(Box.createVerticalStrut(10)); // 간격 추가

                        // 검색 결과 순회
                        while (rs.next()) {
                            String userId = rs.getString("user_id");
                            String content = rs.getString("content");

                            JPanel postPanel = new JPanel(new BorderLayout());
                            postPanel.setBackground(Color.WHITE); 
                            postPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); 
                            postPanel.setMaximumSize(new Dimension(600, 80)); 

                            JLabel userLabel = new JLabel("User: " + userId);
                            userLabel.setFont(new Font("Arial", Font.BOLD, 12));
                            userLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 

                            JLabel contentLabel = new JLabel("<html>" + content + "</html>"); 
                            contentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                            contentLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                            postPanel.add(userLabel, BorderLayout.NORTH);
                            postPanel.add(contentLabel, BorderLayout.CENTER);

                            resultPanel.add(postPanel);
                            resultPanel.add(Box.createVerticalStrut(10));
                        }
                    } else {
                        JLabel noResultsLabel = new JLabel("No posts found for this hashtag.");
                        noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                        noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        resultPanel.add(noResultsLabel);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            else if (query.startsWith("@")) {
                // User ID search
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT id FROM user WHERE id = ?")) {
                    stmt.setString(1, query.substring(1)); // Remove "@" from query
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String userId = rs.getString("id");

                        JPanel userPanel = new JPanel(new BorderLayout());
                        userPanel.setBackground(Color.WHITE); 
                        userPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); 
                        userPanel.setMaximumSize(new Dimension(400, 50));

                        JLabel userLabel = new JLabel("@" + userId);
                        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        userLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 추가

                        JButton followButton = new JButton(isFollowing(conn, userId) ? "Unfollow" : "Follow");
                        followButton.setFont(new Font("Arial", Font.PLAIN, 12));
                        followButton.setPreferredSize(new Dimension(80, 30)); // 버튼 크기 설정
                        followButton.addActionListener(e -> toggleFollow(conn, userId, followButton));

                        userPanel.add(userLabel, BorderLayout.WEST); // 왼쪽 정렬
                        userPanel.add(followButton, BorderLayout.EAST); // 오른쪽 정렬

                        resultPanel.add(userPanel);
                        resultPanel.add(Box.createVerticalStrut(10)); 
                    } else {
                        JLabel noResultsLabel = new JLabel("User not found.");
                        noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                        noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        resultPanel.add(noResultsLabel);
                    }
                }
            }

            resultPanel.revalidate();
            resultPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
    private boolean isFollowing(Connection conn, String userId) { 
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM follow WHERE follow_id = ? AND followed_id = ?")) {
            stmt.setString(1, UserInfo.getInstance().getUserId()); // 로그인 사용자 ID
            stmt.setString(2, userId); // 검색된 사용자 ID
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // 결과가 있으면 팔로우 상태
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void toggleFollow(Connection conn, String userId, JButton followButton) {
        try {
            if (isFollowing(conn, userId)) {
                // 이미 팔로우 중인 경우 -> 언팔로우
                try (PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM follow WHERE follow_id = ? AND followed_id = ?")) {
                    stmt.setString(1, UserInfo.getInstance().getUserId()); // 로그인 사용자 ID
                    stmt.setString(2, userId); // 검색된 사용자 ID
                    stmt.executeUpdate();
                    followButton.setText("Follow");
                    JOptionPane.showMessageDialog(followButton, "You have unfollowed this user.", "Notification", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // 팔로우 중이 아닌 경우 -> 팔로우
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO follow (follow_id, followed_id) VALUES (?, ?)")) {
                    stmt.setString(1, UserInfo.getInstance().getUserId()); // 로그인 사용자 ID
                    stmt.setString(2, userId); // 검색된 사용자 ID
                    stmt.executeUpdate();
                    followButton.setText("Unfollow");
                    JOptionPane.showMessageDialog(followButton, "You are now following this user.", "Notification", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateBottomIcons(String activeTab) {
        homeButton.setIcon(new ImageIcon("icon/home" + (activeTab.equals("Home") ? "Active.png" : "Inactive.png")));
        searchButton.setIcon(new ImageIcon("icon/search" + (activeTab.equals("Search") ? "Active.png" : "Inactive.png")));
        bookmarkButton.setIcon(new ImageIcon("icon/bookmark" + (activeTab.equals("Bookmark") ? "Active.png" : "Inactive.png")));
    }


    
}

