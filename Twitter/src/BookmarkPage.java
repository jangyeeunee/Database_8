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


        // Wrap the content panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(bookmarkPage);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add the scroll pane to this panel
        add(scrollPane, BorderLayout.CENTER);
    }
}
