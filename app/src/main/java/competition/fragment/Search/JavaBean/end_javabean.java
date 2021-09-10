package competition.fragment.Search.JavaBean;

import java.util.List;

public class end_javabean{

    /**
     * code : 0
     * msg :
     * data : {"dynamics":[{"dynamicId":0,"topic":"","publishTime":"","content":"","picturesUrl":[],"publisher":{"userId":0,"username":"","userPortraitPath":""}}],"nextOffset":0}
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
         * dynamics : [{"dynamicId":0,"topic":"","publishTime":"","content":"","picturesUrl":[],"publisher":{"userId":0,"username":"","userPortraitPath":""}}]
         * nextOffset : 0
         */

        private List<DynamicsBean> dynamics;
        private int nextOffset;

        public List<DynamicsBean> getDynamics() {
            return dynamics;
        }

        public void setDynamics(List<DynamicsBean> dynamics) {
            this.dynamics = dynamics;
        }

        public int getNextOffset() {
            return nextOffset;
        }

        public void setNextOffset(int nextOffset) {
            this.nextOffset = nextOffset;
        }

        public static class DynamicsBean {
            /**
             * dynamicId : 0
             * topic :
             * publishTime :
             * content :
             * picturesUrl : []
             * publisher : {"userId":0,"username":"","userPortraitPath":""}
             */

            private int dynamicId;
            private String topic;
            private String publishTime;
            private String content;
            private List<String> picturesUrl;
            private PublisherBean publisher;

            public int getDynamicId() {
                return dynamicId;
            }

            public void setDynamicId(int dynamicId) {
                this.dynamicId = dynamicId;
            }

            public String getTopic() {
                return topic;
            }

            public void setTopic(String topic) {
                this.topic = topic;
            }

            public String getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(String publishTime) {
                this.publishTime = publishTime;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public List<String> getPicturesUrl() {
                return picturesUrl;
            }

            public void setPicturesUrl(List<String> picturesUrl) {
                this.picturesUrl = picturesUrl;
            }

            public PublisherBean getPublisher() {
                return publisher;
            }

            public void setPublisher(PublisherBean publisher) {
                this.publisher = publisher;
            }

            public static class PublisherBean {
                /**
                 * userId : 0
                 * username :
                 * userPortraitPath :
                 */

                private int userId;
                private String username;
                private String userPortraitPath;

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

                public String getUserPortraitPath() {
                    return userPortraitPath;
                }

                public void setUserPortraitPath(String userPortraitPath) {
                    this.userPortraitPath = userPortraitPath;
                }
            }
        }
    }
}