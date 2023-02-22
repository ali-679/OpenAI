package ir.eyrsa.app.openai.Retrofit;

import java.util.HashMap;

import ir.eyrsa.app.openai.Model.BodyRecieveModel;
import ir.eyrsa.app.openai.Model.BodySendModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface IRetrofit {

//    @Headers({"Authorization: Bearer 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"})

//    @POST("completions")
//    Call<ResponseBody> ai(@HeaderMap HashMap<String,String> header, @Body MyModel body);

    @POST("completions")
    Call<BodyRecieveModel> ai(@HeaderMap HashMap<String,String> header, @Body BodySendModel body);

//    @POST("completions")
//    Call<BodyRecieveModels> a(@HeaderMap HashMap<String,String> header, @Body BodySendModel body);


}
