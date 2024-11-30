import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommentWindow extends JFrame {
    private final int postId;
    private final Post post; // Post 객체를 받아와 UI 업데이트

    public CommentWindow(int postId, Post post) {
        this.postId = postId; // Store the post ID
        this.post = post;     // Post 객체 저장
        setupUI();
    }

    private void setupUI() {
        setTitle("Add Comment");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Comment input area
        JTextArea commentTextArea = new JTextArea();
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String commentText = commentTextArea.getText().trim();
                if (!commentText.isEmpty()) {
                    // Call DB function to add comment
                    dbConnect db = dbConnect.getInstance();
                    boolean success = db.addComment(UserInfo.getInstance().getUserId(), postId, commentText); // Add comment to DB
                    if (success) {
                        JOptionPane.showMessageDialog(CommentWindow.this, "Comment added successfully!");
                        post.updateComments(); // Post UI update
                        dispose(); // Close the window
                    } else {
                        JOptionPane.showMessageDialog(CommentWindow.this, "Failed to add comment.");
                    }
                } else {
                    JOptionPane.showMessageDialog(CommentWindow.this, "Comment cannot be empty.");
                }
            }
        });
        add(submitButton, BorderLayout.SOUTH);
    }
}