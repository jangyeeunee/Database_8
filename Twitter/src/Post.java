import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class Post extends JPanel {
    private final String userId;
    private final String content;
    private final Timestamp createAt;

    public Post(String userId, String content, Timestamp createAt) {
        this.userId = userId;
        this.content = content;
        this.createAt = createAt;

        int calculatedHeight = calculateHeight(content);

        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        setPreferredSize(new Dimension(360, calculatedHeight));
        setMaximumSize(new Dimension(360, calculatedHeight));
        setMinimumSize(new Dimension(360, calculatedHeight));

        JPanel userProfile = new JPanel();
        userProfile.setPreferredSize(new Dimension(80, calculatedHeight));
        userProfile.setBackground(Color.WHITE);
        userProfile.setLayout(new FlowLayout(FlowLayout.CENTER));

        ImageIcon originalIcon = new ImageIcon("icon/profile.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel profileImg = new JLabel(scaledIcon);
        userProfile.add(profileImg);

        JPanel userContent = new JPanel();
        userContent.setBackground(Color.WHITE);
        userContent.setLayout(new BoxLayout(userContent, BoxLayout.Y_AXIS));

        JLabel userName = new JLabel(userId);
        JLabel userPost = new JLabel("<html>" + content.replaceAll("\n", "<br>") + "</html>");
        userContent.add(userName);
        userContent.add(userPost);

        add(userProfile, BorderLayout.WEST);
        add(userContent, BorderLayout.CENTER);

        JPanel downDoc = new JPanel();
        downDoc.setPreferredSize(new Dimension(400, 40));
        downDoc.setBackground(Color.WHITE);
        downDoc.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

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

        downDoc.add(likeButton);
        downDoc.add(repostButton);
        downDoc.add(commentButton);
        downDoc.add(bookmarkButton);

        add(downDoc, BorderLayout.SOUTH);
    }

    // Getter for createAt
    public Timestamp getCreateAt() {
        return createAt;
    }

    private int calculateHeight(String content) {
        int baseHeight = 80;
        int charsPerLine = 50;
        int lineHeight = 20;

        int contentLines = (int) Math.ceil((double) content.length() / charsPerLine);
        return baseHeight + (contentLines * lineHeight);
    }

    private JButton createIconButton(String iconPath) {
        JButton button;

        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledImage = icon.getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            button = new JButton(scaledIcon);
        } catch (Exception e) {
            button = new JButton();
            System.out.println("Icon not found: " + iconPath);
        }

        button.setPreferredSize(new Dimension(40, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        return button;
    }
}
