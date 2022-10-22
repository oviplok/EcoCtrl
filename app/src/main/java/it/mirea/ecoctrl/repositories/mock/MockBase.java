package it.mirea.ecoctrl.repositories.mock;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.models.PlaceF;

public class MockBase implements RepoTasks {
    MutableLiveData<List<Place>> data;
    List<Place> list;
    // MutableLiveData<Place> searchPlace;

    public MutableLiveData<List<Place>> getAllPlaces() {
        return data;
    }

    public MockBase() {
        list = new ArrayList<>();

        Place place1 = new Place();
        place1.setPlace_name("Dubai");
       // place1.setCreator(new Person("Леонид", "Шешуков"));
        place1.setMetanInfo("0.12");
        place1.setSerdInfo("0.33");
        place1.setAzdInfo("0.2");
        place1.setLat(25.216982);
        place1.setLng(55.170364);
        list.add(place1);

        Place place2 = new Place();
        place2.setPlace_name("Podolsk");
        place2.setMetanInfo("0.5");
        place2.setSerdInfo("0.6");
        place2.setAzdInfo("0.2002");
        place2.setLat(55.412902);
        place2.setLng(37.564411);
        list.add(place2);

        Place place3 = new Place();
        place3.setPlace_name("RTU MIREA");
        place3.setMetanInfo("0.13");
        place3.setSerdInfo("0.12");
        place3.setAzdInfo("0.20");
        place3.setLat(55.669911);
        place3.setLng(37.479842);
        list.add(place3);

        Place place4 = new Place();
        place4.setPlace_name("Dmitrievskogo");
        // place1.setCreator(new Person("Леонид", "Шешуков"));
        place4.setMetanInfo("0.2");
        place4.setSerdInfo("0.3");
        place4.setAzdInfo("0.4");
        place4.setLat(55.710630);
        place4.setLng(37.879742);
        list.add(place4);

        Place place5 = new Place();
        place5.setPlace_name("Nekrasovka");
        place5.setMetanInfo("0.5");
        place5.setSerdInfo("0.6");
        place5.setAzdInfo("0.77");
        place5.setLat(55.704756);
        place5.setLng(37.927272);
        list.add(place5);

        data = new MutableLiveData<>(list);
    }


    @Override
    public void addPlace(PlaceF placeF) {
        Place place = Place.convertFromFire(placeF);
        list.add(place);

        data.setValue(list);
    }

    @Override
    public void changePlace(PlaceF placeF) {
       // String check =placeF.getPlace_name();
       // Place place = Place.convertFromFire(placeF);
       // list.add(place);
       // data.setValue(list);
    }

    @Override
    public void deletePlace(Place place) {
        list.remove(place);

        data.setValue(list);
    }

    @Override
    public MutableLiveData<Place> findPlace(String id, LifecycleOwner owner) {
        MutableLiveData<Place> specificPlace = new MutableLiveData<>();

        data.observe(owner, (List<Place> places) -> {
            specificPlace.setValue(places.stream()
                    .filter(place -> id.equals(place.getPlace_name()))
                    .findAny()
                    .orElse(null)
            );
        });

        return specificPlace;
    }


}
