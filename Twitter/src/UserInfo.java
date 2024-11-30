import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfo {
    private static UserInfo instance;

    private String userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userBirth;
    private String userGender;
    private int followerCount;
    private int followingCount;

    // Private constructor for Singleton pattern
    private UserInfo() {
    }

    // Get the single instance of UserInfo
    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }

    // Getters and Setters for user properties
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    
    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    // Method to set all user information from a ResultSet (after login or query)
    public void setUserInfo(ResultSet rs) throws SQLException {
        this.userId = rs.getString("id");
        this.userFirstName = rs.getString("first_name");
        this.userLastName = rs.getString("last_name");
        this.userEmail = rs.getString("email");
        this.userPhone = rs.getString("phone_number");
        this.userBirth = rs.getString("birth");
        this.userGender = rs.getString("gender");
    }
    public void updateUserInfoFromDb(String userId) {
        dbConnect db = dbConnect.getInstance();
        db.setUserInfo(userId);  // DB에서 사용자 정보 가져오기
    }
}
