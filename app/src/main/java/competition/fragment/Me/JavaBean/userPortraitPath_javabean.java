package competition.fragment.Me.JavaBean;

public class userPortraitPath_javabean {

    /**
     * code : 200说明请求成功, 其余错误, 参见状态码文档
     * msg : 请求状态信息, 通常与code相对应
     * data : {"user":{"userPortraitPath":"用户头像超链接; "}}
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
         * user : {"userPortraitPath":"用户头像超链接; "}
         */

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * userPortraitPath : 用户头像超链接;
             */

            private String userPortraitPath;

            public String getUserPortraitPath() {
                return userPortraitPath;
            }

            public void setUserPortraitPath(String userPortraitPath) {
                this.userPortraitPath = userPortraitPath;
            }
        }
    }
}
