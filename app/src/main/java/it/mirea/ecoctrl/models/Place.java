package it.mirea.ecoctrl.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Place implements Serializable {
    public String place_name,metanInfo,serdInfo, azdInfo;
   // public GeoPoint geo;
    public double lat,lng;
    public static String callPath = "places";

    @Exclude
    public boolean mapResult;
    public LatLng pointSee; //= new LatLng(lat, lng);

    public Place(){
    }

    public Place(String place_name, String metanInfo,
                 String serdInfo,String azdInfo,LatLng pointSee,boolean mapResult){
        this.place_name=place_name;
        this.metanInfo=metanInfo;
        this.serdInfo=serdInfo;
        this.azdInfo=azdInfo;
        this.pointSee=pointSee;
        this.mapResult=mapResult;
    }
}
