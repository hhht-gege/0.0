package competition.fragment.Setting;

public class Setitem {
    /**
     * collarId : 127.0.0.1
     * collarIntroduction : 111
     * userId : 19
     * petId : 0
     */

    private String collarId;
    private String collarIntroduction;
    private int userId;
    private int petId;
    private String url;
    private String name;
    private int socket;

    public Setitem(String collarId, String collarIntroduction, int userId, int petId) {
        this.collarId = collarId;
        this.collarIntroduction = collarIntroduction;
        this.userId = userId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSocket() {
        return socket;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }
}
