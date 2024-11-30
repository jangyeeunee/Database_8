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
    private JPanel postsPanel;

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
        
     //top   TopPanel topPanel = new TopPanel();
     //top JPanel topPanelUI = topPanel.topPanel("User");

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

        // Tabs Panel
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBackground(Color.WHITE);

        JPanel tabButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton myPostsTab = new JButton("My Posts");
        JButton likedPostsTab = new JButton("Liked Posts");

        for (JButton tab : new JButton[]{myPostsTab, likedPostsTab}) {
            tab.setBackground(Color.WHITE);
            tab.setForeground(new Color(29, 161, 242));
            tab.setFont(new Font("Arial", Font.BOLD, 14));
            tab.setFocusPainted(false);
        }

        tabButtonsPanel.add(myPostsTab);
        tabButtonsPanel.add(likedPostsTab);
        tabPanel.add(tabButtonsPanel, BorderLayout.NORTH);

        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);

        // Initial load of My Posts
        loadPosts("myPosts");

        // Tab Switching Logic
        myPostsTab.addActionListener(e -> {
            postsPanel.removeAll();
            loadPosts("myPosts");
        });

        likedPostsTab.addActionListener(e -> {
            postsPanel.removeAll();
            loadPosts("likedPosts");
        });

        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        tabPanel.add(scrollPane, BorderLayout.CENTER);

        add(tabPanel, BorderLayout.CENTER);

    }

    private void loadPosts(String type) {
        dbConnect db = dbConnect.getInstance();
        Post[] postsArray;

        if (type.equals("myPosts")) {
            postsArray = db.getUserPosts(userId); // Load user's posts
        } else {
            postsArray = db.getUserLikedPosts(userId); // Load liked posts
        }

        List<Post> posts = Arrays.asList(postsArray);
        displayPosts(posts);
    }

    private void displayPosts(List<Post> posts) {
        postsPanel.removeAll(); // Clear previous posts

        if (posts == null || posts.isEmpty()) {
            JLabel noPostsLabel = new JLabel("No posts found.");
            noPostsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            noPostsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            postsPanel.add(noPostsLabel);
        } else {
            for (Post post : posts) {
                if (post != null) {
                    post.setMaximumSize(new Dimension(400, 150)); // 고정 크기 설정
                    post.setPreferredSize(new Dimension(400, 150)); // 고정 크기 설정
                    postsPanel.add(post);
                }
            }
        }

        postsPanel.revalidate();
        postsPanel.repaint();
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
