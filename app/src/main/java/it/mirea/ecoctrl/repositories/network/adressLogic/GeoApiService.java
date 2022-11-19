package it.mirea.ecoctrl.repositories.network.adressLogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import it.mirea.ecoctrl.BuildConfig;
import it.mirea.ecoctrl.cutContent.IPtoLoc;
import it.mirea.ecoctrl.repositories.models.GeoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoApiService {
    public boolean done;
    GeoResponse geoResponse =new GeoResponse();
    private LocationApiService api;
    public String getIp() {
        Log.i("IP","IP: "+ip);
        return ip;

    }

    public void setIp(String ip) {
        this.ip = ip;
        Log.e("IP",this.ip);
    }

    public String ip;//= "46.138.164.145" ;
    public String getApi(){
        return "https://api.apilayer.com/ip_to_location/";
    }


    public GeoApiService() {
        Log.i("IP","GEO BUILDER");

         Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(getApi())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
           api = retrofit.create(LocationApiService.class);
    }

    public MutableLiveData<GeoResponse> getAddressesFromIP(String ip) {
        setIp("46.138.164.145");
        MutableLiveData<GeoResponse> address = new MutableLiveData<>();
        Log.e("IP","GET ADDRESSES FROM IP");
        api.getNewLocation(getIp(), BuildConfig.IP_TO_LOCATION_API_KEY).enqueue(
                new Callback<GeoResponse>() {
                    @Override
                    public void onResponse(Call<GeoResponse> call, Response<GeoResponse> response) {
                        //  address.setValue();
                        Log.i("IP","ON RESPONSE");
                        Log.i("IP",response.toString());
                        if (response.isSuccessful() && response.body() != null) {

                            Log.e("IP",response.body().toString());
                            geoResponse.setCity(response.body().getCity());
                            geoResponse.setLatitude(response.body().getLatitude());
                            geoResponse.setLongitude(response.body().getLongitude());

                            address.setValue(geoResponse);

                        }
                    }

                    @Override
                    public void onFailure(Call<GeoResponse> call, Throwable t) {
                        t.getMessage();
                        Log.e("IP","ON FAILURE");
                        done=true;
                    }

                }
        );

        Log.i("IP","RETURN");
        return address;
    }

}
