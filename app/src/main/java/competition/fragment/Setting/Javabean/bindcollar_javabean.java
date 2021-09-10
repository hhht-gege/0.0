package competition.fragment.Setting.Javabean;

public class bindcollar_javabean {

    /**
     * code : 状态码; 如果访问路径的用户 id 与 token 对应的用户 id 不匹配, 则请求失败
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
