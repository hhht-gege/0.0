package competition.fragment.Search;


import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

import competition.Utils.API;
import competition.Utils.RetrofitManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;

public class UploadImags {
    private static final String TAG = "Uploadme";

    public Call<ResponseBody> uploadimags(List<String> paths,String topic,String content,String token){
        System.out.println("chong");
        API api = RetrofitManager.getRetrofit().create(API.class);
        Map<String,RequestBody> bodyMap=new HashMap<>();
        bodyMap.put("topic",RequestBody.create(null,topic));
        bodyMap.put("content",RequestBody.create(null,content));
        List<MultipartBody.Part> parts=new ArrayList<>();
        for (String s:paths
             ) {
            parts.add(ReturnPart(s,"pictures"));
        }
        return api.upload_imags(parts,bodyMap,token);
    }

    public static MultipartBody.Part ReturnPart(String path,String key){
        File file = new File(path);
        if(!file.exists()){
            Log.d(TAG, "uploadImags: 文件不存在");
        }else{
            Log.d(TAG, "uploadImags: 文件存在");
        }
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key,file.getName(),body);
        return part;
    }

}
