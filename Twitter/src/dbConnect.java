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

    public Post[] getFollowingPost() {
        ResultSet rs = null;
        System.out.println("getFollowingPosts");

        try {
            String followingPostQuery = "SELECT p.id AS post_id, p.content, p.user_id, p.create_at " +
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

                postList.add(new Post(postId, userId, content, createAt));

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

    public Post[] getBookmarkPost() {
        ResultSet rs = null;
        System.out.println("getBookmarkPosts");

        try {
            String followingPostQuery = " SELECT DISTINCT p.id AS post_id, p.content, p.user_id, p.create_at FROM post AS p JOIN bookmark_group AS bg ON p.id = bg.post_id WHERE bg.user_id = ?";


            PreparedStatement pstmt = con.prepareStatement(followingPostQuery);
            pstmt.setString(1, UserInfo.getInstance().getUserId());
            rs = pstmt.executeQuery();

            List<Post> postList = new ArrayList<>();

            while (rs.next()) {
                int postId = rs.getInt("post_id");
                String content = rs.getString("content");
                String userId = rs.getString("user_id");
                Timestamp createAt = rs.getTimestamp("create_at");

                postList.add(new Post(postId, userId, content, createAt));

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

    public boolean addComment(int postId, String comment) {
        String query = "INSERT INTO COMMENT (post_id, comment) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, postId);
            stmt.setString(2, comment);
            stmt.executeUpdate();
            return true; // Success
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Failure
        }
    }

    public List<String> getCommentsByPostId(int postId) {
        String query = "SELECT comment FROM COMMENT WHERE post_id = ?";
        List<String> comments = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(rs.getString("comment"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return comments; // Return the list of comments
    }



    public void CreatePost(Map<String, String> data,JFrame parentFrame) {

        Statement stmt = null;
        ResultSet rs = null;

        try {
            String query = "INSERT INTO POST(content, user_id, create_at) VALUES (?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis()); // 현재 시간

            // 데이터 바인딩
            pstmt.setString(1, data.get("content"));
            pstmt.setString(2, UserInfo.getInstance().getUserId());
            pstmt.setTimestamp(3, currentTimestamp);

            // 데이터베이스에 삽입
            int rowsInserted = pstmt.executeUpdate();
            System.out.println("데베 삽입 " + rowsInserted);

            if (rowsInserted > 0) {

                rs = pstmt.getGeneratedKeys();
                if(rs.next()) {

                    //hash 테이블과 mapping
                    int postId = rs.getInt(1);
                    pstmt.close();
                    mapHashtag(data.get("hashtag"), postId);
                    System.out.println("post생성 성공");
                }

                pstmt.close();

            }

            parentFrame.dispose();



        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("post 생성중 오류 발생.");
        }


    }

    public void mapHashtag(String hashtag, int id) {
        ResultSet rs = null;
        String[] hashs = hashtag.split(", ");
        try {
            for (int i = 0; i < hashs.length; i++) {
                String query = "INSERT INTO POST_HASHTAG(post_id, hash_name) VALUES (?, ?)";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setInt(1, id);
                pstmt.setString(2, hashs[i]);

                pstmt.executeUpdate();
            }

        } catch (SQLException e) {}
    }

    public List<Post> getUserPosts(String userId) {
        List<Post> posts = new ArrayList<>();

        try {
            String query = "SELECT * FROM POST WHERE user_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int postId = rs.getInt("id");
                String content = rs.getString("content");
                Timestamp createAt = rs.getTimestamp("create_at");

                // Create Post object and add to the list
                Post post = new Post(postId, userId, content, createAt);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }
    public List<Post> getUserLikedPosts(String userId) {
        List<Post> posts = new ArrayList<>();

        try {
            // 좋아요를 누른 포스트 가져오는 쿼리
            String query = "SELECT p.id, p.user_id, p.content, p.create_at " +
                    "FROM POST p " +
                    "JOIN POST_LIKE pl ON p.id = pl.post_id " +
                    "WHERE pl.user_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int postId = rs.getInt("id");
                String content = rs.getString("content");
                String postUserId = rs.getString("user_id");  // 포스트 작성자 ID
                Timestamp createAt = rs.getTimestamp("create_at");

                // Post 객체 생성 후 리스트에 추가
                Post post = new Post(postId, postUserId, content, createAt);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public void addBookmark(int postId)
    {
        String queryInsert = "INSERT INTO BOOKMARK_GROUP (post_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(queryInsert)) {
            stmt.setInt(1, postId);
            stmt.setString(2, UserInfo.getInstance().getUserId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void addLike(int postId)
    {
        String queryInsert = "INSERT INTO POST_LIKE (post_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(queryInsert)) {
            stmt.setInt(1, postId);
            stmt.setString(2, UserInfo.getInstance().getUserId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public ResultSet getPostsByHashtag(String hashtag) throws SQLException {
        String query = "SELECT user_id, content FROM post WHERE content LIKE ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, "%" + hashtag + "%");
        return stmt.executeQuery();
    }

    public ResultSet getPostsByUser(String userId) throws SQLException {
        String query = "SELECT id FROM user WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, userId);
        return stmt.executeQuery();
    }
    public boolean isFollowing(String userId) throws SQLException {
        String query = "SELECT * FROM follow WHERE follow_id = ? AND followed_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, UserInfo.getInstance().getUserId()); // 로그인한 사용자 ID
            stmt.setString(2, userId); // 검색된 사용자 ID
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // 결과가 있으면 팔로우 상태, 없으면 팔로우 안 함
        }
    }

    // Follow/Unfollow
    public void toggleFollow(String userId) throws SQLException {
        if (isFollowing(userId)) {
            String sql = "DELETE FROM follow WHERE follow_id = ? AND followed_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, UserInfo.getInstance().getUserId());
            stmt.setString(2, userId);
            stmt.executeUpdate();
        } else {
            String sql = "INSERT INTO follow (follow_id, followed_id) VALUES (?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, UserInfo.getInstance().getUserId());
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
    }

    public boolean updateUserInfo(String userId, String newPassword, String newFirstName, String newLastName, String newEmail, String newPhone, String newBirth, String newGender) {
        String query = "UPDATE USER SET pwd = ?, first_name = ?, last_name = ?, email = ?, phone_number = ?, birth = ?, gender = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            // Set parameters in the query
            stmt.setString(1, newPassword);
            stmt.setString(2, newFirstName);
            stmt.setString(3, newLastName);
            stmt.setString(4, newEmail);
            stmt.setString(5, newPhone);
            stmt.setString(6, newBirth);
            stmt.setString(7, newGender);
            stmt.setString(8, userId);

            // Execute update and check if the row is updated
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return true; // Success, user info updated
            } else {
                System.out.println("User not found or no changes detected.");
                return false; // Failure, user not found or no changes
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error, handle exception
        }
    }
    public void updateFollowStats() {
        UserInfo user = UserInfo.getInstance();
        String userId = user.getUserId();

        if (userId == null || userId.isEmpty()) {
            System.out.println("Login please");
            return;
        }

        try {
            String followerQuery = "SELECT COUNT(*) AS follower_count FROM FOLLOW WHERE followed_id = ?";
            PreparedStatement followerStmt = con.prepareStatement(followerQuery);
            followerStmt.setString(1, userId);
            ResultSet followerRs = followerStmt.executeQuery();
            if (followerRs.next()) {
                user.setFollowerCount(followerRs.getInt("follower_count"));
            }

            String followingQuery = "SELECT COUNT(*) AS following_count FROM FOLLOW WHERE follow_id = ?";
            PreparedStatement followingStmt = con.prepareStatement(followingQuery);
            followingStmt.setString(1, userId);
            ResultSet followingRs = followingStmt.executeQuery();
            if (followingRs.next()) {
                user.setFollowingCount(followingRs.getInt("following_count"));
            }

            System.out.println("Follower Count: " + user.getFollowerCount());
            System.out.println("Following Count: " + user.getFollowingCount());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
