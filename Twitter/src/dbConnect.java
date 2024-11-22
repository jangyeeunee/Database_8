import javax.swing.*;
import java.sql.*;
import java.util.*;

public class dbConnect {
    private static dbConnect dbConnecter;

    private Connection con;

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
                SwingUtilities.invokeLater(() -> {
                    BottomPanel button = BottomPanel.getInstance();
                    button.homeButton.doClick(); // 홈 버튼 동작 실행
                });
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
            String user = "root", passwd = "wldmsdl7715";
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

    public Post[] getFollowingPost() {
        ResultSet rs = null;
        System.out.println("getFollowingPosts");

        try {
            String followingPostQuery = "SELECT p.id AS post_id, p.content, p.user_id, p.create_at, p.repost_id " +
                    "FROM post AS p " +
                    "JOIN (SELECT followed_id AS following FROM FOLLOW WHERE follow_id = ?) AS f " +
                    "ON p.user_id = f.following " +
                    "ORDER BY p.create_at DESC";

            PreparedStatement pstmt = con.prepareStatement(followingPostQuery);
            pstmt.setString(1, UserInfo.getInstance().getUserId());
            rs = pstmt.executeQuery();

            List<Post> postList = new ArrayList<>();

            while (rs.next()) {
                int postId = rs.getInt("post_id");
                String content = rs.getString("content");
                String userId = rs.getString("user_id");
                Timestamp createAt = rs.getTimestamp("create_at");
                int repostId = rs.getInt("repost_id");

                postList.add(new Post(postId, userId, content, repostId, createAt));

                System.out.println("Post ID: " + postId);
                System.out.println("Content: " + content);
                System.out.println("Posted by: " + userId);
                System.out.println("Created At: " + createAt);
                System.out.println("------------------------");
            }

            return postList.toArray(new Post[0]);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return new Post[0];
        }
    }

    public Post[]getBookmarkPost() {
        ResultSet rs = null;
        System.out.println("getBookmarkPosts");

        try {
            String followingPostQuery = " SELECT DISTINCT p.id AS post_id, p.content, p.user_id, p.create_at, p.repost_id FROM post AS p JOIN bookmark_group AS bg ON p.id = bg.post_id WHERE bg.user_id = ?";


            PreparedStatement pstmt = con.prepareStatement(followingPostQuery);
            pstmt.setString(1, UserInfo.getInstance().getUserId());
            rs = pstmt.executeQuery();

            List<Post> postList = new ArrayList<>();

            while (rs.next()) {
                int postId = rs.getInt("post_id");
                String content = rs.getString("content");
                String userId = rs.getString("user_id");
                Timestamp createAt = rs.getTimestamp("create_at");
                int repostId = rs.getInt("repost_id");

                postList.add(new Post(postId, userId, content, repostId, createAt));

                System.out.println("Post ID: " + postId);
                System.out.println("Content: " + content);
                System.out.println("Posted by: " + userId);
                System.out.println("Created At: " + createAt);
                System.out.println("------------------------");
            }

            return postList.toArray(new Post[0]);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return new Post[0];
        }
    }



}