package competition.Login.JavaBean;

public class Find_JavaBean  {
    /**
     * code : 状态码
     * msg : 状态消息
     * data : {"checkCode":"用户接收到的邮箱验证码, 有效时间默认为5分钟"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int  getCode() {
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * checkCode : 用户接收到的邮箱验证码, 有效时间默认为5分钟
         */

        private String checkCode;

        public String getCheckCode() {
            return checkCode;
        }

        public void setCheckCode(String checkCode) {
            this.checkCode = checkCode;
        }
    }
}
