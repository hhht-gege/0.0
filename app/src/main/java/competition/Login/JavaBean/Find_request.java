package competition.Login.JavaBean;

public class Find_request {

    /**
     * username : 用户名称
     */

    private String username;

   public Find_request(String username){
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
