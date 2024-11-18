public class UserInfo {
    private static UserInfo instance;
    private String userId;
    private UserInfo(){}

    public static UserInfo getInstance(){
        if(instance == null) instance = new UserInfo();
        return instance;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

}
