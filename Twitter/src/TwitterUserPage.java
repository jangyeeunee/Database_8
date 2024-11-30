import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Arrays;

public class TwitterUserPage extends JPanel {
    private String userId;
    private String userName;
    private int followingCount;
    private int followerCount;
    private boolean isCurrentUser;
    private JPanel postsPanel; // 게시물 표시 패널
    private JButton myPostsTab, likedPostsTab;

    public TwitterUserPage() {
        UserInfo userInfo = UserInfo.getInstance();
        this.userId = userInfo.getUserId();
        this.userName = userInfo.getUserFirstName() + " " + userInfo.getUserLastName();
        this.followingCount = userInfo.getFollowingCount();
        this.followerCount = userInfo.getFollowerCount();
        this.isCurrentUser = true;

        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // TopPanel (for title or header)
       //top TopPanel topPanel = new TopPanel();
      //top  JPanel topPanelUI = topPanel.topPanel("User");

        // Profile Panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        profilePanel.setBackground(Color.WHITE);

        // Profile Info Panel
        JPanel profileInfoPanel = new JPanel();
        profileInfoPanel.setLayout(new BoxLayout(profileInfoPanel, BoxLayout.Y_AXIS));
        profileInfoPanel.setBackground(Color.WHITE);

        JLabel displayNameLabel = new JLabel(userName);
        displayNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel userIdLabel = new JLabel("@" + userId);
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userIdLabel.setForeground(Color.GRAY);

        JLabel followingLabel = new JLabel("Following: " + followingCount + "  |  Followers: " + followerCount);
        followingLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        profileInfoPanel.add(displayNameLabel);
        profileInfoPanel.add(userIdLabel);
        profileInfoPanel.add(followingLabel);

        // Add Profile Info Panel to Profile Panel
        profilePanel.add(profileInfoPanel, BorderLayout.CENTER);

        // Create main panel to hold TopPanel and ProfilePanel vertically
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Add TopPanel and ProfilePanel to mainPanel
       //top mainPanel.add(topPanelUI);
        mainPanel.add(profilePanel);

        // Add mainPanel to the North of the layout (so both panels are displayed at the top)
        add(mainPanel, BorderLayout.NORTH);

        // Add "Edit Info" button if it's the current user
        if (isCurrentUser) {
            JButton editButton = new JButton("Edit Info");
            editButton.setBackground(new Color(29, 161, 242));
            editButton.setForeground(Color.BLACK);
            editButton.setFont(new Font("Arial", Font.BOLD, 14));
            editButton.addActionListener(e -> handleEditButtonClick());
            profilePanel.add(editButton, BorderLayout.EAST);  // Position button to top-right of profilePanel
        }

        // Posts Panel (directly add scrollPane)
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);

        // Load "My Posts" initially
        loadPosts("myPosts");

        // ScrollPane for posts
        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add scrollPane to main layout (center section)
        add(scrollPane, BorderLayout.CENTER);

    }


    private void loadPosts(String type) {
        dbConnect db = dbConnect.getInstance();
        // type에 따라 사용자의 게시물 또는 좋아요한 게시물 가져오기
        Post[] postsArray = (type.equals("myPosts")) 
            ? db.getUserPosts(userId) // Load user's posts
            : db.getUserLikedPosts(userId); // Load liked posts

        List<Post> posts = Arrays.asList(postsArray);

        displayPosts(posts);
    }
    private void displayPosts(List<Post> posts) {
        postsPanel.removeAll(); // Clear existing posts

        if (posts == null || posts.isEmpty()) {
            JLabel noPostsLabel = new JLabel("No posts found.");
            noPostsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            noPostsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            postsPanel.add(noPostsLabel);
        } else {
            for (Post post : posts) {
                if (post != null) {
                    post.setMaximumSize(new Dimension(400, 150)); // Fixed size
                    post.setPreferredSize(new Dimension(400, 150)); // Fixed size
                    postsPanel.add(post);
                }
            }
        }

        postsPanel.revalidate();
        postsPanel.repaint();
    }

    private void setActiveTab(JButton activeTab, JButton inactiveTab) {
        activeTab.setBackground(new Color(29, 161, 242));
        inactiveTab.setBackground(Color.WHITE);
    }

    private void handleEditButtonClick() {
        // Info edit fields (same as before)
        JPasswordField newPasswordField = new JPasswordField(20); 
        JTextField newNameField = new JTextField(UserInfo.getInstance().getUserFirstName() + " " + UserInfo.getInstance().getUserLastName());
        JTextField newEmailField = new JTextField(UserInfo.getInstance().getUserEmail());
        JTextField newPhoneField = new JTextField(UserInfo.getInstance().getUserPhone());
        JTextField newBirthField = new JTextField(UserInfo.getInstance().getUserBirth());
        JTextField newGenderField = new JTextField(UserInfo.getInstance().getUserGender());
        
        Object[] fields = {
            "New Password:", newPasswordField,
            "Name:", newNameField,
            "Email:", newEmailField,
            "Phone Number:", newPhoneField,
            "Birth Date:", newBirthField,
            "Gender:", newGenderField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Edit User Info", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String newPassword = new String(newPasswordField.getPassword()); 
            String newName = newNameField.getText();
            String newEmail = newEmailField.getText();
            String newPhone = newPhoneField.getText();
            String newBirth = newBirthField.getText();
            String newGender = newGenderField.getText();

            String[] nameParts = newName.split(" ");
            String firstName = nameParts.length > 0 ? nameParts[0] : "";  // First name
            String lastName = nameParts.length > 1 ? nameParts[1] : "";   // Last name

            UserInfo.getInstance().setUserFirstName(firstName);
            UserInfo.getInstance().setUserLastName(lastName);
            UserInfo.getInstance().setUserEmail(newEmail);
            UserInfo.getInstance().setUserPhone(newPhone);
            UserInfo.getInstance().setUserBirth(newBirth);
            UserInfo.getInstance().setUserGender(newGender);

            dbConnect db = dbConnect.getInstance();
            if (db.updateUserInfo(UserInfo.getInstance().getUserId(), newPassword, firstName, lastName, newEmail, newPhone, newBirth, newGender)) {
                JOptionPane.showMessageDialog(this, "User information updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user information.");
            }
        }
    }
}
