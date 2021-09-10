package competition.Login.JavaBean;

public class login_nopass_request {

    /**
     * token : 用户识别身份码, 默认有效期为7天, 如果一个小时内没有进行操作, 将会自动失效; 请求成功之后会刷新原token的存在时间,如超过7天未使用, 则自动废弃. 注: 7天内再次登录, 只会获得相同的 token
     */

    private String token;

    public login_nopass_request(String token){
        this.token=token;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
