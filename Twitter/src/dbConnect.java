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

    public void getActionDb() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/TWITTER";
            String user = "root", passwd = "자기 비번 쓰세요";
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
                    String name = rs.getString("first_name")+" "+rs.getString("last_name");
                    System.out.println("user name : " + name);
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


    }

}
