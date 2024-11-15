import javax.swing.*;
import java.awt.*;

public class BookmarkPage extends JPanel {
    public BookmarkPage(TwitterHome parent) {
        setLayout(new BorderLayout()); // Set layout for the entire panel

        // Create the content panel for bookmarks
        JPanel bookmarkPage = new JPanel();
        bookmarkPage.setLayout(new BoxLayout(bookmarkPage, BoxLayout.Y_AXIS));
        bookmarkPage.setBackground(new Color(230, 245, 255));

        // Add dummy bookmark items
        for (int i = 1; i <= 20; i++) {
            JPanel postPanel = new JPanel();
            postPanel.setLayout(new BorderLayout());
            postPanel.setPreferredSize(new Dimension(350, 70));
            postPanel.setMaximumSize(new Dimension(350, 70));
            postPanel.setBackground(Color.WHITE);
            postPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel postLabel = new JLabel("Post #" + i + ": This is a sample post.");
            postLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            postPanel.add(postLabel, BorderLayout.CENTER);

            bookmarkPage.add(Box.createVerticalStrut(10)); // Add spacing between posts
            bookmarkPage.add(postPanel);
        }

        // Wrap the content panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(bookmarkPage);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add the scroll pane to this panel
        add(scrollPane, BorderLayout.CENTER);
    }
}
