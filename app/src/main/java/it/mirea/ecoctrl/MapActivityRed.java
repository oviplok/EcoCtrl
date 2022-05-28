package it.mirea.ecoctrl;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
////
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
///
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
///
import it.mirea.ecoctrl.databinding.ActivityMapRedBinding;


public class MapActivityRed extends FragmentActivity implements OnMapReadyCallback {
    RelativeLayout red;

    String place = "place";
    String metanInfo = "met";
    String azdInfo = "azd";
    String serdInfo = "serd";
    String point = "point";
    String search;

    Button show;
    EditText find;
    TextView infoPlace;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mapCol = db.collection("places");
    //DocumentReference mapDoc = db.collection("places").document("РТУ МИРЭА");


    private GoogleMap mMap;
    private ActivityMapRedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapRedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        red = findViewById(R.id.red_action);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        infoPlace = findViewById(R.id.infoPlaces);
        show = findViewById(R.id.showPoint);
        find = findViewById(R.id.finder);
        // Начальное положение карты, не работает (удалено)
       /* mapDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){


                }
                else{
                    infoPlace.setTextColor(Color.parseColor("#CC0000"));
                    infoPlace.setTextSize(30);
                    infoPlace.setText("Ничего не найдено...");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                infoPlace.setTextColor(Color.parseColor("#CC0000"));
                infoPlace.setTextSize(30);
                infoPlace.setText("Сервер не работает...");
            }
        });*/
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPoint();
            }
        });

    }
    //Работа поиска
    private void showPoint() {
        if (TextUtils.isEmpty(find.getText().toString())) {
            infoPlace.setTextColor(Color.parseColor("#CC0000"));
            infoPlace.setTextSize(30);
            infoPlace.setText("Введите название в поиск!");
        }
        else{
            search = find.getText().toString();
            mapCol.document(search).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")////////?
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        infoPlace.setTextColor(Color.parseColor("#FF000000"));
                        infoPlace.setTextSize(20);
                        String placeSee = documentSnapshot.getString(place);
                        String infoMet = documentSnapshot.getString(metanInfo);
                        String infoSerd = documentSnapshot.getString(serdInfo);
                        String infoAzd = documentSnapshot.getString(azdInfo);
                        GeoPoint Geo = documentSnapshot.getGeoPoint(point);
                        double lat = Geo.getLatitude();
                        double lng = Geo.getLongitude();
                        LatLng pointSee = new LatLng(lat, lng);
                        infoPlace.setText("Метан: "+infoMet+
                                "\nСеры диоксид: "+infoSerd+"\nАзота диоксид: "+infoAzd);
                        float zoomLevel = 16.0f;
                        mMap.addMarker(new MarkerOptions().position(pointSee).title(placeSee));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointSee,zoomLevel));
                    }
                    else{
                        infoPlace.setTextColor(Color.parseColor("#CC0000"));
                        infoPlace.setTextSize(30);
                        infoPlace.setText("Ничего не найдено...");
                    }
                }
            });
        }
    }

}