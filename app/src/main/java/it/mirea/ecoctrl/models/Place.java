package it.mirea.ecoctrl.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Place implements Serializable {
    private String place_name,metanInfo,serdInfo, azdInfo;
   // public GeoPoint geo;
    private double lat,lng;
    private static String callPath = "places";

    @Exclude
    private boolean mapResult;
    private LatLng pointSee; //= new LatLng(lat, lng);

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

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getMetanInfo() {
        return metanInfo;
    }

    public void setMetanInfo(String metanInfo) {
        this.metanInfo = metanInfo;
    }

    public String getSerdInfo() {
        return serdInfo;
    }

    public void setSerdInfo(String serdInfo) {
        this.serdInfo = serdInfo;
    }

    public String getAzdInfo() {
        return azdInfo;
    }

    public void setAzdInfo(String azdInfo) {
        this.azdInfo = azdInfo;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public static String getCallPath() {
        return callPath;
    }

   // public static void setCallPath(String callPath) {
     //   Place.callPath = callPath;
    //}

    public boolean isMapResult() {
        return mapResult;
    }

    public void setMapResult(boolean mapResult) {
        this.mapResult = mapResult;
    }

    public LatLng getPointSee() {
        return pointSee;
    }

    public void setPointSee(LatLng pointSee) {
        this.pointSee = pointSee;
    }
}
