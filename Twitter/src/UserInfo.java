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
    private String userAddress;
    private String userBirth;
    private String userGender;

    private UserInfo() {
    }

    public static UserInfo getInstance() {
        if (instance == null) instance = new UserInfo();
        return instance;
    }

    public void setUserInfo(ResultSet rs) {
        try {
            this.userId = rs.getString("id");
            this.userFirstName = rs.getString("first_Name");
            this.userLastName = rs.getString("last_name");
            this.userEmail = rs.getString("email");
            this.userPassword = rs.getString("pwd");
            this.userPhone = rs.getString("phone_number");
            this.userAddress = rs.getString("address");
            this.userBirth = rs.getString("birth");
            this.userGender = rs.getString("gender");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }


}
