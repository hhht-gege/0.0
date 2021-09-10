package competition.fragment.Anim.JavaBean;

import java.io.Serializable;
import java.util.List;

public class Anim_javabean {

    /**
     * code : 0
     * msg :
     * data : {"pet":{"petId":"","petPortraitPath":"","petName":"","petBreed":"","petIntroduction":"","overt":"","userId":"","photos":[{"petPhotoId":0,"petPhotoUrl":""}]},"railings":[{"railingId":"","railingLongitude":"","railingLatitude":"","railingRadius":""}]}
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
         * railings : [{"railingId":"","railingLongitude":"","railingLatitude":"","railingRadius":""}]
         */

        private PetBean pet;

        public PetBean getPet() {
            return pet;
        }

        public void setPet(PetBean pet) {
            this.pet = pet;
        }

        public static class PetBean {
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
            private List<RailingsBean> railings;

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

            public List<RailingsBean> getRailings() {
                return railings;
            }

            public void setRailings(List<RailingsBean> railings) {
                this.railings = railings;
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
            public static class RailingsBean implements Serializable {
                /**
                 * railingId :
                 * railingLongitude :
                 * railingLatitude :
                 * railingRadius :
                 */

                private int railingId;
                private String railingLongitude;
                private String railingLatitude;
                private String railingRadius;



                public int getRailingId() {
                    return railingId;
                }

                public void setRailingId(int railingId) {
                    this.railingId = railingId;
                }

                public String getRailingLongitude() {
                    return railingLongitude;
                }

                public void setRailingLongitude(String railingLongitude) {
                    this.railingLongitude = railingLongitude;
                }

                public String getRailingLatitude() {
                    return railingLatitude;
                }

                public void setRailingLatitude(String railingLatitude) {
                    this.railingLatitude = railingLatitude;
                }

                public String getRailingRadius() {
                    return railingRadius;
                }

                public void setRailingRadius(String railingRadius) {
                    this.railingRadius = railingRadius;
                }
            }
        }

    }
}
