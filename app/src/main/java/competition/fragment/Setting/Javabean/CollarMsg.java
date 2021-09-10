package competition.fragment.Setting.Javabean;

public class CollarMsg {
    private double temp;
    private double humidity;
    private double heartrate;
    private double latitude;
    private double longitude;
    private Integer stepnumber;
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

    public Integer getStepnumber() {
        return stepnumber;
    }

    public void setStepnumber(Integer stepnumber) {
        this.stepnumber = stepnumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
