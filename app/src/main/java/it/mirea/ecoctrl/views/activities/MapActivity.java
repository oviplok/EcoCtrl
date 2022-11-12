package it.mirea.ecoctrl.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
//import it.mirea.ecoctrl.databinding.ActivityMapGreenBinding;

import java.util.ArrayList;
import java.util.List;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.databinding.ActivityAddBinding;
import it.mirea.ecoctrl.databinding.ActivityMapBinding;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.repositories.models.GeoResponse;
import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.models.PlaceF;
import it.mirea.ecoctrl.cutContent.IPtoLocation;
import it.mirea.ecoctrl.repositories.network.adressLogic.GeoApiService;
import it.mirea.ecoctrl.repositories.room.MapRoomDatabase;
import it.mirea.ecoctrl.viewModels.MapViewModel;
import it.mirea.ecoctrl.views.adapters.MapImageSliderAdapter;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private MapViewModel mapViewModel;
    private MapRoomDatabase mapRoomDatabase;
    CoordinatorLayout green;


    Button show;
    List<String> images = new ArrayList<>();
    ImageButton list;
    ImageButton change;
    ImageButton add;
   // ImageButton usr;
    ImageButton share;
    EditText find;
    TextView infoPlace;


    String LNG;
    String PLACE;
    String LAT;
    String fev;


    String income_place;
    Uri income;
    String email;
    String lvl;
    String place;
    String ip;
    boolean connected = false;
    boolean searchRes;
    boolean searchdone;

    private GoogleMap mMap;
    private ActivityMapBinding map_binding;
    private ActivityAddBinding add_binding;

    private static final String SHARED_PREF = "sharedPrefs";
    private static final String IP = "IP";
   // private static final String LVL = "lvl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMapViewModel();

       //String LEVEL=sharedPreferences.getString(LVL,"");

        ServiceLocator.getInstance().initBase(getApplication());

        map_binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(map_binding.getRoot());
        green = findViewById(R.id.map_action);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        list = findViewById(R.id.listButton);
        share =findViewById(R.id.shareButton);

        infoPlace = findViewById(R.id.infoPlaces);
        show = findViewById(R.id.showPoint);
        change = findViewById(R.id.changeInfo);
        add = findViewById(R.id.addInfo);
        find = findViewById(R.id.finder);
        email = getIntent().getStringExtra("email").toString();
        lvl = getIntent().getStringExtra("lvl").toString();
        income_place = getIntent().getStringExtra("income_place").toString();


        mapRoomDatabase= MapRoomDatabase.getInstance(getApplicationContext());
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            Context context = getApplicationContext().getApplicationContext();
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
           /* SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(IP, ip);
            editor.apply();*/
           // IPtoLocation IPtoLocation = new IPtoLocation();
           // IPtoLocation.setIp(ip);
            //GeoApiService geoApiService = new GeoApiService();
            //geoApiService.setIp(ip);
            Log.i("IP","IP EXIST");
            //Snackbar.make(green, IPtoLocation.getIp(), Snackbar.LENGTH_SHORT).show();
        }
        else{
            connected = false;
            Snackbar.make(green, R.string.no_internet, Snackbar.LENGTH_SHORT).show();
        }

      /* // if (email.equals("anon") || email.isEmpty()){
        //    usr.setVisibility(View.GONE);
       // }

        usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MapActivity.this, UsrActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
            }
        });*/
        
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.e("SHARE",mapViewModel.getPlace().getPlace_name());
                sharePlace();
            }
        });
    }

    private void sharePlace() {
        if (mapViewModel.placeLiveData != null) {
        mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(@Nullable Place place) {
                if (place != null) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://rf.ecoctrl_app/" + place.getPlace_name());
                        sendIntent.setType("text/plain");
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                }

            }
        });
        }
        else {
                Snackbar.make(green, "Поиск пуст", Snackbar.LENGTH_SHORT).show();
            }
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
        //Log.e("Map_app",ServiceLocator.getInstance().getGson().toJson(income));
        if (!income_place.equals("")) {
            ServiceLocator.getInstance().getRepository().findPlace(income_place, this).observe(this, (Place place) -> {
                if (place != null) {
                    find.setText(place.getPlace_name());
                    showPoint();
                }
            });
        }
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
                Intent intent = new Intent(MapActivity.this, PlistActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("lvl", lvl);
                startActivity(intent);
            }
        });
    }

    //Работа поиска
    private void showPoint() {
        ViewPager2 searchImageSlider;
        searchImageSlider =findViewById(R.id.searchImageSlider);
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
                            mMap.addMarker(new MarkerOptions().position(getPointSee).title(place.getPlace_name()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getPointSee, zoomLevel));
                            if(place.getImages()!=null){
                                List<String> image = new ArrayList<String>();
                                image.add(place.getImages());
                                searchImageSlider.setAdapter(new MapImageSliderAdapter(image, false,MapActivity.this ));
                                searchImageSlider.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                            }
                            searchRes = true;
                            mapViewModel.setPlace(place);
                            Log.e("SHARE",mapViewModel.getPlace().getPlace_name());
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
                            mMap.addMarker(new MarkerOptions().position(getPointSee).title(place.getPlace_name()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getPointSee, zoomLevel));
                            if(place.getImages()!=null){
                                searchImageSlider.setVisibility(View.VISIBLE);
                                searchImageSlider.setAdapter(new MapImageSliderAdapter(place.getImagesF(), false,MapActivity.this ));
                                searchImageSlider.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                            }
                            else{
                                searchImageSlider.setVisibility(View.GONE);
                            }
                            mapViewModel.setPlace(place);
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
                                infoPlace.setText(getString(R.string.metan) + place.getMetanInfo() +
                                        getString(R.string.serd) + place.getSerdInfo() + getString(R.string.azd) + place.getAzdInfo());
                                float zoomLevel = 16.0f;
                                LatLng getPointSee = new LatLng(place.getLat(), place.getLng());
                                mMap.addMarker(new MarkerOptions().position(getPointSee).title(place.getPlace_name()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getPointSee, zoomLevel));
                                place.setMapResult(false);
                                searchRes = true;
                                Place place1 = Place.convertFromFire(place);
                                mapViewModel.setPlace(place1);
                            } else {
                                infoPlace.setTextColor(Color.parseColor("#CC0000"));
                                infoPlace.setTextSize(30);
                                infoPlace.setText(R.string.no_inf);
                                searchRes = false;
                            }
                        }
                    });
                }
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
        final ImageButton chImage_button =Ch_Window.findViewById(R.id.chImageArrow);
        final ViewPager2 chImageSlider = Ch_Window.findViewById(R.id.chImageSlider);
        if(find.getText().toString().isEmpty() || find.getText().toString().equals("")){
            ch_wind.show();
        }
        else if(connected){
            Log.i("IP","IP NOT NULL");
            mapViewModel.getAddressFromIp(ip).observe(MapActivity.this, new Observer<GeoResponse>() {
                @Override
                public void onChanged(GeoResponse geoResponse) {
                    Log.i("IP","ON CHANGED");
                    Log.e("City",geoResponse.getCity());
                    PLACE = geoResponse.getCity();
                    chPlace.setText(PLACE);
                    ch_wind.show();
                }
            });
            Log.i("IP","TO TEXT");
        }
        else{
            chPlace.setText(find.getText());
        }

        chImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chImageSlider.getVisibility() == View.GONE) {
                    chImageSlider.setVisibility(View.VISIBLE);
                    chImage_button.setImageResource(R.drawable.arrow_up);
                } else {
                    chImageSlider.setVisibility(View.GONE);
                    chImage_button.setImageResource(R.drawable.arrow_down);
                }
            }
        });
        chImageSlider.setAdapter(new MapImageSliderAdapter(images, true,MapActivity.this ));
        chImageSlider.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

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
                            List<String> image = new ArrayList<String>();
                            if(images==null){
                                image.add(place.getImages());
                            }
                            else{
                                image=images;
                            }
                            mapViewModel.changePlace(chPlace.getText().toString(),
                                    chMetan.getText().toString(), chSerd.getText().toString(),
                                    chAzd.getText().toString(),"off",place.getLng(),place.getLat(),image);
                        }
                    });

                }
                else {

                    mapViewModel.showPlace(chPlace.getText().toString(),"on",MapActivity.this);
                    mapViewModel.placeLiveData.observe(MapActivity.this, new Observer<Place>() {
                        @Override
                        public void onChanged(Place place) {
                            List<String> image = new ArrayList<String>();
                            if(images==null){
                                image.add(place.getImages());
                            }
                            else{
                                image=images;
                            }
                           // List<String> image = new ArrayList<String>();
                           // image.add(place.getImages());

                            mapViewModel.changePlace(chPlace.getText().toString(),
                                    chMetan.getText().toString(), chSerd.getText().toString(),
                                    chAzd.getText().toString(),"on",place.getLng(),place.getLat(),image);
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
    }
    private void showAddInfo() {
        AlertDialog.Builder add_wind = new AlertDialog.Builder(this);
        add_wind.setTitle("Добавление данных");
        add_wind.setMessage("Введите данные");

        LayoutInflater inflater = LayoutInflater.from(this);
        View add_Window = inflater.inflate(R.layout.activity_add,null);

        add_wind.setView(add_Window);

        final EditText AddPlace = add_Window.findViewById(R.id.place_for_add);
        final EditText AddMetan = add_Window.findViewById(R.id.metan_for_add);
        final EditText AddSerd = add_Window.findViewById(R.id.serd_for_add);
        final EditText AddAzd = add_Window.findViewById(R.id.azd_for_add);
        final EditText AddLat = add_Window.findViewById(R.id.lat_for_add);
        final EditText AddLng = add_Window.findViewById(R.id.lng_for_add);
        final ViewPager2 addImageSlider = add_Window.findViewById(R.id.addImageSlider);
        final ImageButton addImmage_button = add_Window.findViewById(R.id.addImageArrow);
        ////////////////////////////////////////////////////////////////////////////

        if(connected){
            Log.i("IP","IP NOT NULL");
            mapViewModel.getAddressFromIp(ip).observe(MapActivity.this, new Observer<GeoResponse>() {
                @Override
                public void onChanged(GeoResponse geoResponse) {
                    Log.i("IP","ON CHANGED");
                    Log.e("City",geoResponse.getCity());
                    PLACE = geoResponse.getCity();
                    LAT = ""+geoResponse.getLatitude();
                    LNG = ""+geoResponse.getLongitude();
                    Log.e("Lat",LAT);
                    Log.e("Lat",LNG);
                    //SetIPplace(PLACE,LAT,LNG);
                    AddLat.setText(LAT);
                    AddLng.setText(LNG);
                    AddPlace.setText(PLACE);
                    add_wind.show();
                }
            });
            Log.i("IP","TO TEXT");
        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        addImmage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addImageSlider.getVisibility() == View.GONE) {
                    addImageSlider.setVisibility(View.VISIBLE);
                    addImmage_button.setImageResource(R.drawable.arrow_up);
                } else {
                    addImageSlider.setVisibility(View.GONE);
                    addImmage_button.setImageResource(R.drawable.arrow_down);
                }
            }
        });
        addImageSlider.setAdapter(new MapImageSliderAdapter(images, true,MapActivity.this ));
        addImageSlider.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        add_wind.setNegativeButton("Вернуться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        add_wind.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
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
                            AddLng.getText().toString(), AddLat.getText().toString(),images,false,"off");
                }
                else {
                    Log.e("MapAct","connected");
                    mapViewModel.AddPlace(AddPlace.getText().toString(),
                            AddMetan.getText().toString(), AddSerd.getText().toString(), AddAzd.getText().toString(),
                            AddLng.getText().toString(), AddLat.getText().toString(),images,false,"on");
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

    }

}