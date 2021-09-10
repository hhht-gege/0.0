package competition.fragment.Anim.JavaBean;

public class warningtemp_request {

    /**
     * limitMax : 7927938709735896
     * limitMin : -7.508078830367964E14
     * petId : -8465641352168488
     */

    private double limitMax;
    private double limitMin;
    private int petId;

    public warningtemp_request(double limitMax, double limitMin, int petId) {
        this.limitMax = limitMax;
        this.limitMin = limitMin;
        this.petId = petId;
    }

    public double getLimitMax() {
        return limitMax;
    }

    public void setLimitMax(double limitMax) {
        this.limitMax = limitMax;
    }

    public double getLimitMin() {
        return limitMin;
    }

    public void setLimitMin(double limitMin) {
        this.limitMin = limitMin;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }
}
