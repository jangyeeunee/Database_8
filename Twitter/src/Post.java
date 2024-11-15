import javax.swing.*;
import java.awt.*;

public class Post extends JPanel {
    public Post() {
        // Use BorderLayout for the main Post panel
        setLayout(new BorderLayout(0,0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Left panel with width 80 and height 150
        JPanel userProfile = new JPanel();
        userProfile.setPreferredSize(new Dimension(80, 150));
        userProfile.setBackground(Color.WHITE);
        userProfile.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Load and resize the profile image
        ImageIcon originalIcon = new ImageIcon("icon/profile.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Set desired width and height
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel profileImg = new JLabel(scaledIcon);
        userProfile.add(profileImg);

        // Right panel with width 320 and height 150
        JPanel userContent = new JPanel();
        userContent.setPreferredSize(new Dimension(320, 150));
        userContent.setBackground(Color.WHITE);
        userContent.setLayout(new GridLayout(2,1));

        // Add a label to the right panel
        JLabel userName = new JLabel("user Name");
        userContent.add(userName);

        JLabel userPost = new JLabel("UserContent");
        userContent.add(userPost);

        // Panel to hold left and right panels side by side
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        sidePanel.setBackground(Color.WHITE);

        // Add left and right panels to the sidePanel
        sidePanel.add(userProfile);
        sidePanel.add(userContent);

        // Add the sidePanel to the top of the main panel (using BorderLayout)
        add(sidePanel, BorderLayout.NORTH);

        // Create and add the bottom panel
        JPanel downDoc = new JPanel();
        downDoc.setPreferredSize(new Dimension(400, 30));
        downDoc.setBackground(Color.WHITE);
        downDoc.setLayout(new GridLayout(1, 4, 60, 0));




        // Create and add the button on downDoc
        JButton likeButton = new JButton("🩷");


        JButton repostButton = new JButton("🔃");


        JButton commentButton = new JButton("💬");


        JButton bookmarkButton = new JButton("🔖");


        downDoc.add(likeButton);
        downDoc.add(repostButton);
        downDoc.add(commentButton);
        downDoc.add(bookmarkButton);

        // Add the bottom panel below the left and right panels
        add(downDoc, BorderLayout.SOUTH);
    }
}
