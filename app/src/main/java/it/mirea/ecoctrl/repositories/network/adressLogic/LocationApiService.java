package it.mirea.ecoctrl.repositories.network.adressLogic;

import android.annotation.TargetApi;

import it.mirea.ecoctrl.BuildConfig;
import it.mirea.ecoctrl.repositories.models.GeoResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface LocationApiService {
    /*@Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })*/
    @GET("46.138.164.145")
    Call<GeoResponse> getLocation(@Header("apikey:") String key);
}
