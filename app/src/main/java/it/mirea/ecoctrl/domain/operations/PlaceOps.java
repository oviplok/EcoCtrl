package it.mirea.ecoctrl.domain.operations;

import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.mirea.ecoctrl.domain.models.Place;

public class PlaceOps {

    public Map<String, Object> MakeMap(String addPlace, String addMetan,
                                       String addSerd, String addAzd, String addLng, String addLat){

        Map<String, Object> InfoChangeMap = new HashMap<>();
        if((addLat.equals("") || addLng.equals(""))){
            InfoChangeMap.put("place", addPlace);
            InfoChangeMap.put("met", addMetan);
            InfoChangeMap.put("serd", addSerd);
            InfoChangeMap.put("azd", addAzd);
        }
        else{
            double Lng = Double.parseDouble(addLng);
            double Lat = Double.parseDouble(addLat);
            GeoPoint Geo= new GeoPoint(Lat,Lng);
            InfoChangeMap.put("place", addPlace);
            InfoChangeMap.put("met", addMetan);
            InfoChangeMap.put("serd", addSerd);
            InfoChangeMap.put("azd", addAzd);
            InfoChangeMap.put("point", Geo);
        }

        return InfoChangeMap;
    }

    public static Place insertInfo(String addPlace, String addMetan,
                                   String addSerd, String addAzd, String addLng, String addLat, List<String> addImage){

        Place place = new Place();
        place.setPlace_name(addPlace);
        place.setAzdInfo(addAzd);
        place.setMetanInfo(addMetan);
        place.setSerdInfo(addSerd);
        place.setImagesF(addImage);
       // placeF.setFav(fav);
       // if((addLat.equals("") || addLng.equals(""))){

        //}
       // else{
            double Lng = Double.parseDouble(addLng);
            double Lat = Double.parseDouble(addLat);
            place.setLat(Lat);
            place.setLng(Lng);
        //}
        Log.e("AddOper", place.getPlace_name());
        return place;

    }

}
