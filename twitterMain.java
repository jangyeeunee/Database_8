import javax.swing.*;

public class twitterMain {
    public static void main(String[] args) {
        // TwitterLogin 실행
        TwitterLogin loginScreen = new TwitterLogin();
        loginScreen.setVisible(true);

        // 로그인 성공 시 검색 화면 실행
        loginScreen.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if (UserInfo.getInstance().getUserId() != null) {
                    // 로그인 성공 시 검색 화면을 이벤트 디스패처 스레드에서 실행
                    SwingUtilities.invokeLater(() -> new TwitterSearch());
                } else {
                    System.out.println("Login failed or canceled.");
                }
            }
        });
    }
}