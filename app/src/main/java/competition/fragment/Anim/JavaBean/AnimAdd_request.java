package competition.fragment.Anim.JavaBean;

public class AnimAdd_request {


    /**
     * petName :
     * petBreed :
     * petIntroduction :
     * overt : false
     */

    private String petName;
    private String petBreed;
    private String petIntroduction;
    private boolean overt;

    public AnimAdd_request(String petName, String petBreed, String petIntroduction, boolean overt) {
        this.petName = petName;
        this.petBreed = petBreed;
        this.petIntroduction = petIntroduction;
        this.overt = overt;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getPetIntroduction() {
        return petIntroduction;
    }

    public void setPetIntroduction(String petIntroduction) {
        this.petIntroduction = petIntroduction;
    }

    public boolean isOvert() {
        return overt;
    }

    public void setOvert(boolean overt) {
        this.overt = overt;
    }
}
