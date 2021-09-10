package competition.fragment.Anim.JavaBean;

import java.util.List;

public class Anim_history_javabean {

    /**
     * code : 200
     * msg :
     * data : {"collarMsgs":[{"temp":25,"humidity":46,"heartrate":90,"latitude":28.738272928712647,"longitude":113.79018041633702,"stepnumber":0,"time":"2021-05-15 20:45:48"},{"temp":24,"humidity":49,"heartrate":86,"latitude":28.43577199008032,"longitude":113.2358856756743,"stepnumber":0,"time":"2021-05-15 20:55:01"},{"temp":24,"humidity":40,"heartrate":89,"latitude":28.006527238986493,"longitude":113.95237707571674,"stepnumber":0,"time":"2021-05-15 21:08:09"},{"temp":25,"humidity":40,"heartrate":86,"latitude":28.936197488095818,"longitude":113.96542918576934,"stepnumber":0,"time":"2021-05-15 21:10:03"},{"temp":25,"humidity":48,"heartrate":89,"latitude":28.081527123819612,"longitude":113.99517672186379,"stepnumber":0,"time":"2021-05-15 21:15:08"}]}
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
        private List<CollarMsgsBean> collarMsgs;

        public List<CollarMsgsBean> getCollarMsgs() {
            return collarMsgs;
        }

        public void setCollarMsgs(List<CollarMsgsBean> collarMsgs) {
            this.collarMsgs = collarMsgs;
        }

        public static class CollarMsgsBean {
            /**
             * temp : 25
             * humidity : 46
             * heartrate : 90
             * latitude : 28.738272928712647
             * longitude : 113.79018041633702
             * stepnumber : 0
             * time : 2021-05-15 20:45:48
             */

            private double temp;
            private double humidity;
            private double heartrate;
            private double latitude;
            private double longitude;
            private int stepnumber;
            private String time;

            public double getTemp() {
                return temp;
            }

            public void setTemp(double temp) {
                this.temp = temp;
            }

            public double getHumidity() {
                return humidity;
            }

            public void setHumidity(double humidity) {
                this.humidity = humidity;
            }

            public double getHeartrate() {
                return heartrate;
            }

            public void setHeartrate(double heartrate) {
                this.heartrate = heartrate;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public int getStepnumber() {
                return stepnumber;
            }

            public void setStepnumber(int stepnumber) {
                this.stepnumber = stepnumber;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
