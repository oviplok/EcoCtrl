package it.mirea.ecoctrl.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
////import it.mirea.ecoctrl.databinding.ActivityMapGreenBinding;
import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.databinding.ActivityMapBinding;
import it.mirea.ecoctrl.models.Place;
import it.mirea.ecoctrl.viewModels.MapViewModel;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private MapViewModel mapViewModel;
    RelativeLayout green;

    Button show;
    ImageButton change;
    ImageButton add;
    ImageButton usr;
    EditText find;
    TextView infoPlace;

    String email;
    String lvl;

    boolean connected = false;

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapViewModel();
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        green = findViewById(R.id.map_action);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        usr = findViewById(R.id.userButton);
        infoPlace = findViewById(R.id.infoPlaces);
        show = findViewById(R.id.showPoint);
        change = findViewById(R.id.changeInfo);
        add = findViewById(R.id.addInfo);
        find = findViewById(R.id.finder);
        email = getIntent().getStringExtra("email").toString();
        lvl = getIntent().getStringExtra("lvl").toString();

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else{
            connected = false;
            Snackbar.make(green, R.string.no_internet, Snackbar.LENGTH_SHORT).show();
        }

        if (email.equals("anon") ){
            usr.setVisibility(View.GONE);
        }

        usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MapActivity.this, UsrActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
            }
        });

    }

    private void initMapViewModel() {
        mapViewModel = new ViewModelProvider(this)
                .get(MapViewModel.class);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        infoPlace = findViewById(R.id.infoPlaces);
        show = findViewById(R.id.showPoint);
        change = findViewById(R.id.changeInfo);
        add = findViewById(R.id.addInfo);
        find = findViewById(R.id.finder);

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
            infoPlace.setText(R.string.set_name);
        } else {
            //local DB
            if(!connected || email.equals("anon") || lvl.equals("red")){

            }
            else {
                mapViewModel.showPoint(find.getText().toString());
                mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(Place place) {
                        if (place.isMapResult()) {
                            infoPlace.setTextColor(Color.parseColor("#FF000000"));
                            infoPlace.setTextSize(20);
                            infoPlace.setText(getString(R.string.metan) + place.getMetanInfo() +
                                    getString(R.string.serd) + place.getSerdInfo() + getString(R.string.azd) + place.getAzdInfo());
                            float zoomLevel = 16.0f;
                            mMap.addMarker(new MarkerOptions().position(place.getPointSee()).title(place.getPlace_name()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getPointSee(), zoomLevel));
                            place.setMapResult(false);
                        } else {
                            infoPlace.setTextColor(Color.parseColor("#CC0000"));
                            infoPlace.setTextSize(30);
                            infoPlace.setText(R.string.no_inf);
                        }
                    }
                });

            }

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
                if (met < 0) {
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
                if (serd < 0) {
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
                    Snackbar.make(green, "Значения азота неверны", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }

                if(!connected || email.equals("anon") || lvl.equals("red")){



                }

                else {
                    mapViewModel.changePoint(chPlace.getText().toString(),
                            chMetan.getText().toString(), chSerd.getText().toString(), chAzd.getText().toString());

                    mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                        @Override
                        public void onChanged(Place place) {
                            if (place.isMapResult()) {
                                Snackbar.make(green, "Данные изменены",
                                        Snackbar.LENGTH_LONG).show();
                                place.setMapResult(false);//=false;
                                find.setText(chPlace.getText().toString());
                                showPoint();
                            } else {
                                Snackbar.make(green, "Не удалось изменить",
                                        Snackbar.LENGTH_LONG).show();
                                showChangeInfo();
                            }
                        }
                    });
                }
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
                    showAddInfo();
                    return;
                }
                double met = Double.parseDouble(AddMetan.getText().toString());
                if (met < 0 ) {
                    Snackbar.make(green, "Значения метана не верны", Snackbar.LENGTH_SHORT).show();
                    showAddInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddMetan.getText().toString())) {
                    Snackbar.make(green, "Введите значения метана", Snackbar.LENGTH_SHORT).show();
                    showAddInfo();
                    return;
                }
                double serd = Double.parseDouble(AddSerd.getText().toString());
                if (serd <0 ) {
                    Snackbar.make(green, "Значения серы не верны", Snackbar.LENGTH_SHORT).show();
                    showAddInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddSerd.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showAddInfo();
                    return;
                }

                if (TextUtils.isEmpty(AddAzd.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showAddInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddLat.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showAddInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddLng.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                    showAddInfo();
                    return;
                }
                double azd = Double.parseDouble(AddAzd.getText().toString());
                if (azd < 0) {
                    Snackbar.make(green, "Значения азота неверны", Snackbar.LENGTH_SHORT).show();
                    showChangeInfo();
                    return;
                }

                if(!connected || email.equals("anon") || lvl.equals("red")){

                }
                else {
                    mapViewModel.AddPoint(AddPlace.getText().toString(),
                            AddMetan.getText().toString(), AddSerd.getText().toString(), AddAzd.getText().toString(), AddLng.getText().toString(), AddLng.getText().toString());

                    mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {

                        @Override
                        public void onChanged(Place place) {
                            if (place.isMapResult()) {
                                Snackbar.make(green, "Данные добавлены",
                                        Snackbar.LENGTH_LONG).show();
                                place.setMapResult(false);//=false;
                                find.setText(AddPlace.getText().toString());
                                showPoint();
                            } else {
                                Snackbar.make(green, "Не удалось изменить",
                                        Snackbar.LENGTH_LONG).show();
                                showChangeInfo();
                            }
                        }
                    });
                }
            }
        });
        ch_wind.show();
    }

}