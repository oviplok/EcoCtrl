package it.mirea.ecoctrl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

import it.mirea.ecoctrl.databinding.ActivityMapRedBinding;


public class MapActivityRed extends FragmentActivity implements OnMapReadyCallback {
    RelativeLayout red;
    String place = "place";
    String metanInfo = "met";
    String azdInfo = "azd";
    String serdInfo = "serd";
    String point = "point";
    String search;


    //ImageButton add_Room;

    Button show;
    ImageButton usr;
    EditText find;
    TextView infoPlace;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mapCol = db.collection("places");


    private GoogleMap mMap;
    private ActivityMapRedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapRedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        red = findViewById(R.id.red_action);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        usr = findViewById(R.id.userButton);

        String email = getIntent().getExtras().getString("email");

        usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.equals("anon")){
                    Snackbar.make(red, "Вы вошли анонимно!", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MapActivityRed.this, UsrActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        });

    }

    private void showAddRoom() {
        AlertDialog.Builder ch_wind = new AlertDialog.Builder(this);
        ch_wind.setTitle("Добавление данных в личную базу");
        ch_wind.setMessage("Введите данные");

        LayoutInflater inflater = LayoutInflater.from(this);
        View Ch_Window = inflater.inflate(R.layout.activity_add,null);

        ch_wind.setView(Ch_Window);

        final EditText AddPlace = Ch_Window.findViewById(R.id.place_for_add);
        final EditText AddMetan = Ch_Window.findViewById(R.id.metan_for_add);
        final EditText AddSerd = Ch_Window.findViewById(R.id.serd_for_add);
        final EditText AddAzd = Ch_Window.findViewById(R.id.azd_for_add);
        final EditText AddLat = Ch_Window.findViewById(R.id.lat_for_add);
        final EditText AddLng = Ch_Window.findViewById(R.id.lng_for_add);

        ch_wind.setNegativeButton("Вернуться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        ch_wind.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //возможные ошибки (переделать в свитч)
                if (TextUtils.isEmpty(AddPlace.getText().toString())) {
                    Snackbar.make(red, "Введите место", Snackbar.LENGTH_SHORT).show();
                    showAddRoom();
                    return;
                }
                double met = Double.parseDouble(AddMetan.getText().toString());
                if (met < 0 ) {
                    Snackbar.make(red, "Значения метана не верны", Snackbar.LENGTH_SHORT).show();
                    showAddRoom();
                    return;
                }
                if (TextUtils.isEmpty(AddMetan.getText().toString())) {
                    Snackbar.make(red, "Введите значения метана", Snackbar.LENGTH_SHORT).show();
                    showAddRoom();
                    return;
                }
                double serd = Double.parseDouble(AddSerd.getText().toString());
                if (serd <0 ) {
                    Snackbar.make(red, "Значения серы не верны", Snackbar.LENGTH_SHORT).show();
                    showAddRoom();
                    return;
                }
                if (TextUtils.isEmpty(AddSerd.getText().toString())) {
                    Snackbar.make(red, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showAddRoom();
                    return;
                }

                if (TextUtils.isEmpty(AddAzd.getText().toString())) {
                    Snackbar.make(red, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showAddRoom();
                    return;
                }
                if (TextUtils.isEmpty(AddLat.getText().toString())) {
                    Snackbar.make(red, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showAddRoom();
                    return;
                }
                if (TextUtils.isEmpty(AddLng.getText().toString())) {
                    Snackbar.make(red, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showAddRoom();
                    return;
                }
                double azd = Double.parseDouble(AddAzd.getText().toString());
                if (azd < 0) {
                    Snackbar.make(red, "значения азота неверны", Snackbar.LENGTH_SHORT).show();
                    showAddRoom();
                    return;
                }
////////////////////////
            }
        });
        ch_wind.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        infoPlace = findViewById(R.id.infoPlaces);
        show = findViewById(R.id.showPoint);
        find = findViewById(R.id.finder);
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
                @SuppressLint("SetTextI18n")
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