package it.mirea.ecoctrl.repositories.network.authLogic;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface VKApiService {
    @GET("account.getProfileInfo")
    Call<VKApiLogic.APIResponse> getPersonInfo(@QueryMap Map<String, String> api_info, @Query("access_token") String access_token);
}
