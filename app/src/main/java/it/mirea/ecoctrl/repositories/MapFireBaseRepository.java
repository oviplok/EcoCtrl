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
    private CollectionReference mapCol = db.collection(Place.getCallPath());
    private Place place = new Place();


    public MutableLiveData<Place> Search(String search) {
        MutableLiveData<Place> mapLiveData = new MutableLiveData<>();

        mapCol.document(search).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    place.setPlace_name(documentSnapshot.getString("place"));// = documentSnapshot.getString("place");
                    place.setMetanInfo(documentSnapshot.getString("met"));// = documentSnapshot.getString("met");
                    place.setSerdInfo(documentSnapshot.getString("serd"));// = documentSnapshot.getString("serd");
                    place.setAzdInfo(documentSnapshot.getString("azd"));// = documentSnapshot.getString("azd");
                    GeoPoint geo = documentSnapshot.getGeoPoint("point");
                    place.setLat(geo.getLatitude());//lat = geo.getLatitude();
                    place.setLng(geo.getLongitude());//lng = geo.getLongitude();
                    LatLng pointSee=new LatLng(place.getLat(),place.getLng());
                    place.setPointSee(pointSee);

                    place.setMapResult(true);// = true;
                    mapLiveData.setValue(place);
                } else {
                    place.setMapResult(false);// = false;
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
                                            place.setMapResult(true);// = true;
                                            mapLiveData.setValue(place);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    place.setMapResult(false);// = false;
                                    mapLiveData.setValue(place);
                                }
                            });
                        } else {
                            place.setMapResult(false);// = false;
                            mapLiveData.setValue(place);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                place.setMapResult(false);// = false;
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
                            place.setMapResult(false);// = false;
                            mapLiveData.setValue(place);
                        }
                        else{
                            mapCol.document(addPlace).set(infoChangeMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            place.setMapResult(true);// = true;
                                            mapLiveData.setValue(place);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    place.setMapResult(false);// = false;
                                    mapLiveData.setValue(place);
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                place.setMapResult(false);// = false;
                mapLiveData.setValue(place);
            }
        });
        return mapLiveData;
    }
}
