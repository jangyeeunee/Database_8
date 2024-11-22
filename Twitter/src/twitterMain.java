import javax.swing.*;

public class twitterMain {
    public static void main(String[] args) {
        dbConnect db = dbConnect.getInstance();
        new TwitterLogin();
    }
}
