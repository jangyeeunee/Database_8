import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class Post extends JPanel {
    private String userId;
    private String content;
    private int id;
    private int repost_id;
    private Timestamp createAt;

    private JLabel userNameLabel;
    private JLabel contentLabel;
    private JLabel createAtLabel;

    public Post(int id, String userId, String content, int repost_id,Timestamp createAt) {
        this.userId = userId;
        this.content = content;
        this.createAt = createAt;
        this.id = id;
        this.repost_id = repost_id;
    }

    private void setupUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setPreferredSize(new Dimension(360, calculateHeight(content)));

        // User profile panel
        JPanel userProfilePanel = new JPanel();
        userProfilePanel.setPreferredSize(new Dimension(80, calculateHeight(content)));
        userProfilePanel.setBackground(Color.WHITE);
        userProfilePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Profile image
        JLabel profileImg = new JLabel(new ImageIcon(new ImageIcon("icon/profile.jpg")
                .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        userProfilePanel.add(profileImg);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel spaceLabel = new JLabel("");
        contentPanel.add(spaceLabel);
        spaceLabel.setFont(new Font("Arial",Font.BOLD,20));
        userNameLabel = new JLabel(userId);
        userNameLabel.setForeground(Color.BLACK);
        userNameLabel.setFont(new Font("Arial",Font.BOLD,15));
        contentLabel = new JLabel("<html>" + content.replaceAll("\n", "<br>") + "</html>");
        contentLabel.setForeground(Color.BLACK);
        contentLabel.setFont(new Font("맑은 고딕",Font.PLAIN,15));
        createAtLabel = new JLabel(createAt.toString());
        createAtLabel.setFont(new Font("Arial", Font.BOLD,11));
        createAtLabel.setForeground(Color.darkGray);


        contentPanel.add(userNameLabel);
        contentPanel.add(contentLabel);
        contentPanel.add(createAtLabel);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(360, 40));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton likeButton = createIconButton("icon/likeIcon.png");
        final boolean[] isLiked = {false};

        likeButton.addActionListener(e -> {
            if (isLiked[0]) {
                likeButton.setIcon(new ImageIcon(new ImageIcon("icon/likeIcon.png")
                        .getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH)));
            } else {
                likeButton.setIcon(new ImageIcon(new ImageIcon("icon/likePressed.png")
                        .getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH)));
            }
            isLiked[0] = !isLiked[0];
        });
        JButton repostButton = createIconButton("icon/retweetIcon.png");
        JButton commentButton = createIconButton("icon/commentIcon.png");
        JButton bookmarkButton = createIconButton("icon/bookmark.png");
        final boolean[] isBookmarked = {false};

        bookmarkButton.addActionListener(e -> {
            if (isBookmarked[0]) {
                bookmarkButton.setIcon(new ImageIcon(new ImageIcon("icon/bookmark.png")
                        .getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH)));
            } else {
                bookmarkButton.setIcon(new ImageIcon(new ImageIcon("icon/Pressed.png")
                        .getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH)));
            }
            isBookmarked[0] = !isBookmarked[0];
        });

        bottomPanel.add(likeButton);
        bottomPanel.add(repostButton);
        bottomPanel.add(commentButton);
        bottomPanel.add(bookmarkButton);

        add(userProfilePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void postUpdate(String userId, String content, Timestamp createAt) {
        this.userId = userId;
        this.content = content;
        this.createAt = createAt;

        // Update UI components
        userNameLabel.setText(userId);
        contentLabel.setText("<html>" + content.replaceAll("\n", "<br>") + "</html>");
        createAtLabel.setText(createAt.toString());

        // Repaint and revalidate to ensure the UI updates
        repaint();
        revalidate();
    }

    private JButton createIconButton(String iconPath) {
        ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH));
        JButton button = new JButton(icon);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(40, 40));
        return button;
    }

    private int calculateHeight(String content) {
        int baseHeight = 80;
        int charsPerLine = 50;
        int lineHeight = 20;
        int contentLines = (int) Math.ceil((double) content.length() / charsPerLine);
        return baseHeight + (contentLines * lineHeight);
    }

    public Timestamp getCreateAt() {
        return createAt;
    }
}




