package competition.Login.JavaBean;

public class Findok_request {

    /**
     * username : 用户名
     * newPassword : 设置的新密码
     * verificationCode : 邮箱验证码
     */

    private String username;
    private String newPassword;
    private String verificationCode;

    public Findok_request(String username,String newPassword,String verificationCode){
        this.username=username;
        this.newPassword=newPassword;
        this.verificationCode=verificationCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
