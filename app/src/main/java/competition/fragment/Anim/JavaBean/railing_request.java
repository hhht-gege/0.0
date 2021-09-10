package competition.fragment.Anim.JavaBean;

public class railing_request {



    /**
     * petId : 1
     * railingLongitude : 134
     * railingLatitude : 23
     * railingRadius : 2.2
     */

    private int petId;
    private double railingLongitude;
    private double railingLatitude;
    private double railingRadius;

    public railing_request(int petId, double railingLongitude, double railingLatitude, double railingRadius) {
        this.petId = petId;
        this.railingLongitude = railingLongitude;
        this.railingLatitude = railingLatitude;
        this.railingRadius = railingRadius;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public double getRailingLongitude() {
        return railingLongitude;
    }

    public void setRailingLongitude(double railingLongitude) {
        this.railingLongitude = railingLongitude;
    }

    public double getRailingLatitude() {
        return railingLatitude;
    }

    public void setRailingLatitude(double railingLatitude) {
        this.railingLatitude = railingLatitude;
    }

    public double getRailingRadius() {
        return railingRadius;
    }

    public void setRailingRadius(double railingRadius) {
        this.railingRadius = railingRadius;
    }
}
