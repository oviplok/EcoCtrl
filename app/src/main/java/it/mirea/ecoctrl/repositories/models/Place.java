package it.mirea.ecoctrl.repositories.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.Gson;
import java.util.List;

@Entity(tableName = "places")
public class Place extends PlaceF{
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "place")
    public String place_name;

    @ColumnInfo(name = "met")
    public String metanInfo;

    @ColumnInfo(name = "azd")
    public String azdInfo;

    @ColumnInfo(name = "serd")
    public String serdInfo;

    @ColumnInfo(name = "lat")
    public double lat;

    @ColumnInfo(name = "lng")
    public double lng;

    @ColumnInfo(name = "images")
    private String images;

    public Place(){
    }

    public Place(String place_name, String metanInfo,
                 String serdInfo,String azdInfo,double lat,double lng){

        this.place_name=place_name;
        this.metanInfo=metanInfo;
        this.serdInfo=serdInfo;
        this.lat=lat;
        this.lng=lng;
        this.azdInfo=azdInfo;
    }

    public static Place convertFromFire(PlaceF placeF) {
        Place place= new Place();

        place.setPlace_name(placeF.getPlace_name());
        place.setLat(placeF.getLat());
        place.setLng(placeF.getLng());
        place.setAzdInfo(placeF.getAzdInfo());
        place.setMetanInfo(placeF.getMetanInfo());
        place.setSerdInfo(placeF.getSerdInfo());
        place.setImagesF(placeF.getImagesF());

        return place;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String imagesDTO) {
        this.images = imagesDTO;
        super.setImagesF(new Gson().fromJson(this.images, List.class));
    }

    @Override
    public List<String> getImagesF() {
        if (super.getImagesF() == null || super.getImagesF().isEmpty()) {
            super.setImagesF(new Gson().fromJson(this.images, List.class));
        }
        return super.getImagesF();
    }

    @Override
    public void setImagesF(List<String> images) {
        super.setImagesF(images);
        this.images = new Gson().toJson(images);
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

    public void setLatLng(double lat,double lng){
        this.lat=lat;
        this.lng=lng;
    }
}
