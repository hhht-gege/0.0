package competition.fragment.Anim.JavaBean;

public class AnimAdd_javabean {


    /**
     * code : 状态码; 如果访问路径的用户 id 与 token 对应的用户 id 不匹配, 则请求失败
     * msg : 状态消息
     * data : {"petId":"添加的牲畜分配到的id"}
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
         * petId : 添加的牲畜分配到的id
         */

        private int petId;

        public int getPetId() {
            return petId;
        }

        public void setPetId(int petId) {
            this.petId = petId;
        }
    }
}
