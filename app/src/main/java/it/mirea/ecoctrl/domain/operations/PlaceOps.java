package it.mirea.ecoctrl.domain.operations;

import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

import it.mirea.ecoctrl.domain.models.PlaceF;

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

    public static PlaceF insertInfo(String addPlace, String addMetan,
                                    String addSerd, String addAzd, String addLng, String addLat){

        PlaceF placeF = new PlaceF();
        placeF.setPlace_name(addPlace);
        placeF.setAzdInfo(addAzd);
        placeF.setMetanInfo(addMetan);
        placeF.setSerdInfo(addSerd);
       // placeF.setFav(fav);
       // if((addLat.equals("") || addLng.equals(""))){

        //}
       // else{
            double Lng = Double.parseDouble(addLng);
            double Lat = Double.parseDouble(addLat);
            placeF.setLat(Lat);
            placeF.setLng(Lng);
        //}
        Log.e("AddOper", placeF.getPlace_name());
        return placeF;

    }

}
