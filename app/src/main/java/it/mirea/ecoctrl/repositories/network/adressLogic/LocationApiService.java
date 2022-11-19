package it.mirea.ecoctrl.repositories.network.adressLogic;

import android.annotation.TargetApi;

import it.mirea.ecoctrl.BuildConfig;
import it.mirea.ecoctrl.repositories.models.GeoResponse;
import it.mirea.ecoctrl.views.activities.MapActivity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface LocationApiService {

    @GET("{ip}")
    Call<GeoResponse> getNewLocation(@Path("ip") String ip, @Query("apikey") String apiKey);
}
