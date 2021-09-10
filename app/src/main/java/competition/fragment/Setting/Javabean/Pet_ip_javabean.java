package competition.fragment.Setting.Javabean;

import java.io.Serializable;
import java.util.List;

public class Pet_ip_javabean {

    /**
     * code : 0
     * msg :
     * data : {"pet":{"petId":"","petPortraitPath":"","petName":"","petBreed":"","petIntroduction":"","overt":"","userId":"","photos":[{"petPhotoId":0,"petPhotoUrl":""}]}}
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
        /**
         * pet : {"petId":"","petPortraitPath":"","petName":"","petBreed":"","petIntroduction":"","overt":"","userId":"","photos":[{"petPhotoId":0,"petPhotoUrl":""}]}
         */

        private PetBean pet;

        public PetBean getPet() {
            return pet;
        }

        public void setPet(PetBean pet) {
            this.pet = pet;
        }

        public static class PetBean implements Serializable {
            /**
             * petId :
             * petPortraitPath :
             * petName :
             * petBreed :
             * petIntroduction :
             * overt :
             * userId :
             * photos : [{"petPhotoId":0,"petPhotoUrl":""}]
             */

            private int petId;
            private String petPortraitPath;
            private String petName;
            private String petBreed;
            private String petIntroduction;
            private boolean overt;
            private int userId;
            private List<PhotosBean> photos;

            public int getPetId() {
                return petId;
            }

            public void setPetId(int petId) {
                this.petId = petId;
            }

            public String getPetPortraitPath() {
                return petPortraitPath;
            }

            public void setPetPortraitPath(String petPortraitPath) {
                this.petPortraitPath = petPortraitPath;
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

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public List<PhotosBean> getPhotos() {
                return photos;
            }

            public void setPhotos(List<PhotosBean> photos) {
                this.photos = photos;
            }

            public static class PhotosBean {
                /**
                 * petPhotoId : 0
                 * petPhotoUrl :
                 */

                private int petPhotoId;
                private String petPhotoUrl;

                public int getPetPhotoId() {
                    return petPhotoId;
                }

                public void setPetPhotoId(int petPhotoId) {
                    this.petPhotoId = petPhotoId;
                }

                public String getPetPhotoUrl() {
                    return petPhotoUrl;
                }

                public void setPetPhotoUrl(String petPhotoUrl) {
                    this.petPhotoUrl = petPhotoUrl;
                }
            }
        }
    }
}
