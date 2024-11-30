import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    private JPanel panel;
    private JLabel label; // Dynamic label for updates
    private static TopPanel instance;

    public TopPanel() {
        // Initialize components
        label = new JLabel();
        label.setForeground(Color.DARK_GRAY); // Text color
        label.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 13));
        label.setHorizontalAlignment(SwingConstants.CENTER); // Center align
    }

    public JPanel topPanel(String labelText) {
        // Main panel
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(400, 80));

        // Twitter Icon
        JLabel twitterIconLabel = new JLabel();
        ImageIcon twitterIcon = new ImageIcon("icon/twitterIcon.png");
        Image twitterImage = twitterIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        twitterIconLabel.setIcon(new ImageIcon(twitterImage));
        twitterIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(twitterIconLabel, BorderLayout.CENTER);

        // Label Panel (Always added)
        JPanel labelPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);

                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);
            }
        };
        labelPanel.setPreferredSize(new Dimension(400, 30));
        labelPanel.setOpaque(false);

        // Add label
        label.setText(labelText); // Dynamic text based on input
        labelPanel.add(label); // Add label to labelPanel
        panel.add(labelPanel, BorderLayout.SOUTH); // Add labelPanel to panel

        // Force revalidation and repaint to ensure updates
        panel.revalidate();
        panel.repaint();

        return panel;
    }



    public static TopPanel getInstance() {
        if (instance == null) {
            instance = new TopPanel();
        }
        return instance;
    }
}
