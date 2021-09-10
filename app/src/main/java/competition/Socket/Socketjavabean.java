package competition.Socket;

public class Socketjavabean {

    /**
     * fromRailing : {"petId":123,"railingId":9,"railingLatitude":28.06823,"railingLongitude":113.00585,"railingRadius":3.3333333333333335}
     * ip : 106.54.174.38
     * recentCollarMsg : {"heartrate":90,"humidity":49,"latitude":28.52415800978249,"longitude":113.93931069640912,"stepnumber":0,"temp":26,"time":"2021-05-16 22:01:20"}
     * warningTime : 2021-05-16 10:01:20
     */

    private FromRailingBean fromRailing;
    private String ip;
    private RecentCollarMsgBean recentCollarMsg;
    private String warningTime;

    public FromRailingBean getFromRailing() {
        return fromRailing;
    }

    public void setFromRailing(FromRailingBean fromRailing) {
        this.fromRailing = fromRailing;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public RecentCollarMsgBean getRecentCollarMsg() {
        return recentCollarMsg;
    }

    public void setRecentCollarMsg(RecentCollarMsgBean recentCollarMsg) {
        this.recentCollarMsg = recentCollarMsg;
    }

    public String getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(String warningTime) {
        this.warningTime = warningTime;
    }

    public static class FromRailingBean {
        /**
         * petId : 123
         * railingId : 9
         * railingLatitude : 28.06823
         * railingLongitude : 113.00585
         * railingRadius : 3.3333333333333335
         */

        private int petId;
        private int railingId;
        private double railingLatitude;
        private double railingLongitude;
        private double railingRadius;

        public int getPetId() {
            return petId;
        }

        public void setPetId(int petId) {
            this.petId = petId;
        }

        public int getRailingId() {
            return railingId;
        }

        public void setRailingId(int railingId) {
            this.railingId = railingId;
        }

        public double getRailingLatitude() {
            return railingLatitude;
        }

        public void setRailingLatitude(double railingLatitude) {
            this.railingLatitude = railingLatitude;
        }

        public double getRailingLongitude() {
            return railingLongitude;
        }

        public void setRailingLongitude(double railingLongitude) {
            this.railingLongitude = railingLongitude;
        }

        public double getRailingRadius() {
            return railingRadius;
        }

        public void setRailingRadius(double railingRadius) {
            this.railingRadius = railingRadius;
        }
    }

    public static class RecentCollarMsgBean {
        /**
         * heartrate : 90
         * humidity : 49
         * latitude : 28.52415800978249
         * longitude : 113.93931069640912
         * stepnumber : 0
         * temp : 26
         * time : 2021-05-16 22:01:20
         */

        private int heartrate;
        private int humidity;
        private double latitude;
        private double longitude;
        private int stepnumber;
        private int temp;
        private String time;

        public int getHeartrate() {
            return heartrate;
        }

        public void setHeartrate(int heartrate) {
            this.heartrate = heartrate;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
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

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
