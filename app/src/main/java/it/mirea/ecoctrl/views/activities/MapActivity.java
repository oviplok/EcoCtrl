package it.mirea.ecoctrl.views.activities;

import androidx.annotation.Nullable;
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
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;
//import it.mirea.ecoctrl.databinding.ActivityMapGreenBinding;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.databinding.ActivityMapBinding;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.models.PlaceF;
import it.mirea.ecoctrl.repositories.room.MapRoomDatabase;
import it.mirea.ecoctrl.viewModels.MapViewModel;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private MapViewModel mapViewModel;
    private MapRoomDatabase mapRoomDatabase;
    RelativeLayout green;


    Button show;
    ImageButton list;
    ImageButton change;
    ImageButton add;
    ImageButton usr;
    EditText find;
    TextView infoPlace;

    String email;
    String lvl;

    boolean connected = false;
    boolean searchRes;
    boolean searchdone;

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapViewModel();

        ServiceLocator.getInstance().initBase(getApplication());

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        green = findViewById(R.id.map_action);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        list = findViewById(R.id.listButton);
        usr = findViewById(R.id.userButton);
        infoPlace = findViewById(R.id.infoPlaces);
        show = findViewById(R.id.showPoint);
        change = findViewById(R.id.changeInfo);
        add = findViewById(R.id.addInfo);
        find = findViewById(R.id.finder);
        email = getIntent().getStringExtra("email").toString();
        lvl = getIntent().getStringExtra("lvl").toString();
        mapRoomDatabase= MapRoomDatabase.getInstance(getApplicationContext());
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
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // showAddFav();
                Intent intent = new Intent(MapActivity.this, PlistActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("lvl", lvl);
                startActivity(intent);
            }
        });
    }

    /*private void showAddFav() {
        if (TextUtils.isEmpty(find.getText().toString()) || !searchRes) {
            Snackbar.make(green, "Ошибка добавления в закладки",
                    Snackbar.LENGTH_LONG).show();
        }
        else if(!fav){
            if(connected) {
                Log.e("FavStar","connected");
                mapViewModel.showPlace(find.getText().toString(), "on", this);
                Log.e("FavStar","after ViewModel");
                mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                    @Override
                    public void onChanged(@Nullable Place placefav) {
                        if (placefav != null) {
                            mapViewModel.changePlace(placefav.getPlace_name(),
                                    placefav.getMetanInfo(), placefav.getSerdInfo(),
                                    placefav.getAzdInfo(), "on", true);
                            searchRes = true;
                            Log.e("FavStar","added");
                         //   Snackbar.make(green, "add in fav", Snackbar.LENGTH_LONG).show();
                            list.setImageResource(R.drawable.ic_action_full_star);
                        } else {
                            Log.e("FavStar","FireStart");
                            mapViewModel.placeFireLiveData.observe(MapActivity.this, new Observer<PlaceF>() {
                                @Override
                                public void onChanged(@Nullable PlaceF placefav) {
                                    if (placefav != null) {
                                        String Lng = Double.toString(placefav.getLng());
                                        String Lat = Double.toString(placefav.getLat());
                                        mapViewModel.AddPlace(placefav.getPlace_name(),
                                                placefav.getMetanInfo(), placefav.getSerdInfo(),
                                                placefav.getAzdInfo(), Lng, Lat, true, "on");
                                       // Snackbar.make(green, "add in fav", Snackbar.LENGTH_LONG).show();
                                        list.setImageResource(R.drawable.ic_action_full_star);
                                        searchRes = true;
                                        Log.e("FavStar","FireEnd");
                                    } else {
                                        Snackbar.make(green, "Ошибка добавления в закладки",
                                                Snackbar.LENGTH_LONG).show();
                                        searchRes = false;
                                    }
                                }
                            });

                           // Log.e("FavStar","not added");
                            //Snackbar.make(green, "так",
                              //      Snackbar.LENGTH_LONG).show();
                            //searchRes = false;
                        }
                    }
                });
               // Log.e("FireStar","StartStart");
               // if (!searchRes) {
                Log.e("FavStar","FireStart");
                mapViewModel.placeFireLiveData.observe(MapActivity.this, new Observer<PlaceF>() {
                    @Override
                    public void onChanged(@Nullable PlaceF placeF) {
                        if (placeF != null) {
                            String Lng = Double.toString(placeF.getLng());
                            String Lat = Double.toString(placeF.getLat());
                            mapViewModel.AddPlace(placeF.getPlace_name(),
                                    placeF.getMetanInfo(), placeF.getSerdInfo(),
                                    placeF.getAzdInfo(), Lng, Lat, true, "on");
                            Snackbar.make(green, "add in fav",
                                    Snackbar.LENGTH_LONG).show();
                            star.setImageResource(R.drawable.ic_action_full_star);
                            searchRes = true;
                            Log.e("FavStar","FireEnd");
                        } else {
                            Snackbar.make(green, "Ошибка добавления в закладки",
                                    Snackbar.LENGTH_LONG).show();
                            searchRes = false;
                        }
                    }
                });
               // }
            } else{
                Log.e("FavStar","no connection");
                mapViewModel.showPlace(find.getText().toString(), "off", this);
                Log.e("FavStar","after ViewModel");
                mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                    @Override
                    public void onChanged(@Nullable Place place) {
                        if (place != null) {
                            mapViewModel.changePlace(place.getPlace_name(),
                                    place.getMetanInfo(), place.getSerdInfo(),
                                    place.getAzdInfo(), "on", true);
                            //Snackbar.make(green, "add in fav", Snackbar.LENGTH_LONG).show();
                            list.setImageResource(R.drawable.ic_action_full_star);
                            searchRes = true;
                        } else {
                            searchRes = false;
                           // Snackbar.make(green, "Ошибка добавления в закладки", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
        else {
            if(connected) {
                Log.e("FavStar","connected");
                mapViewModel.showPlace(find.getText().toString(), "on", this);
                Log.e("FavStar","after ViewModel");
                mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                    @Override
                    public void onChanged(@Nullable Place place) {
                        if (place != null) {
                            mapViewModel.changePlace(place.getPlace_name(),
                                    place.getMetanInfo(), place.getSerdInfo(),
                                    place.getAzdInfo(), "on", false);
                            searchRes = true;
                            Log.e("FavStar","added");
                            list.setImageResource(R.drawable.ic_action_empty_star);
                        } else {
                            Log.e("FavStar","FireStart");
                            mapViewModel.placeFireLiveData.observe(MapActivity.this, new Observer<PlaceF>() {
                                @Override
                                public void onChanged(@Nullable PlaceF placeF) {
                                    if (placeF != null) {
                                        String Lng = Double.toString(placeF.getLng());
                                        String Lat = Double.toString(placeF.getLat());
                                        mapViewModel.AddPlace(placeF.getPlace_name(),
                                                placeF.getMetanInfo(), placeF.getSerdInfo(),
                                                placeF.getAzdInfo(), Lng, Lat, false, "on");
                                        list.setImageResource(R.drawable.ic_action_empty_star);
                                        searchRes = true;
                                        Log.e("FavStar","FireEnd");
                                    } else {
                                        Snackbar.make(green, "Ошибка добавления в закладки",
                                                Snackbar.LENGTH_LONG).show();
                                        searchRes = false;
                                    }
                                }
                            });
                        }
                    }
                });
            } else{
                Log.e("FavStar","no connection");
                mapViewModel.showPlace(find.getText().toString(), "off", this);
                Log.e("FavStar","after ViewModel");
                mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                    @Override
                    public void onChanged(@Nullable Place place) {
                        if (place != null) {
                            mapViewModel.changePlace(place.getPlace_name(),
                                    place.getMetanInfo(), place.getSerdInfo(),
                                    place.getAzdInfo(), "on", false);
                            list.setImageResource(R.drawable.ic_action_empty_star);
                            searchRes = true;
                        } else {
                            searchRes = false;
                        }
                    }
                });
            }
        }

    }*/

    //Работа поиска
    private void showPoint() {
        infoPlace.setText("");
        searchRes = false;
        Log.e("SearchPart","searching...");
        if (TextUtils.isEmpty(find.getText().toString())) {
            infoPlace.setTextColor(Color.parseColor("#CC0000"));
            infoPlace.setTextSize(30);
            infoPlace.setText(R.string.set_name);
        } else {
            //local DB
            if(!connected || email.equals("anon") || lvl.equals("red")){
                Log.e("SearchPart"," not connected");

                mapViewModel.showPlace(find.getText().toString(),"off",this);
                mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(@Nullable Place place) {
                        if (place!=null) {
                            infoPlace.setTextColor(Color.parseColor("#FF000000"));
                            infoPlace.setTextSize(20);
                            LatLng getPointSee = new LatLng(place.getLat(),place.getLng());
                            infoPlace.setText(getString(R.string.metan) + place.getMetanInfo() +
                                    getString(R.string.serd) + place.getSerdInfo() + getString(R.string.azd) + place.getAzdInfo());
                            float zoomLevel = 16.0f;

                           /* if(place.isFav()){
                                star.setImageResource(R.drawable.ic_action_full_star);
                            }
                            else{
                                star.setImageResource(R.drawable.ic_action_empty_star);
                            }*/
                            mMap.addMarker(new MarkerOptions().position(getPointSee).title(place.getPlace_name()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getPointSee, zoomLevel));
                            searchRes = true;
                        } else {
                            infoPlace.setTextColor(Color.parseColor("#CC0000"));
                            infoPlace.setTextSize(30);
                            infoPlace.setText(R.string.no_inf);
                            searchRes = false;
                        }
                    }
                });
            }
            else {
                Log.e("SearchPart", "connected");
                mapViewModel.showPlace(find.getText().toString(), "on", this);
                mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(@Nullable Place place) {
                        if (place != null) {
                            infoPlace.setTextColor(Color.parseColor("#FF000000"));
                            infoPlace.setTextSize(20);
                            LatLng getPointSee = new LatLng(place.getLat(), place.getLng());
                            infoPlace.setText(getString(R.string.metan) + place.getMetanInfo() +
                                    getString(R.string.serd) + place.getSerdInfo() + getString(R.string.azd) + place.getAzdInfo());
                            float zoomLevel = 16.0f;
                           // fav=place.isFav();
                          /*  if(place.isFav()){
                                star.setImageResource(R.drawable.ic_action_full_star);
                            }
                            else{
                                star.setImageResource(R.drawable.ic_action_empty_star);
                            }*/
                            //LatLng latLng=new LatLng();

                            mMap.addMarker(new MarkerOptions().position(getPointSee).title(place.getPlace_name()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getPointSee, zoomLevel));
                            searchRes = true;
                            searchdone=true;
                        }
                        else{
                            searchdone=true;
                            searchRes=false;
                        }
                    }
                });
                if(!searchRes && searchdone){
                    mapViewModel.placeFireLiveData.observe(MapActivity.this, new Observer<PlaceF>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onChanged(PlaceF place) {
                            if (place.isMapResult()) {
                                infoPlace.setTextColor(Color.parseColor("#FF000000"));
                                infoPlace.setTextSize(20);
                               // fav=place.isFav();
                               /* if(place.isFav()){
                                    star.setImageResource(R.drawable.ic_action_full_star);
                                }
                                else{
                                    star.setImageResource(R.drawable.ic_action_empty_star);
                                }*/
                                infoPlace.setText(getString(R.string.metan) + place.getMetanInfo() +
                                        getString(R.string.serd) + place.getSerdInfo() + getString(R.string.azd) + place.getAzdInfo());
                                float zoomLevel = 16.0f;
                                mMap.addMarker(new MarkerOptions().position(place.getPointSee()).title(place.getPlace_name()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getPointSee(), zoomLevel));
                                place.setMapResult(false);
                                searchRes = true;
                            } else {
                                infoPlace.setTextColor(Color.parseColor("#CC0000"));
                                infoPlace.setTextSize(30);
                                infoPlace.setText(R.string.no_inf);
                                //fav=false;
                                searchRes = false;
                            }
                        }
                    });
                }
                /*if(fav){
                    list.setImageResource(R.drawable.ic_action_full_star);
                }
                else{
                    list.setImageResource(R.drawable.ic_action_empty_star);
                }*/
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
                    mapViewModel.showPlace(chPlace.getText().toString(),"on",MapActivity.this);
                    mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                        @Override
                        public void onChanged(Place place) {
                            mapViewModel.changePlace(chPlace.getText().toString(),
                                    chMetan.getText().toString(), chSerd.getText().toString(),
                                    chAzd.getText().toString(),"off",place.getLng(),place.getLat());
                        }
                    });

                }
                else {

                    mapViewModel.showPlace(chPlace.getText().toString(),"on",MapActivity.this);
                    mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                        @Override
                        public void onChanged(Place place) {
                            mapViewModel.changePlace(chPlace.getText().toString(),
                                    chMetan.getText().toString(), chSerd.getText().toString(),
                                    chAzd.getText().toString(),"on",place.getLng(),place.getLat());
                        }
                    });
                    mapViewModel.placeFireLiveData.observe(MapActivity.this, new Observer<PlaceF>() {
                        @Override
                        public void onChanged(PlaceF place) {
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

        ch_wind.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
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
                Log.e("MapAct","con_part");
                if(!connected || email.equals("anon") || lvl.equals("red")){
                    Log.e("MapAct","not connected");
                    mapViewModel.AddPlace(AddPlace.getText().toString(),
                            AddMetan.getText().toString(), AddSerd.getText().toString(), AddAzd.getText().toString(),
                            AddLng.getText().toString(), AddLat.getText().toString(),false,"off");
                }
                else {
                    Log.e("MapAct","connected");
                    mapViewModel.AddPlace(AddPlace.getText().toString(),
                            AddMetan.getText().toString(), AddSerd.getText().toString(), AddAzd.getText().toString(),
                            AddLng.getText().toString(), AddLat.getText().toString(),false,"on");
                    mapViewModel.placeFireLiveData.observe(MapActivity.this, new Observer<PlaceF>() {
                        @Override
                        public void onChanged(PlaceF place) {
                            if (place.isMapResult()) {
                                Snackbar.make(green, "Данные добавлены",
                                        Snackbar.LENGTH_LONG).show();
                                place.setMapResult(false);
                                find.setText(AddPlace.getText().toString());
                                Log.e("AddPart","added");
                                showPoint();
                            } else {
                                Snackbar.make(green, "Не удалось добавить",
                                        Snackbar.LENGTH_LONG).show();
                                Log.e("AddPart","not added");
                            }
                        }
                    });
                }
            }
        });
        ch_wind.show();
    }
}