package it.mirea.ecoctrl.cutContent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.stream.Collectors;

import it.mirea.ecoctrl.BuildConfig;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IPtoLocation {

     private IPtoLoc api;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String ip="46.138.164.145" ;


     public IPtoLocation(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.apilayer.com/ip_to_location/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(IPtoLoc.class);
    }

    public LiveData<List<String>> getAddressesFromIP(String pattern) {
        MutableLiveData<List<String>> address = new MutableLiveData<>();

        api.listAddresses(new AddressRequest(pattern), "Token ".concat(BuildConfig.IP_TO_LOCATION_API_KEY)).enqueue(new retrofit2.Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                   // address.setValue(response.body().);
                    address.setValue(response.body().suggestions.stream().map(address -> address.value).collect(Collectors.toList()));
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {

            }
        });

        return address;
    }

    static class AddressRequest{
        int count;
        String query;

        public AddressRequest(String query) {
            this.count = 5;
            this.query = query;
        }
    }

    static class AddressResponse{

        static class Address{
            String value;
            String unrestricted_value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getUnrestricted_value() {
                return unrestricted_value;
            }

            public void setUnrestricted_value(String unrestricted_value) {
                this.unrestricted_value = unrestricted_value;
            }
        }

        List<Address> suggestions;

        public List<Address> getSuggestions() {
            return suggestions;
        }

        public void setSuggestions(List<Address> suggestions) {
            this.suggestions = suggestions;
        }
    }

}
