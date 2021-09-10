package competition.fragment.Anim.JavaBean;

public class Anim_ip_javabean {

    /**
     * code : 0
     * msg :
     * data : {"collarIp":""}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * collarIp :
         */

        private String collarIp;

        public String getCollarIp() {
            return collarIp;
        }

        public void setCollarIp(String collarIp) {
            this.collarIp = collarIp;
        }
    }
}
