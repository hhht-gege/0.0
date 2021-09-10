package competition.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.petcollar.top:8083/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Retrofit getRetrofit(){
        return retrofit;
    }

}
