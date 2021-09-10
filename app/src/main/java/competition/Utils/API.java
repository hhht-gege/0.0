package competition.Utils;

import java.util.List;
import java.util.Map;

import competition.Login.JavaBean.Find_JavaBean;
import competition.Login.JavaBean.Find_request;
import competition.Login.JavaBean.Findok_request;
import competition.Login.JavaBean.JavaBean;
import competition.Login.JavaBean.login_RequestBody;
import competition.fragment.Anim.JavaBean.AnimAdd_request;
import competition.fragment.Anim.JavaBean.railing_request;
import competition.fragment.Anim.JavaBean.warningtemp_request;
import competition.fragment.Me.JavaBean.profile_request;
import competition.fragment.Setting.Javabean.bindcollar_request;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    //忘记密码

    @Headers("Content-Type:application/json")
    @POST("email_check")
    Call<ResponseBody> post_find(@Body Find_request find_request);

    @Headers("Content-Type:application/json")
    @POST("password_reset")
    Call<ResponseBody> post_findok(@Body Findok_request findok_request);

    @Headers("Content-Type:application/json")
    @PATCH("settings/profile")
    Call<ResponseBody> change_message(@Body profile_request profile_request, @Header("token") String token);

    @Headers("Content-Type:application/json")
    @POST("pets")
    Call<ResponseBody> post_add(@Body AnimAdd_request animAdd_request,@Header("token") String token);

    @Headers("Content-Type:application/json")
    @GET("pets/view")
    Call<ResponseBody> get_addall(@Header("token") String token);

    @Headers("Content-Type:application/json")
    @GET("pets/{petId}")
    Call<ResponseBody> into_anim(@Header("token") String token,@Path("petId") int petId);


    @Headers("Content-Type:application/json")
    @DELETE("pets/{petId}")
    Call<ResponseBody> delete_anim(@Header("token") String token,@Path("petId") int petId);

    @Multipart
    @PATCH("pets/{petId}/avatar")
    Call<ResponseBody> upload_imag(@Part MultipartBody.Part image,@Path("petId") int petId,@Header("token") String token);

    @Multipart
    @PATCH("settings/avatar")
    Call<ResponseBody> upload_imag(@Part MultipartBody.Part image,@Header("token") String token);

    @Multipart
    @POST("dynamics")
    Call<ResponseBody> upload_imags(@Part List<MultipartBody.Part> parts, @PartMap Map<String ,RequestBody> bodyMap, @Header("token") String token);

    @Headers("Content-Type:application/json")
    @GET("dynamics/new")
    Call<ResponseBody> get_dynamics_top(@Query("current_dynamic_id") int current_dynamic_id, @Header("token") String token);

    @Headers("Content-Type:application/json")
    @GET("dynamics/history")
    Call<ResponseBody> get_dynamics_end(@Query("offset_dynamic_id")int offset_dynamic_id,@Header("token") String token);

    @Headers("Content-Type:application/json")
    @GET("collar/status/{ip}")
    Call<ResponseBody> collar_status(@Header("token") String token,@Path("ip") String ip);

    @Headers("Content-Type:application/json")
    @POST("collar/binding")
    Call<ResponseBody> bind_collar(@Header("token") String token,@Body bindcollar_request request);

    @Headers("Content-Type:application/json")
    @GET("collar/")
    Call<ResponseBody> get_collar(@Header("token") String token);

    @Headers("Content-Type:application/json")
    @GET("pets/{petId}")
    Call<ResponseBody> get_pet(@Header("token") String token,@Path("petId") int petId);

    @Headers("Content-Type:application/json")
    @PUT("railing/")
    Call<ResponseBody> create_railing(@Header("token")String token, @Body railing_request request);

    @Headers("Content-Type:application/json")
    @PUT("railing/temperature/")
    Call<ResponseBody> warning_temp(@Header("token")String token, @Body warningtemp_request request);

    @Headers("Content-Type:application/json")
    @GET("collar/history/{ip}/{day}")
    Call<ResponseBody> collar_history(@Header("token")String token,@Path("ip") String ip,@Path("day")String day);

    @Headers("Content-Type:application/json")
    @GET("pets/{petId}/collar_ip")
    Call<ResponseBody> get_pet_collar(@Header("token")String token,@Path("petId") int petId);

    @Multipart
    @POST("pets/{petId}/photos")
    Call<ResponseBody> upload_anim_imag(@Part List<MultipartBody.Part> parts,@Path("petId") int petId ,@Header("token") String token);

    @Headers("Content-Type:application/json")
    @GET("collar/getPetByIp/{ip}")
    Call<ResponseBody> getPetByIp(@Path("ip") String ip);

    @Headers("Content-Type:application/json")
    @PATCH("pets/{petId}/profile")
    Call<ResponseBody> anim_change(@Header("token") String token,@Body AnimAdd_request request,@Path("petId") int petId);

}
