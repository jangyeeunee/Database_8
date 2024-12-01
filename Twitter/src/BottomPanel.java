import javax.swing.*;
import java.awt.*;

public class BottomPanel {
    private static BottomPanel instance;
    public JButton homeButton;
    public JButton bookmarkButton;
    public JButton searchButton;
    private JButton userButton;
    private JButton addPostButton;

    // Private constructor for Singleton
    private BottomPanel() {
        initializeButtons();
    }

    public JPanel BottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 5));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(400, 50));

        // Add Home Button
        bottomPanel.add(homeButton);
        bottomPanel.add(searchButton);
        bottomPanel.add(addPostButton);
        bottomPanel.add(bookmarkButton);
        bottomPanel.add(userButton);

        return bottomPanel;
    }

    private void initializeButtons() {
        // Initialize Home Button
        homeButton = createIconButton("icon/homeIcon.png");
        homeButton.addActionListener(e -> {
            updateIcons("Home");
            MainFrame.getInstance().showPage("Home");
        });

        //Initialize Search Button
        searchButton = createIconButton("icon/searchIcon.png");
        searchButton.addActionListener(e -> {
            updateIcons("Search");
            MainFrame.getInstance().showPage("TwitterSearch");
        });


        // Initialize addPost Button
        addPostButton = createIconButton("icon/addPostIcon.png");
        addPostButton.addActionListener(e -> {
            updateIcons("addPost");
            createPost createdPost = new createPost();
            createdPost.setVisible(true);
        });


        // Initialize Bookmark Button
        bookmarkButton = createIconButton("icon/bookmarkIcon.png");
        bookmarkButton.addActionListener(e -> {
            updateIcons("Bookmark");
            MainFrame.getInstance().showPage("Bookmark");
        });


        userButton = createIconButton("icon/userIcon.png");
        userButton.addActionListener(e -> {
            updateIcons("User");
            dbConnect db = dbConnect.getInstance();
            db.updateFollowStats(UserInfo.getInstance().getUserId());
            TwitterUserPage userpage = TwitterUserPage.getInstance();
            userpage.setFollowerCount(UserInfo.getInstance().getFollowerCount());
            userpage.setFollowingCount(UserInfo.getInstance().getFollowingCount());
            userpage.updateUserInfoUI();
            MainFrame.getInstance().showPage("User"); // Show User Page
        });
    }

    public static JButton createIconButton(String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image iconImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(iconImage);

        JButton button = new JButton(resizedIcon);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        return button;
    }

    public void updateIcons(String currentPage) {
        // Update Home Button Icon
        ImageIcon homeIcon = new ImageIcon(currentPage.equals("Home") ? "icon/homePressed.png" : "icon/homeIcon.png");
        Image homeImage = homeIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        homeButton.setIcon(new ImageIcon(homeImage));

        // Update Bookmark Button Icon
        ImageIcon bookmarkIcon = new ImageIcon(currentPage.equals("Bookmark") ? "icon/bookmarkPressed.png" : "icon/bookmarkIcon.png");
        Image bookmarkImage = bookmarkIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        bookmarkButton.setIcon(new ImageIcon(bookmarkImage));

        //Update Search Button Icon
        ImageIcon searchIcon = new ImageIcon(currentPage.equals("Search") ? "icon/searchPressed.png" : "icon/searchIcon.png");
        Image searchImage = searchIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        searchButton.setIcon(new ImageIcon(searchImage));

        //Update User Button Icon
        ImageIcon userIcon = new ImageIcon(currentPage.equals("User") ? "icon/userPressed.png" : "icon/userIcon.png");
        Image userImage = userIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        userButton.setIcon(new ImageIcon(userImage));


    }

    public static BottomPanel getInstance() {
        if (instance == null) {
            instance = new BottomPanel();
        }
        return instance;
    }
}
