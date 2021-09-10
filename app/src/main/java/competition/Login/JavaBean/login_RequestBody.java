package competition.Login.JavaBean;

public class login_RequestBody {

    /**
     * username : 4-16位, 支持中英文、数字
     * password : 6-18位, 字母、数字、字符`~!@#$%^&*()+=|{}':;,\\.<>/?！￥…（）—【】‘；：”“’。，、？
     */

    private String username;
    private String password;

    public login_RequestBody(String usernameStr, String passwordStr) {
        username=usernameStr;
        password=passwordStr;
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
}
