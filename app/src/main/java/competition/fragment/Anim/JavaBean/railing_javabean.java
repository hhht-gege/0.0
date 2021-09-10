package competition.fragment.Anim.JavaBean;

public class railing_javabean {

    /**
     * code : 200
     * msg :
     * data : {"railingId":6}
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
         * railingId : 6
         */

        private int railingId;

        public int getRailingId() {
            return railingId;
        }

        public void setRailingId(int railingId) {
            this.railingId = railingId;
        }
    }
}
