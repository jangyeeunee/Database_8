import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static MainFrame instance;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;

    public MainFrame() {
        setTitle("Twitter Application");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add pages
        mainPanel.add(TwitterHome.getInstance(), "Home");
        mainPanel.add(BookmarkPage.getInstance(), "Bookmark");
        mainPanel.add(TwitterSearch.getInstance(),"TwitterSearch");
        mainPanel.add(TwitterUserPage.getInstance(), "User");


        // Add Bottom Panel
        add(BottomPanel.getInstance().BottomPanel(), BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void showPage(String pageName) {
        cardLayout.show(mainPanel, pageName);
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }
}
