import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TwitterSearch extends JPanel {
    private static TwitterSearch instance;
    private CardLayout cardLayout;
    private JPanel centerPanel;
    private JTextField searchField;
    private JPanel resultPanel;
    private JPanel topPanel;
    private JPanel searchPanel;

    public TwitterSearch() {
        instance = this;

        setLayout(new BorderLayout());
        JPanel northPanel = new JPanel(new BorderLayout());

        topPanel = new TopPanel().topPanel("Search");
        northPanel.add(topPanel, BorderLayout.NORTH);

        searchPanel = createSearchBar();
        searchPanel.setVisible(true);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);

        // Post Container
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);
        centerPanel.add(createResultPanel(), "TwitterSearch");
        add(centerPanel, BorderLayout.CENTER);

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

        searchField.addActionListener(e -> performSearch(searchField.getText().trim()));

        searchPanel.add(iconLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        return searchPanel;
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

        dbConnect db = dbConnect.getInstance();

        try {
            resultPanel.removeAll();

            if (query.startsWith("#")) {
                // Hashtag search
                ResultSet rs = db.getPostsByHashtag(query);
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
            }
            else if (query.startsWith("@")) {
                // User ID search
                ResultSet rs = db.getPostsByUser(query.substring(1)); // Remove "@" from query
                if (rs.next()) {
                    String userId = rs.getString("id");

                    if(userId.equals(UserInfo.getInstance().getUserId())){
                        JOptionPane.showMessageDialog(null, "Don't search YOU!");
                        return;
                    }

                    JPanel userPanel = new JPanel(new BorderLayout());
                    userPanel.setBackground(Color.WHITE);
                    userPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                    userPanel.setMaximumSize(new Dimension(400, 50));

                    JLabel userLabel = new JLabel("@" + userId);
                    userLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    userLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 추가

                    JButton followButton = new JButton("Follow");
                    followButton.setFont(new Font("Arial", Font.PLAIN, 12));
                    followButton.setPreferredSize(new Dimension(80, 30)); // 버튼 크기 설정

                    boolean isCurrentlyFollowing = db.isFollowing(userId); // 현재 팔로우 상태 확인
                    followButton.setText(isCurrentlyFollowing ? "Unfollow" : "Follow");

                    followButton.addActionListener(e -> {
                        try {
                            db.toggleFollow(userId);
                            followButton.setText(db.isFollowing(userId) ? "Unfollow" : "Follow");
                            TwitterHome.getInstance().displayPosts();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    });

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

            resultPanel.revalidate();
            resultPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TwitterSearch getInstance() {
        if (instance == null) {
            instance = new TwitterSearch();
        }
        return instance;
    }
}