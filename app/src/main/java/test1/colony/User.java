package test1.colony;

/**
 * Created by zahi on 11/09/2016.
 */
public class User {
    private  String title ,message;
    private  long userId ,dateCreatedMilli;

    public User(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public User(String title, String message, long userId, long dateCreatedMilli) {
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.dateCreatedMilli = dateCreatedMilli;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return userId;
    }

    public long getDate() {
        return dateCreatedMilli;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
