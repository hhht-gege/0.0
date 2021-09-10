package competition.fragment.Anim.JavaBean;

public class Imag_javabean {
    /**
     * code : 200说明请求成功, 其余错误, 参见状态码文档
     * msg : 请求状态信息, 通常与code相对应
     * data : {"petPortraitPath":"牲畜头像超链接"}
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
         * petPortraitPath : 牲畜头像超链接
         */

        private String petPortraitPath;

        public String getPetPortraitPath() {
            return petPortraitPath;
        }

        public void setPetPortraitPath(String petPortraitPath) {
            this.petPortraitPath = petPortraitPath;
        }
    }
}
