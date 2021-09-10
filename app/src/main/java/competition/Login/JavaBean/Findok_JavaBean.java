package competition.Login.JavaBean;

public class Findok_JavaBean {

    /**
     * code : 状态码, 如果邮箱验证码没有过期, 且用户名和邮箱验证码是匹配的, 那么返回200; 否则, 返回除200之外的状态码
     * msg : 状态消息
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
