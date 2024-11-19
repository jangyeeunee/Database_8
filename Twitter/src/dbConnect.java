import java.sql.Statement;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;

public class dbConnect {
    private String actionDb;
    private String Id;
    private String Password;

    public dbConnect(String actionDb, String Id, String Password) {
        this.actionDb = actionDb;
        this.Id = Id;
        this.Password = Password;
    }

    public User getActionDb() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        User userInfo = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/TWITTER";
            String user = "root", passwd = "tndk1008";
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
                    String Id = rs.getString("id");
                    String Password = rs.getString("pwd");
                    String email = rs.getString("email");
                    String birth = rs.getString("birth");
                    String Gender = rs.getString("gender");
                    String phoneNumber = rs.getString("phone_number");
                    String name = rs.getString("first_name")+" "+rs.getString("last_name");
                    userInfo = new User(Id,name,email,Password,Gender,phoneNumber,birth);
                }else{
                    System.out.println("login failed");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if(stmt != null && !stmt.isClosed()) stmt.close();
            if(rs!=null && !rs.isClosed()) rs.close();
        }catch (SQLException e) {e.printStackTrace();}

        return userInfo;
    }

}
