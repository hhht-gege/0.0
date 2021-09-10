package competition.fragment.Me;

import android.util.Log;

import java.io.File;

import competition.Utils.API;
import competition.Utils.RetrofitManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class Uploadme {
    private static final String TAG = "Uploadme";

    public Call<ResponseBody> uploadImag(String path,  String token){

        API api = RetrofitManager.getRetrofit().create(API.class);
        File file=new File(path);
        if(!file.exists()){
            Log.d(TAG, "uploadImag: 文件不存在");
        }else{
            Log.d(TAG, "uploadImag: 文件存在");
        }
        RequestBody body=RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part=MultipartBody.Part.createFormData("image",file.getName(),body);
        return api.upload_imag(part,token);
    }
}
