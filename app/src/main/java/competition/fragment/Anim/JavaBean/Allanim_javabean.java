package competition.fragment.Anim.JavaBean;

import java.util.List;

public class Allanim_javabean {

    /**
     * code : 0
     * msg :
     * data : {"pets":[{"petId":0,"petName":"","petPortraitPath":"","overt":false}]}
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
        private List<PetsBean> pets;

        public List<PetsBean> getPets() {
            return pets;
        }

        public void setPets(List<PetsBean> pets) {
            this.pets = pets;
        }

        public static class PetsBean {
            /**
             * petId : 0
             * petName :
             * petPortraitPath :
             * overt : false
             */

            private int petId;
            private String petName;
            private String petPortraitPath;
            private String  petBreed;
            private boolean overt;

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

            public String getPetPortraitPath() {
                return petPortraitPath;
            }

            public void setPetPortraitPath(String petPortraitPath) {
                this.petPortraitPath = petPortraitPath;
            }

            public String getPetBreed() {
                return petBreed;
            }

            public void setPetBreed(String petBreed) {
                this.petBreed = petBreed;
            }

            public boolean isOvert() {
                return overt;
            }

            public void setOvert(boolean overt) {
                this.overt = overt;
            }
        }
    }
}
