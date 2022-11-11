package it.mirea.ecoctrl.cutContent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IPtoLoc {
     @POST("api/4_1/rs/suggest/address")
     @Headers({
             "Content-Type: application/json",
             "Accept: application/json"
     })
    Call<IPtoLocation.AddressResponse> listAddresses(@Body IPtoLocation.AddressRequest addressRequest, @Header("Location") String concat);
    // @POST("api/4_1/rs/suggest/address")
   // @Headers({
   //         "Content-Type: application/json",
   //         "Accept: application/json"
   // })
   // Call<AddressAnalysis.AddressResponse> listAddresses(@Body AddressAnalysis.AddressRequest request, @Header("Authorization") String token);

}
