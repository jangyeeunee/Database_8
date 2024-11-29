import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Post extends JPanel {
    private String userId;
    private String content;
    private int id;
    private Timestamp createAt;

    private boolean isBookmarked;
    private boolean isLiked;

    private JLabel userNameLabel;
    private JLabel contentLabel;
    private JLabel createAtLabel;

    private ArrayList<Comment> comments; // To store comments
    private JPanel commentsContainer;   // Panel for displaying comments

    public Post(int id, String userId, String content, Timestamp createAt) {
        this.userId = userId;
        this.content = content;
        this.createAt = createAt;
        this.id = id;
        this.isBookmarked = dbConnect.getInstance().checkisbookmarked(id);
        this.isLiked = dbConnect.getInstance().checkisliked(id);
        comments = new ArrayList<>(); // Initialize comments list
        setupUI();
    }


    //Post UI
    private void setupUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setPreferredSize(new Dimension(400, 150)); // 전체 패널 높이

        // User profile panel
        JPanel userProfilePanel = new JPanel();
        userProfilePanel.setPreferredSize(new Dimension(80, 40)); // 프로필 영역 높이
        userProfilePanel.setBackground(Color.WHITE);
        userProfilePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Profile image
        JLabel profileImg = new JLabel(new ImageIcon(new ImageIcon("icon/profile.jpg")
                .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH))); // 프로필 이미지 크기
        userProfilePanel.add(profileImg);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel spaceLabel = new JLabel("");
        contentPanel.add(spaceLabel);
        spaceLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 내용 상단 공백 크기
        userNameLabel = new JLabel(userId);
        userNameLabel.setForeground(Color.BLACK);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        contentLabel = new JLabel("<html>" + content.replaceAll("\n", "<br>") + "</html>");
        contentLabel.setForeground(Color.BLACK);
        contentLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        createAtLabel = new JLabel(createAt.toString());
        createAtLabel.setFont(new Font("Arial", Font.BOLD, 11));
        createAtLabel.setForeground(Color.DARK_GRAY);

        contentPanel.add(userNameLabel);
        contentPanel.add(contentLabel);
        contentPanel.add(createAtLabel);

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(400, 100)); // 버튼과 댓글 패널 전체 높이

        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BorderLayout()); // BorderLayout으로 변경

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(400, 40)); // 버튼 영역 높이
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton likeButton = createIconButton(
                this.isLiked ? "icon/likePressed.png" : "icon/likeIcon.png"
        );

        likeButton.addActionListener(e -> {
            this.isLiked = !this.isLiked;
            if (!this.isLiked) {
                likeButton.setIcon(new ImageIcon(new ImageIcon("icon/likeIcon.png")
                        .getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH)));
                dbConnect db = dbConnect.getInstance();
                db.delLike(id);
            } else {
                likeButton.setIcon(new ImageIcon(new ImageIcon("icon/likePressed.png")
                        .getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH)));
                dbConnect db = dbConnect.getInstance();
                db.addLike(id);
            }
        });

        //JButton repostButton = createIconButton("icon/retweetIcon.png");
        JButton commentButton = createIconButton("icon/commentIcon.png");
        commentButton.addActionListener(e -> {
            // Open CommentWindow to add a new comment
            CommentWindow commentWindow = new CommentWindow(id, this); // Pass postId and Post instance
            commentWindow.setVisible(true); // Show the comment window
        });


        JButton bookmarkButton = createIconButton(
                isBookmarked? "icon/pressed.png" : "icon/bookmark.png"
        );

        bookmarkButton.addActionListener(e -> {
            this.isBookmarked = !this.isBookmarked;
            if (!this.isBookmarked) {
                bookmarkButton.setIcon(new ImageIcon(new ImageIcon("icon/bookmark.png")
                        .getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH)));
                dbConnect db = dbConnect.getInstance();
                db.delBookmark(id);
            } else {
                bookmarkButton.setIcon(new ImageIcon(new ImageIcon("icon/Pressed.png")
                        .getImage().getScaledInstance(25, 20, Image.SCALE_SMOOTH)));
                dbConnect db = dbConnect.getInstance();
                db.addBookmark(id);
            }
        });

        buttonsPanel.add(likeButton);
        //buttonsPanel.add(repostButton);
        buttonsPanel.add(commentButton);
        buttonsPanel.add(bookmarkButton);

        // Comments container panel
        commentsContainer = new JPanel();
        commentsContainer.setLayout(new BoxLayout(commentsContainer, BoxLayout.Y_AXIS));
        commentsContainer.setBackground(Color.LIGHT_GRAY);

        // Wrap commentsContainer with JScrollPane
        JScrollPane commentsScrollPane = new JScrollPane(commentsContainer);
        commentsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        commentsScrollPane.setPreferredSize(new Dimension(380, 100)); // 댓글 영역 높이 제한
        commentsScrollPane.setBorder(BorderFactory.createTitledBorder("Comments"));

        // Enable independent scrolling for commentsScrollPane
        commentsScrollPane.setWheelScrollingEnabled(true);
        commentsScrollPane.getVerticalScrollBar().setUnitIncrement(10); // 스크롤 단위 설정

        bottomPanel.add(buttonsPanel, BorderLayout.NORTH); // 버튼 섹션
        bottomPanel.add(commentsScrollPane, BorderLayout.CENTER); // 스크롤 가능한 댓글 섹션

        // Main panel layout
        add(userProfilePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateComments();
    }


    // Method to add a comment and update the UI
    public void addComment(String commentText) {
        Comment newComment = new Comment(commentText); // Create a new comment
        comments.add(newComment); // Add to comments list

        // Create a panel for the new comment
        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BorderLayout());
        commentPanel.setBackground(Color.WHITE);
        commentPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel commenterLabel = new JLabel(newComment.getCommenter());
        commenterLabel.setFont(new Font("Arial", Font.BOLD, 12));
        commenterLabel.setForeground(Color.BLUE);

        JLabel commentLabel = new JLabel("<html>" + commentText.replaceAll("\n", "<br>") + "</html>");
        commentLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        commentLabel.setForeground(Color.BLACK);

        commentPanel.add(commenterLabel, BorderLayout.NORTH);
        commentPanel.add(commentLabel, BorderLayout.CENTER);

        commentsContainer.add(commentPanel);
        commentsContainer.revalidate(); // Update layout
        commentsContainer.repaint();   // Refresh panel
    }

    void updateComments() {
        commentsContainer.removeAll(); // 기존 댓글 제거
        List<String> comments = dbConnect.getInstance().getCommentsByPostId(id); // 현재 포스트의 댓글 가져오기
        for (String comment : comments) {
            addComment(comment);
        }
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


    // Comment class to store individual comments
    private static class Comment {
        private String commenter;
        private String text;

        public Comment(String text) {
            this.commenter = UserInfo.getInstance().getUserId();
            this.text = text;
        }

        public String getCommenter() {
            return commenter;
        }

        public String getText() {
            return text;
        }
    }
}
