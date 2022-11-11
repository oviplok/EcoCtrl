package it.mirea.ecoctrl.repositories.network.adressLogic;

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
  //  LocationApiService apiService = new getGeoApiService();
    private IPtoLoc api;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String ip= "46.138.164.145" ;
    public String getApi(){
        return "https://api.apilayer.com/ip_to_location/";
        //return "http://ip-api.com/";
    }


    public LocationApiService getGeoApiService() {
        Log.e("IP","GEO BUILDER");
        return new Retrofit.Builder()
                .baseUrl(getApi())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LocationApiService.class);
    }

    public MutableLiveData<GeoResponse> getAddressesFromIP() {

        MutableLiveData<GeoResponse> address = new MutableLiveData<>();
        Log.e("IP","GET ADDRESSES FROM IP");
        LocationApiService apiService = getGeoApiService();
        apiService.getLocation("apikey:".concat(BuildConfig.IP_TO_LOCATION_API_KEY)).enqueue(new Callback<GeoResponse>() {
            @Override
            public void onResponse(Call<GeoResponse> call, Response<GeoResponse> response) {
              //  address.setValue();
                Log.e("IP","ON RESPONSE");
                Log.e("IP",response.toString());
                if (response.isSuccessful() && response.body() != null) {
                   // GeoResponse geoResponse =new GeoResponse();
                    //Log.e("IP",response.toString());
                    geoResponse.setCity(response.body().getCity());
                    geoResponse.setLatitude(response.body().getLatitude());
                    geoResponse.setLongitude(response.body().getLongitude());
                    //geoResponse.setCountry(ip);
                    address.setValue(geoResponse);
                    done=true;
                   // return address;
                    // address.setValue(response.body().);
                    //address.setValue(response.body().suggestions.stream().map(address -> address.value).collect(Collectors.toList()));
                }
                //response.body().getLatitude();
                //response.body().getLongitude();
            }

            @Override
            public void onFailure(Call<GeoResponse> call, Throwable t) {
                t.getMessage();
                Log.e("IP","ON FAILURE");
                done=true;
            }

        });
        Log.e("IP","RETURN");
       // address.setValue(geoResponse);
        return address;
    }

}
