package it.mirea.ecoctrl.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

import it.mirea.ecoctrl.models.Place;
import it.mirea.ecoctrl.repositories.MapFireBaseRepository;

public class MapViewModel extends AndroidViewModel {

    private MapFireBaseRepository mapFireBaseRepository;
    public LiveData<Place> placeLiveData;

    public MapViewModel(@NonNull Application application) {
        super(application);
        mapFireBaseRepository = new MapFireBaseRepository();
    }

    public void showPoint(String search) {
        placeLiveData = mapFireBaseRepository.Search(search);
    }


    public void changePoint(String chPlace, String chMetan, String chSerd, String chAzd) {
        Map<String, Object> InfoChangeMap = new HashMap<>();
        InfoChangeMap.put("place", chPlace);
        InfoChangeMap.put("met", chMetan);
        InfoChangeMap.put("serd", chSerd);
        InfoChangeMap.put("azd", chAzd);
        placeLiveData = mapFireBaseRepository.Change(chPlace,InfoChangeMap);
    }

    public void AddPoint(String addPlace, String addMetan, String addSerd, String addAzd, String addLng, String addLat) {
        double Lng = Double.parseDouble(addLng);
        double Lat = Double.parseDouble(addLat);
        GeoPoint Geo= new GeoPoint(Lat,Lng);
        Map<String, Object> InfoChangeMap = new HashMap<>();
        InfoChangeMap.put("place", addPlace);
        InfoChangeMap.put("met", addMetan);
        InfoChangeMap.put("serd", addSerd);
        InfoChangeMap.put("azd", addAzd);
        InfoChangeMap.put("point", Geo);

        placeLiveData = mapFireBaseRepository.Add(addPlace,InfoChangeMap);
    }
}
