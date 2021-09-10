package competition.Login.JavaBean;

public class Regis_reque {

    /**
     * username : 4-16位, 支持中英文、数字
     * password : 6-18位, 字母、数字、字符`~!@#$%^&*()+=|{}':;,\\.<>/?！￥…（）—【】‘；：”“’。，、？
     * emailAddress : 账号邮箱, 仅支持QQ邮箱和网易邮箱(@163.com, @126.com/@qq.com结尾的)
     */

    private String username;
    private String password;
    private String emailAddress;

    public Regis_reque(String a,String b,String c){
        username=a;password=b;emailAddress=c;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
