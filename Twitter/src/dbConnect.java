import javax.swing.*;
import java.sql.Statement;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

public class dbConnect {
    private String actionDb;
    private String Id;
    private String Password;
    private JFrame parentFrame;

    public dbConnect(String actionDb, String Id, String Password, JFrame parentFrame) {
        this.actionDb = actionDb;
        this.Id = Id;
        this.Password = Password;
        this.parentFrame = parentFrame;
    }


    public void getActionDb() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/TWITTER";
            String user = "root", passwd = "wldmsdl7715";
            con = DriverManager.getConnection(url, user, passwd);
            System.out.println(con);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            if (this.actionDb.equals("login")) {
                stmt = con.createStatement();
                String s1 = "select * from user where id = \"" + this.Id + "\" and pwd = \"" + this.Password + "\"";
                rs = stmt.executeQuery(s1);
                if(rs.next()) {
                    System.out.println("login success");
                    UserInfo.getInstance().setUserId(this.Id);
                    String name = rs.getString("first_name")+" "+rs.getString("last_name");
                    System.out.println("user name : " + name);
                    // TwitterHome 생성 후 홈 버튼 동작 호출
                    SwingUtilities.invokeLater(() -> {
                        TwitterHome home = TwitterHome.getInstance();
                        home.homeButton.doClick(); // 홈 버튼 동작 실행
                    });
                    parentFrame.dispose();
                }else{
                    System.out.println("login failed");
                }
            }
            else if (this.actionDb.equals("Following Post")) {
                stmt = con.createStatement();
                String followingPostQuery = "SELECT p.id AS post_id, p.content, p.user_id, p.create_at FROM follow f JOIN post p ON f.followed_id = p.user_id WHERE f.follow_id = '" + UserInfo.getInstance().getUserId() + "'ORDER BY p.create_at DESC";
                rs = stmt.executeQuery(followingPostQuery);

                while (rs.next()) {
                    int postId = rs.getInt("post_id");
                    String content = rs.getString("content");
                    String userId = rs.getString("user_id");
                    Timestamp createAt = rs.getTimestamp("create_at");

                    System.out.println("Post ID: " + postId);
                    System.out.println("Content: " + content);
                    System.out.println("Posted by: " + userId);
                    System.out.println("Created At: " + createAt);
                    System.out.println("------------------------");

                    SwingUtilities.invokeLater(() -> {
                        TwitterHome home = TwitterHome.getInstance();
                        home.addOrUpdatePost(postId, userId, content, createAt);
                    });
                }
            }else if(this.actionDb.equals("Bookmark")) {
                stmt = con.createStatement();
                String bookmarkPostQuery = " \"SELECT p.id AS post_id, p.content, p.user_id, p.create_at from bookmark_group";
                rs = stmt.executeQuery(bookmarkPostQuery);

                while (rs.next()){
                    int postId = rs.getInt("post_id");
                    String content = rs.getString("content");
                    String userId = rs.getString("user_id");
                    Timestamp createAt = rs.getTimestamp("create_at");

                    System.out.println("Post ID: " + postId);
                    System.out.println("Content: " + content);
                    System.out.println("Posted by: " + userId);
                    System.out.println("Created At: " + createAt);
                    System.out.println("------------------------");

                    SwingUtilities.invokeLater(() -> {
                        TwitterHome home = TwitterHome.getInstance();
                        home.addOrUpdatePost(postId, userId, content, createAt);
                    });
                }
            }

            else System.out.println("error");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if(stmt != null && !stmt.isClosed()) stmt.close();
            if(rs!=null && !rs.isClosed()) rs.close();
        }catch (SQLException e) {e.printStackTrace();}


    }

}
