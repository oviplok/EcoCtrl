package it.mirea.ecoctrl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
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
////
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
///
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
///
import java.util.HashMap;
import java.util.Map;

import it.mirea.ecoctrl.databinding.ActivityMapGreenBinding;


public class MapActivityGreen extends FragmentActivity implements OnMapReadyCallback {
    RelativeLayout green;

    String place = "place";
    String metanInfo = "met";
    String azdInfo = "azd";
    String serdInfo = "serd";
    String point = "point";
    String search;

    Button show;
    ImageButton change;
    ImageButton add;
    EditText find;
    TextView infoPlace;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference mapCol = db.collection("places");
    //DocumentReference mapDoc = db.collection("places").document("РТУ МИРЭА");


    private GoogleMap mMap;
    private ActivityMapGreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapGreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        green = findViewById(R.id.green_action);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        infoPlace = findViewById(R.id.infoPlaces);
        show = findViewById(R.id.showPoint);
        change = findViewById(R.id.changeInfo);
        add = findViewById(R.id.addInfo);
        find = findViewById(R.id.finder);

        // Начальное положение карты, не работает (удалено)
        /*mapDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                } else {
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
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeInfo();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddInfo();
            }
        });

    }

    //Работа поиска
    private void showPoint() {
        if (TextUtils.isEmpty(find.getText().toString())) {
            infoPlace.setTextColor(Color.parseColor("#CC0000"));
            infoPlace.setTextSize(30);
            infoPlace.setText("Введите название в поиск!");
        } else {
            search = find.getText().toString();
            mapCol.document(search).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
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
                    } else {
                        infoPlace.setTextColor(Color.parseColor("#CC0000"));
                        infoPlace.setTextSize(30);
                        infoPlace.setText("Ничего не найдено...");
                    }
                }
            });
        }

    }

    private void showChangeInfo() {
        AlertDialog.Builder ch_wind = new AlertDialog.Builder(this);
        ch_wind.setTitle("Перепись данных");
        ch_wind.setMessage("Введите данные");

        LayoutInflater inflater = LayoutInflater.from(this);
        View Ch_Window = inflater.inflate(R.layout.activity_change,null);

        ch_wind.setView(Ch_Window);

        final EditText chPlace = Ch_Window.findViewById(R.id.place_for_change);
        final EditText chMetan = Ch_Window.findViewById(R.id.metan_for_change);
        final EditText chSerd = Ch_Window.findViewById(R.id.serd_for_change);
        final EditText chAzd = Ch_Window.findViewById(R.id.azd_for_change);

        ch_wind.setNegativeButton("Вернуться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        ch_wind.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //возможные ошибки
                if (TextUtils.isEmpty(chPlace.getText().toString())) {
                    Snackbar.make(green, "Введите место", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                double met = Double.parseDouble(chMetan.getText().toString());
                if (met < 0 ) {
                    Snackbar.make(green, "Значения метана не верны", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                if (TextUtils.isEmpty(chMetan.getText().toString())) {
                    Snackbar.make(green, "Введите значения метана", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                double serd = Double.parseDouble(chSerd.getText().toString());
                if (serd <0 ) {
                    Snackbar.make(green, "Значения серы не верны", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                if (TextUtils.isEmpty(chSerd.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }

                if (TextUtils.isEmpty(chAzd.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                double azd = Double.parseDouble(chAzd.getText().toString());
                if (azd < 0) {
                    Snackbar.make(green, "значения азота неверны", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }

                mapCol.document(chPlace.getText().toString()).get().
                        addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists()){
                                    GeoPoint Geo = documentSnapshot.getGeoPoint(point);
                                    Map<String, Object> InfoChangeMap = new HashMap<>();
                                    InfoChangeMap.put("place", chPlace.getText().toString());
                                    InfoChangeMap.put("met", chMetan.getText().toString());
                                    InfoChangeMap.put("serd", chSerd.getText().toString());
                                    InfoChangeMap.put("azd", chAzd.getText().toString());
                                    InfoChangeMap.put("point", Geo);
                                    mapCol.document(chPlace.getText().toString()).set(InfoChangeMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Snackbar.make(green, "Все прошло успешно!",
                                                            Snackbar.LENGTH_LONG).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Snackbar.make(green, "Не удалось изменить в базе(ошибка №2)",
                                                    Snackbar.LENGTH_LONG).show();
                                            showChangeInfo();
                                            return;
                                        }
                                    });
                                }
                                else {

                                    Snackbar.make(green, "Данные не существуют!",
                                            Snackbar.LENGTH_LONG).show();
                                    showChangeInfo();
                                    return;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(green, "Не удалось изменить в базе(ошибка №1)",
                                Snackbar.LENGTH_LONG).show();
                        showChangeInfo();
                        return;
                    }
                });

            }
        });
        ch_wind.show();
    }
    private void showAddInfo() {
        AlertDialog.Builder ch_wind = new AlertDialog.Builder(this);
        ch_wind.setTitle("Добавление данных");
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
                    Snackbar.make(green, "Введите место", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                double met = Double.parseDouble(AddMetan.getText().toString());
                if (met < 0 ) {
                    Snackbar.make(green, "Значения метана не верны", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddMetan.getText().toString())) {
                    Snackbar.make(green, "Введите значения метана", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                double serd = Double.parseDouble(AddSerd.getText().toString());
                if (serd <0 ) {
                    Snackbar.make(green, "Значения серы не верны", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddSerd.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }

                if (TextUtils.isEmpty(AddAzd.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddLat.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddLng.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }
                double azd = Double.parseDouble(AddAzd.getText().toString());
                if (azd < 0) {
                    Snackbar.make(green, "значения азота неверны", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }

                mapCol.document(AddPlace.getText().toString()).get().
                        addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    Snackbar.make(green, "Данные существуют!",
                                            Snackbar.LENGTH_LONG).show();
                                    showChangeInfo();
                                    return;
                                }
                                else{
                                    double Lng = Double.parseDouble(AddLng.getText().toString());
                                    double Lat = Double.parseDouble(AddLat.getText().toString());
                                    GeoPoint Geo= new GeoPoint(Lat,Lng);
                                    Map<String, Object> InfoChangeMap = new HashMap<>();
                                    InfoChangeMap.put("place", AddPlace.getText().toString());
                                    InfoChangeMap.put("met", AddMetan.getText().toString());
                                    InfoChangeMap.put("serd", AddSerd.getText().toString());
                                    InfoChangeMap.put("azd", AddAzd.getText().toString());
                                    InfoChangeMap.put("point", Geo);
                                    mapCol.document(AddPlace.getText().toString()).set(InfoChangeMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Snackbar.make(green, "Все прошло успешно!",
                                                            Snackbar.LENGTH_LONG).show();


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Snackbar.make(green, "Не удалось изменить в базе(ошибка записи)",
                                                    Snackbar.LENGTH_LONG).show();
                                            showChangeInfo();
                                            return;
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(green, "Не удалось изменить в базе(ошибка в базе)",
                                Snackbar.LENGTH_LONG).show();
                    }
                });

            }
        });
        ch_wind.show();
    }

}