import javax.swing.*;
import java.sql.Statement;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

public class dbConnect {
    private static dbConnect dbConnecter;

    private Connection con;

//    public dbConnect(String Id, String Password, JFrame parentFrame) {
//        this.Id = Id;
//        this.Password = Password;
//        this.parentFrame = parentFrame;
//    }

    public dbConnect() {
        this.connectDB();
    }

    public static dbConnect getInstance() {
        if (dbConnecter == null) {
            dbConnecter = new dbConnect();
        }
        return dbConnecter;
    }

    public boolean loginDB(String Id, String Password, JFrame parentFrame) {
        Statement stmt = null;
        ResultSet rs = null;
        UserInfo user = UserInfo.getInstance();
        boolean flag = false;

        try {
            stmt = con.createStatement();
            String s1 = "select * from user where id = \"" + Id + "\" and pwd = \"" + Password + "\"";
            rs = stmt.executeQuery(s1);
            if (rs.next()) {
                System.out.println("login success");
                user.setUserInfo(rs);
                flag = true;
                // TwitterHome 생성 후 홈 버튼 동작 호출
//                    SwingUtilities.invokeLater(() -> {
//                        TwitterHome home = TwitterHome.getInstance();
//                        home.homeButton.doClick(); // 홈 버튼 동작 실행
//                    }
            }
            if (flag) {
                System.out.println(user.getUserId());
                parentFrame.dispose();
            } else
                parentFrame.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void connectDB() {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/TWITTER";
            String user = "root", passwd = "tndk1008";
            con = DriverManager.getConnection(url, user, passwd);
            System.out.println(con);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean SignUpDB(Map<String, String> data, JFrame parentFrame) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String query = "INSERT INTO USER (id, pwd, first_name, last_name, email, phone_number, birth, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);

            // 데이터 바인딩
            pstmt.setString(1, data.get("id"));
            pstmt.setString(2, data.get("password"));
            pstmt.setString(3, data.get("first_name"));
            pstmt.setString(4, data.get("last_name"));
            pstmt.setString(5, data.get("email"));
            pstmt.setString(6, data.get("phone_number"));
            pstmt.setString(7, data.get("birth"));
            pstmt.setString(8, data.get("gender"));

            // 데이터베이스에 삽입
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("회원가입 성공!");
                return true;
            }

            pstmt.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("회원가입 중 오류 발생.");
        }
        return false;
    }

}


//    public void getActionDb() {
//        Statement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            if (this.actionDb.equals("login")) {
//                stmt = con.createStatement();
//                String s1 = "select * from user where id = \"" + this.Id + "\" and pwd = \"" + this.Password + "\"";
//                rs = stmt.executeQuery(s1);
//                if (rs.next()) {
//                    System.out.println("login success");
//                    UserInfo.getInstance().setUserId(this.Id);
//                    String name = rs.getString("first_name") + " " + rs.getString("last_name");
//                    System.out.println("user name : " + name);
//                    // TwitterHome 생성 후 홈 버튼 동작 호출
//                    SwingUtilities.invokeLater(() -> {
//                        TwitterHome home = TwitterHome.getInstance();
//                        home.homeButton.doClick(); // 홈 버튼 동작 실행
//                    });
//                    parentFrame.dispose();
//                } else {
//                    System.out.println("login failed");
//                }
//            } else if (this.actionDb.equals("Following Post")) {
//                stmt = con.createStatement();
//                String followingPostQuery = "SELECT p.id AS post_id, p.content, p.user_id, p.create_at FROM follow f JOIN post p ON f.followed_id = p.user_id WHERE f.follow_id = '" + UserInfo.getInstance().getUserId() + "'ORDER BY p.create_at DESC";
//                rs = stmt.executeQuery(followingPostQuery);
//
//                while (rs.next()) {
//                    int postId = rs.getInt("post_id");
//                    String content = rs.getString("content");
//                    String userId = rs.getString("user_id");
//                    Timestamp createAt = rs.getTimestamp("create_at");
//
//                    System.out.println("Post ID: " + postId);
//                    System.out.println("Content: " + content);
//                    System.out.println("Posted by: " + userId);
//                    System.out.println("Created At: " + createAt);
//                    System.out.println("------------------------");
//
//                    SwingUtilities.invokeLater(() -> {
//                        TwitterHome home = TwitterHome.getInstance();
//                        home.addOrUpdatePost(postId, userId, content, createAt);
//                    });
//                }
//            } else if (this.actionDb.equals("Bookmark")) {
//                stmt = con.createStatement();
//                String bookmarkPostQuery = " SELECT FROM post AS p JOIN bookmark_group AS bg ON p.id = bg.post_id; ";
//                rs = stmt.executeQuery(bookmarkPostQuery);
//
//                while (rs.next()) {
//                    int postId = rs.getInt("post_id");
//                    String content = rs.getString("content");
//                    String userId = rs.getString("user_id");
//                    Timestamp createAt = rs.getTimestamp("create_at");
//
//                    System.out.println("Post ID: " + postId);
//                    System.out.println("Content: " + content);
//                    System.out.println("Posted by: " + userId);
//                    System.out.println("Created At: " + createAt);
//                    System.out.println("------------------------");
//
//                    SwingUtilities.invokeLater(() -> {
//                        BookmarkPage bookmarkPage = BookmarkPage.getInstance();
//                        bookmarkPage.addOrUpdatePost(postId, userId, content, createAt);
//                    });
//                }
//            } else System.out.println("error");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            if (stmt != null && !stmt.isClosed()) stmt.close();
//            if (rs != null && !rs.isClosed()) rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//    }

