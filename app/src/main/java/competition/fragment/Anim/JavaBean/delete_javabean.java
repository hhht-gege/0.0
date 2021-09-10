package competition.fragment.Anim.JavaBean;

public class delete_javabean {

    /**
     * code : 200说明请求成功, 其余错误, 参见状态码文档
     * msg : 请求状态信息, 通常与code相对应
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
