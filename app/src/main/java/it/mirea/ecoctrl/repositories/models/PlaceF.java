package it.mirea.ecoctrl.repositories.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaceF implements Serializable {

    public String place_name;


    @Exclude
    private boolean mapResult;

    private static String callPath = "places";

    private String metanInfo;
    private String azdInfo;
    private String serdInfo;
    private double lat;
    private double lng;
    //private LatLng pointSee;
    private List<String> images;


    public PlaceF(){
        //id = UUID.randomUUID().toString();
       // placeList = new ArrayList<>();
        images = new ArrayList<>();
    }

    public PlaceF(String place_name, String metanInfo,
                  String serdInfo, String azdInfo, LatLng pointSee, boolean mapResult){

        this.place_name=place_name;
        this.metanInfo=metanInfo;
        this.serdInfo=serdInfo;
        this.azdInfo=azdInfo;
        //this.pointSee=pointSee;
        this.mapResult=mapResult;
    }

    public List<String> getImagesF() {
        return images;
    }

    public void setImagesF(List<String> images) {
        this.images = images;
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

    public boolean isMapResult() {
        return mapResult;
    }

    public void setMapResult(boolean mapResult) {
        this.mapResult = mapResult;
    }

    //public LatLng getPointSee() {
      //  return pointSee;
    //}

    //public void setPointSee(LatLng pointSee) {
      //  this.pointSee = pointSee;
    //}
}

