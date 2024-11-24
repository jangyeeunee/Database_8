import javax.swing.*;
import java.awt.*; 
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class TwitterUserPage extends JFrame {
    private String userId;
    private String userName;
    private int followingCount;
    private int followerCount;
    private boolean isCurrentUser;

    public TwitterUserPage() {
        UserInfo userInfo = UserInfo.getInstance();
        this.userId = userInfo.getUserId();
        this.userName = userInfo.getUserFirstName() + " " + userInfo.getUserLastName();
        this.followingCount = 10; 
        this.followerCount = 100; 

        setTitle("Twitter User Profile");
        setSize(500, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(Color.WHITE);

        JLabel displayNameLabel = new JLabel(userName);
        displayNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel userIdLabel = new JLabel("@" + userId);
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userIdLabel.setForeground(Color.GRAY);

        JLabel followingLabel = new JLabel("Following: " + followingCount + "  |  Followers: " + followerCount);
        followingLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        userInfoPanel.add(displayNameLabel);
        userInfoPanel.add(userIdLabel);
        userInfoPanel.add(followingLabel);

        topPanel.add(userInfoPanel, BorderLayout.CENTER);

        if (!isCurrentUser) {
            JButton followButton = new JButton("Follow");
            followButton.setBackground(new Color(29, 161, 242));
            followButton.setForeground(Color.WHITE);
            followButton.setFont(new Font("Arial", Font.BOLD, 14));
            topPanel.add(followButton, BorderLayout.EAST);
        }

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout());
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

        JPanel postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);

        dbConnect db = dbConnect.getInstance();
        Post[] posts = db.getFollowingPost(); 
        if (posts.length == 0) {
            postsPanel.add(new JLabel("No posts to display.")); 
        } else {
            for (Post post : posts) {
                JPanel postPanel = new JPanel(new BorderLayout());
                postPanel.setBackground(Color.WHITE);
                postPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                
                //post와 연동할 부분. 한 패널로 스크롤 가능하게 할 것.
               // JLabel postLabel = new JLabel(post.getContent());
               // postLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                //postPanel.add(postLabel, BorderLayout.CENTER);
                postsPanel.add(postPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tabPanel, BorderLayout.CENTER);
//하단, 이미지로 교체예정?
        JPanel bottomNav = new JPanel();
        bottomNav.setLayout(new GridLayout(1, 4));
        bottomNav.setBackground(Color.WHITE);

        JButton homeButton = new JButton("Home");
        JButton searchButton = new JButton("Search");
        JButton bookmarkButton = new JButton("Bookmark");
        JButton postButton = new JButton("Post");

        for (JButton navButton : new JButton[]{homeButton, searchButton, bookmarkButton, postButton}) {
            navButton.setFont(new Font("Arial", Font.BOLD, 12));
            navButton.setFocusPainted(false);
            bottomNav.add(navButton);
        }

        mainPanel.add(bottomNav, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TwitterUserPage());
    }
}
