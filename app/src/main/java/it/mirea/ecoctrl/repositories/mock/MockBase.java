package it.mirea.ecoctrl.repositories.mock;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.models.PlaceDTO;
import it.mirea.ecoctrl.domain.models.Place;
import it.mirea.ecoctrl.repositories.models.UserDTO;

public class MockBase implements RepoTasks {
    MutableLiveData<List<PlaceDTO>> data;
    List<PlaceDTO> list;
    // MutableLiveData<Place> searchPlace;

    public MutableLiveData<List<PlaceDTO>> getAllPlaces() {
        return data;
    }

    public MockBase() {
        list = new ArrayList<>();

        PlaceDTO placeDTO1 = new PlaceDTO();
        placeDTO1.setPlace_name("Dubai");
        placeDTO1.setMetanInfo("0.12");
        placeDTO1.setSerdInfo("0.33");
        placeDTO1.setAzdInfo("0.2");
        placeDTO1.setLat(25.216982);
        placeDTO1.setLng(55.170364);
        list.add(placeDTO1);

        PlaceDTO placeDTO2 = new PlaceDTO();
        placeDTO2.setPlace_name("Podolsk");
        placeDTO2.setMetanInfo("0.5");
        placeDTO2.setSerdInfo("0.6");
        placeDTO2.setAzdInfo("0.2002");
        placeDTO2.setLat(55.412902);
        placeDTO2.setLng(37.564411);
        list.add(placeDTO2);

        PlaceDTO placeDTO3 = new PlaceDTO();
        placeDTO3.setPlace_name("RTU MIREA");
        placeDTO3.setMetanInfo("0.13");
        placeDTO3.setSerdInfo("0.12");
        placeDTO3.setAzdInfo("0.20");
        placeDTO3.setLat(55.669911);
        placeDTO3.setLng(37.479842);
        list.add(placeDTO3);

        PlaceDTO placeDTO4 = new PlaceDTO();
        placeDTO4.setPlace_name("Dmitrievskogo");
        placeDTO4.setMetanInfo("0.2");
        placeDTO4.setSerdInfo("0.3");
        placeDTO4.setAzdInfo("0.4");
        placeDTO4.setLat(55.710630);
        placeDTO4.setLng(37.879742);
        list.add(placeDTO4);

        PlaceDTO placeDTO5 = new PlaceDTO();
        placeDTO5.setPlace_name("Nekrasovka");
        placeDTO5.setMetanInfo("0.5");
        placeDTO5.setSerdInfo("0.6");
        placeDTO5.setAzdInfo("0.77");
        placeDTO5.setLat(55.704756);
        placeDTO5.setLng(37.927272);
        list.add(placeDTO5);

        data = new MutableLiveData<>(list);
    }


    @Override
    public void addPlace(Place place) {
        PlaceDTO placeDTO = PlaceDTO.convertFromPlace(place);
        list.add(placeDTO);
        data.setValue(list);
    }

    @Override
    public void changePlace(Place place) {
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
    public <T extends User> LiveData<T> findUser(String email, LifecycleOwner owner) {
        return null;
    }

    @Override
    public <T extends User> LiveData<T> findUser(String email, String password, LifecycleOwner owner) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public <T extends PlaceDTO> LiveData<List<User>> getAllUsers() {
        return null;
    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public MutableLiveData<PlaceDTO> findPlace(String id, LifecycleOwner owner) {
        MutableLiveData<PlaceDTO> specificPlace = new MutableLiveData<>();

        data.observe(owner, (List<PlaceDTO> placeDTOS) -> {
            specificPlace.setValue(placeDTOS.stream()
                    .filter(place -> id.equals(place.getPlace_name()))
                    .findAny()
                    .orElse(null)
            );
        });

        return specificPlace;
    }


}
