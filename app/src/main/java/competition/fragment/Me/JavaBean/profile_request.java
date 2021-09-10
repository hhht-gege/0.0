package competition.fragment.Me.JavaBean;

public class profile_request {

    /**
     * username : 用户名称
     * emailAddress : 用户邮箱地址
     * userIntroduction : 用户个人介绍; 可以为空字符串, 不可以为 null !
     */

    private String username;
    private String emailAddress;
    private String userIntroduction;

    public profile_request(String username,String emailAddress,String userIntroduction){
        this.username=username;
        this.emailAddress=emailAddress;
        this.userIntroduction=userIntroduction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserIntroduction() {
        return userIntroduction;
    }

    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }
}
