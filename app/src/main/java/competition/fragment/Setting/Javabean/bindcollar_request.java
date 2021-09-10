package competition.fragment.Setting.Javabean;

public class bindcollar_request {

    /**
     * collarId :
     * collarIntroduction :
     * petId :
     */

    private String collarId;
    private String collarIntroduction;
    private int petId;

    public bindcollar_request(String collarId, String collarIntroduction, int petId) {
        this.collarId = collarId;
        this.collarIntroduction = collarIntroduction;
        this.petId = petId;
    }

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

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }
}
