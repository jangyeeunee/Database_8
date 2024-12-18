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
                user.setUserInfo(rs);
                updateFollowStats(UserInfo.getInstance().getUserId());
                flag = true;
                // TwitterHome 생성 후 홈 버튼 동작 호출
                SwingUtilities.invokeLater(() -> {
                    BottomPanel button = BottomPanel.getInstance();
                    button.homeButton.doClick(); // 홈 버튼 동작 실행
                });
            }else{
                JOptionPane.showMessageDialog(parentFrame, "Login Failed");
                return false;
            }
            if (flag) {
                parentFrame.dispose();
            } else{
                JOptionPane.showMessageDialog(null, "ID or password does not match");
                parentFrame.setVisible(true);

            }

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
                JOptionPane.showMessageDialog(null,"Success to Sign up!");
                return true;
            }

            pstmt.close();
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Check what you wrote !");
            ex.printStackTrace();
        }
        return false;
    }

    public Post[] getFollowingPost() {
        ResultSet rs = null;

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

        try {
            String followingPostQuery = " SELECT DISTINCT p.id AS post_id, p.content, p.user_id, p.create_at FROM post AS p JOIN bookmark_group AS bg ON p.id = bg.post_id WHERE bg.user_id = ? ORDER BY p.create_at DESC";


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

            }

            return postList.toArray(new Post[0]);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return new Post[0];
        }
    }

    public boolean addComment(String userId, int postId, String comment) {
        String query = "INSERT INTO COMMENT (user_id, post_id, comment) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1,userId);
            stmt.setInt(2, postId);
            stmt.setString(3, comment);
            stmt.executeUpdate();
            return true; // Success
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Failure
        }
    }

    public List<Map<String, String>> getCommentsWithUsers(int postId) {
        String query = "SELECT user_id, comment FROM COMMENT WHERE post_id = ?";
        List<Map<String, String>> comments = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // 각 댓글에 대한 정보를 저장할 Map 생성
                    Map<String, String> commentData = new HashMap<>();
                    commentData.put("user_id", rs.getString("user_id")); // 작성자 ID
                    commentData.put("comment", rs.getString("comment")); // 댓글 내용
                    comments.add(commentData); // List에 추가
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments; // 댓글 목록 반환
    }
    public void CreatePost(Map<String, String> data,JFrame parentFrame) {
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

            if (rowsInserted > 0) {

                rs = pstmt.getGeneratedKeys();
                if(rs.next()) {

                    //hash 테이블과 mapping
                    int postId = rs.getInt(1);
                    pstmt.close();
                    mapHashtag(data.get("hashtag"), postId);
                }
                pstmt.close();
            }
            parentFrame.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void mapHashtag(String hashtag, int id) {
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

    public Post[] getUserPosts(String userId) {
        ResultSet rs = null;

        try {
            String query = "SELECT p.id AS post_id, p.content, p.user_id, p.create_at " +
                           "FROM post AS p " +
                           "WHERE p.user_id = ? " + 
                           "ORDER BY p.create_at DESC"; 

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, userId);  // 여기에 userId를 전달
            rs = pstmt.executeQuery();

            List<Post> postList = new ArrayList<>();

            while (rs.next()) {
                int postId = rs.getInt("post_id");
                String content = rs.getString("content");
                String userIdFromDB = rs.getString("user_id");
                Timestamp createAt = rs.getTimestamp("create_at");

                postList.add(new Post(postId, userIdFromDB, content, createAt));
            }

            return postList.toArray(new Post[0]);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return new Post[0];
        }
    }

    public Post[] getUserLikedPosts(String userId) {
        ResultSet rs = null;

        try {
            String query = "SELECT p.id AS post_id, p.content, p.user_id, p.create_at " +
                           "FROM post AS p " +
                           "JOIN post_like AS pl ON p.id = pl.post_id " +
                           "WHERE pl.user_id = ? " + 
                           "ORDER BY p.create_at DESC"; // 좋아요한 게시물 정렬

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, userId); // userId를 전달
            rs = pstmt.executeQuery();

            List<Post> postList = new ArrayList<>();

            while (rs.next()) {
                int postId = rs.getInt("post_id");
                String content = rs.getString("content");
                String userIdFromPost = rs.getString("user_id");
                Timestamp createAt = rs.getTimestamp("create_at");

                postList.add(new Post(postId, userIdFromPost, content, createAt));
            }

            return postList.toArray(new Post[0]);

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return new Post[0];
        }
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

    public void delBookmark(int postId)
    {
        String query = "DELETE FROM BOOKMARK_GROUP WHERE post_id = ? and user_id = ?";
        try{
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1,postId);
            stmt.setString(2,UserInfo.getInstance().getUserId());
            stmt.executeUpdate();

            stmt.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLike(int postId) {
        String queryInsert = "INSERT INTO POST_LIKE (post_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(queryInsert)) {
            stmt.setInt(1, postId);
            stmt.setString(2, UserInfo.getInstance().getUserId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void delLike(int postId) {
        String query = "DELETE FROM POST_LIKE WHERE post_id = ? and user_id = ?";
        try{
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1,postId);
            stmt.setString(2,UserInfo.getInstance().getUserId());
            stmt.executeUpdate();

            stmt.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Post[] getPostsByHashtag(String hashtag) throws SQLException {
        String query = "SELECT * FROM POST JOIN ( SELECT post_id FROM POST_HASHTAG WHERE hash_name LIKE ? ) AS H ON POST.id = H.post_id";

        ResultSet rs = null;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + hashtag + "%");
            rs = pstmt.executeQuery();

            List<Post> postList = new ArrayList<>();

            while (rs.next()) {
                int postId = rs.getInt("post_id");
                String content = rs.getString("content");
                String userIdFromDB = rs.getString("user_id");
                Timestamp createAt = rs.getTimestamp("create_at");

                postList.add(new Post(postId, userIdFromDB, content, createAt));
            }

            return postList.toArray(new Post[0]);
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return new Post[0];
        }
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
            updateFollowStats(UserInfo.getInstance().getUserId());
        }
    }

    public boolean updateUserInfo(String userId, String newPassword, String newFirstName, String newLastName, String newEmail, String newPhone, String newBirth, String newGender) {
        String selectQuery = "SELECT pwd, first_name, last_name, email, phone_number, birth, gender FROM USER WHERE id = ?";
        String updateQuery = "UPDATE USER SET pwd = ?, first_name = ?, last_name = ?, email = ?, phone_number = ?, birth = ?, gender = ? WHERE id = ?";

        try (PreparedStatement selectStmt = con.prepareStatement(selectQuery)) {
            selectStmt.setString(1, userId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                // 기존 데이터와 새로운 데이터 비교
                boolean isSame = newPassword.equals(rs.getString("pwd")) &&
                        newFirstName.equals(rs.getString("first_name")) &&
                        newLastName.equals(rs.getString("last_name")) &&
                        newEmail.equals(rs.getString("email")) &&
                        newPhone.equals(rs.getString("phone_number")) &&
                        newBirth.equals(rs.getString("birth")) &&
                        newGender.equals(rs.getString("gender"));

                if (isSame) {
                    JOptionPane.showMessageDialog(null, "No changes detected in the user information.");
                    return false; // 변경 사항 없음
                }
            } else {
                JOptionPane.showMessageDialog(null, "User not found.");
                return false; // 유저 없음
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // 데이터가 변경되었다면 업데이트 실행
        try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
            updateStmt.setString(1, newPassword);
            updateStmt.setString(2, newFirstName);
            updateStmt.setString(3, newLastName);
            updateStmt.setString(4, newEmail);
            updateStmt.setString(5, newPhone);
            updateStmt.setString(6, newBirth);
            updateStmt.setString(7, newGender);
            updateStmt.setString(8, userId);

            int rowsUpdated = updateStmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public boolean checkisbookmarked(int postId) {
        ResultSet rs = null;
        try {
            String query = "SELECT id FROM BOOKMARK_GROUP WHERE user_id = ? and post_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, UserInfo.getInstance().getUserId());
            ps.setInt(2, postId);
            rs = ps.executeQuery();
            return rs.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkisliked(int postId) {
        ResultSet rs = null;
        try {
            String query = "SELECT id FROM POST_LIKE WHERE user_id = ? and post_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, UserInfo.getInstance().getUserId());
            ps.setInt(2, postId);
            rs = ps.executeQuery();
            return rs.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        
    }  public void setUserInfo(String userId) {
        try {
            // 사용자 기본 정보 가져오기
            String query = "SELECT first_name, last_name, email, phone_number, birth, gender FROM USER WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // UserInfo 객체에 사용자 기본 정보 업데이트
                UserInfo.getInstance().setUserFirstName(rs.getString("first_name"));
                UserInfo.getInstance().setUserLastName(rs.getString("last_name"));
                UserInfo.getInstance().setUserEmail(rs.getString("email"));
                UserInfo.getInstance().setUserPhone(rs.getString("phone_number"));
                UserInfo.getInstance().setUserBirth(rs.getString("birth"));
                UserInfo.getInstance().setUserGender(rs.getString("gender"));
            }

            // 팔로잉/팔로워 수 업데이트
            updateFollowStats(userId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 팔로우 통계 업데이트
    public void updateFollowStats(String userId) {
        try {
            // 팔로워 수 가져오기
            String followerQuery = "SELECT COUNT(*) AS follower_count FROM FOLLOW WHERE followed_id = ?";
            PreparedStatement followerStmt = con.prepareStatement(followerQuery);
            followerStmt.setString(1, userId);
            ResultSet followerRs = followerStmt.executeQuery();
            if (followerRs.next()) {
                UserInfo.getInstance().setFollowerCount(followerRs.getInt("follower_count"));
            }

            // 팔로잉 수 가져오기
            String followingQuery = "SELECT COUNT(*) AS following_count FROM FOLLOW WHERE follow_id = ?";
            PreparedStatement followingStmt = con.prepareStatement(followingQuery);
            followingStmt.setString(1, userId);
            ResultSet followingRs = followingStmt.executeQuery();
            if (followingRs.next()) {
                UserInfo.getInstance().setFollowingCount(followingRs.getInt("following_count"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
