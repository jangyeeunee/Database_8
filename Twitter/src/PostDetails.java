public class PostDetails {
    private String content;
    private String creationDate;
    private String username;
    private int commentCount;
    private int likeCount;

    public PostDetails(String content, String creationDate, String username, int commentCount, int likeCount) {
        this.content = content;
        this.creationDate = creationDate;
        this.username = username;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
    }

    public String getContent() {
        return content;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getUsername() {
        return username;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }
}