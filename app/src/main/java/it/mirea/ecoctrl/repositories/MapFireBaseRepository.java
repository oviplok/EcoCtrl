package it.mirea.ecoctrl.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

import it.mirea.ecoctrl.models.Place;

public class MapFireBaseRepository {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference places;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mapCol = db.collection(Place.callPath);
    private Place place = new Place();


    public MutableLiveData<Place> Search(String search) {
        MutableLiveData<Place> mapLiveData = new MutableLiveData<>();

        mapCol.document(search).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    place.place_name = documentSnapshot.getString("place");
                    place.metanInfo = documentSnapshot.getString("met");
                    place.serdInfo = documentSnapshot.getString("serd");
                    place.azdInfo = documentSnapshot.getString("azd");
                    GeoPoint geo = documentSnapshot.getGeoPoint("point");
                    place.lat = geo.getLatitude();
                    place.lng = geo.getLongitude();
                    place.pointSee=new LatLng(place.lat,place.lng);
                    place.mapResult = true;
                    mapLiveData.setValue(place);
                } else {
                    place.mapResult = false;
                    mapLiveData.setValue(place);
                }
            }
        });
        return mapLiveData;

    }

    public MutableLiveData<Place> Change(String chPlace, Map<String, Object> infoChangeMap) {
        MutableLiveData<Place> mapLiveData = new MutableLiveData<>();

        mapCol.document(chPlace).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            GeoPoint Geo = documentSnapshot.getGeoPoint("point");
                            infoChangeMap.put("point", Geo);
                            mapCol.document(chPlace).set(infoChangeMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            place.mapResult = true;
                                            mapLiveData.setValue(place);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    place.mapResult = false;
                                    mapLiveData.setValue(place);
                                }
                            });
                        } else {
                            place.mapResult = false;
                            mapLiveData.setValue(place);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                place.mapResult = false;
                mapLiveData.setValue(place);
            }
        });
        return mapLiveData;
    }

    public MutableLiveData<Place> Add(String addPlace, Map<String, Object> infoChangeMap) {
        MutableLiveData<Place> mapLiveData = new MutableLiveData<>();
        mapCol.document(addPlace).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            place.mapResult = false;
                            mapLiveData.setValue(place);
                        }
                        else{
                            mapCol.document(addPlace).set(infoChangeMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            place.mapResult = true;
                                            mapLiveData.setValue(place);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    place.mapResult = false;
                                    mapLiveData.setValue(place);
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                place.mapResult = false;
                mapLiveData.setValue(place);
            }
        });
        return mapLiveData;
    }
}
