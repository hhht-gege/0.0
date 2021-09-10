package competition.fragment.Anim;

import android.content.BroadcastReceiver;

public class Anim {


    /**
     * petName : 牲畜名称,4-16位, 支持中英文、数字( 后端会确保牲畜名称唯一 )
     * petSpecies : 牲畜品种
     * petIntroduction : 牲畜介绍, 在255个字符内 ( 为null时, 将会使用默认值--"~这只牲畜还没有介绍哦~ )
     * isPetOvert : 牲畜是否公开
     */
    private int petId;
    private String petName;
    private String petBreed;
    private String petPortraitPath;
    private boolean overt;


    public Anim(int petId,String petName,String petBreed,String petPortraitPath,boolean overt){
        this.petId=petId;
        this.petName=petName;
        this.petBreed=petBreed;
        this.petPortraitPath=petPortraitPath;
        this.overt=overt;
    }
    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
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

    public String getPetPortraitPath() {
        return petPortraitPath;
    }

    public void setPetPortraitPath(String petPortraitPath) {
        this.petPortraitPath = petPortraitPath;
    }

    public boolean isOvert() {
        return overt;
    }

    public void setOvert(boolean overt) {
        this.overt = overt;
    }
}
