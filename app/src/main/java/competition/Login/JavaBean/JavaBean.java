package competition.Login.JavaBean;

public class JavaBean {


    /**
     * code : 状态码
     * msg : 响应消息
     * data : {"token":"用户识别身份码, 默认有效期为7天, 如果7天内没有进行操作, 将会自动失效; 登录状态的请求之后会刷新原token的存在时间,如超过7天未使用, 则自动废弃. 注: 7天内再次登录, 只会获得相同的 token","user":{"userId":"用户id","username":"用户名称","userIntroduction":"用户个人介绍","userPortraitPath":"用户头像超链接; "}}
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
         * token : 用户识别身份码, 默认有效期为7天, 如果7天内没有进行操作, 将会自动失效; 登录状态的请求之后会刷新原token的存在时间,如超过7天未使用, 则自动废弃. 注: 7天内再次登录, 只会获得相同的 token
         * user : {"userId":"用户id","username":"用户名称","userIntroduction":"用户个人介绍","userPortraitPath":"用户头像超链接; "}
         */

        private String token;
        private UserBean user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * userId : 用户id
             * username : 用户名称
             * userIntroduction : 用户个人介绍
             * userPortraitPath : 用户头像超链接;
             */

            private int userId;
            private String username;
            private String userIntroduction;
            private String userPortraitPath;
            private String userEmailAddress;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUserIntroduction() {
                return userIntroduction;
            }

            public void setUserIntroduction(String userIntroduction) {
                this.userIntroduction = userIntroduction;
            }

            public String getUserPortraitPath() {
                return userPortraitPath;
            }

            public void setUserPortraitPath(String userPortraitPath) {
                this.userPortraitPath = userPortraitPath;
            }

            public String getUserEmailAddress() {
                return userEmailAddress;
            }

            public void setUserEmailAddress(String userEmailAddress) {
                this.userEmailAddress = userEmailAddress;
            }
        }
    }
}
