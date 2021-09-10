package competition.fragment.Setting.Javabean;

import java.util.List;

public class collars_javabean {

    /**
     * code : 200
     * msg :
     * data : {"collars":[{"collarId":"127.0.0.1","collarIntroduction":"111","userId":19,"petId":20},{"collarId":"43.250.201.30","collarIntroduction":"\u201c\u201d","userId":19,"petId":0}]}
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
        private List<CollarsBean> collars;

        public List<CollarsBean> getCollars() {
            return collars;
        }

        public void setCollars(List<CollarsBean> collars) {
            this.collars = collars;
        }

        public static class CollarsBean {
            /**
             * collarId : 127.0.0.1
             * collarIntroduction : 111
             * userId : 19
             * petId : 20
             */

            private String collarId;
            private String collarIntroduction;
            private int userId;
            private int petId;

            public String getCollarId() {
                return collarId;
            }

            public void setCollarId(String collarId) {
                this.collarId = collarId;
            }

            public String getCollarIntroduction() {
                return collarIntroduction;
            }

            public void setCollarIntroduction(String collarIntroduction) {
                this.collarIntroduction = collarIntroduction;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getPetId() {
                return petId;
            }

            public void setPetId(int petId) {
                this.petId = petId;
            }
        }
    }
}
