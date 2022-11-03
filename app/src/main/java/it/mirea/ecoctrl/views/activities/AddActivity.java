/*package it.mirea.ecoctrl.views.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.databinding.ActivityAddBinding;
import it.mirea.ecoctrl.repositories.models.PlaceF;
import it.mirea.ecoctrl.viewModels.MapViewModel;
import it.mirea.ecoctrl.views.adapters.AddImageSliderAdapter;

public class AddActivity extends AppCompatActivity {
    private ActivityAddBinding binding;
    private MapViewModel mapViewModel;
    CoordinatorLayout green;
    Button exitButton;
    Button add;

    List<String> images = new ArrayList<>();
   // ViewPager2 addImageSlider;
    String income_place;
    String email;
    String lvl;
    boolean connected;

    EditText AddPlace;
    EditText AddMetan;
    EditText AddSerd;
    EditText AddAzd;
    EditText AddLat;
    EditText AddLng;
    ViewPager2 addImageSlider;
    ImageButton addImmage_button;
    RelativeLayout placeHolder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMapViewModel();

        green = findViewById(R.id.add_action);
        email = getIntent().getStringExtra("email").toString();
        lvl = getIntent().getStringExtra("lvl").toString();

        AddPlace = findViewById(R.id.place_for_add);
        AddMetan = findViewById(R.id.metan_for_add);
        AddSerd = findViewById(R.id.serd_for_add);
        AddAzd = findViewById(R.id.azd_for_add);
        AddLat = findViewById(R.id.lat_for_add);
        AddLng = findViewById(R.id.lng_for_add);
        addImageSlider = findViewById(R.id.addImageSlider);
        addImmage_button = findViewById(R.id.imageArrow);
        placeHolder = findViewById(R.id.placeHolder);

        exitButton =findViewById(R.id.exit);
        add=findViewById(R.id.addPoint);

        addImmage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addImageSlider.getVisibility() == View.GONE) {
                    addImageSlider.setVisibility(View.VISIBLE);
                    addImmage_button.setImageResource(R.drawable.arrow_up);
                   // placeHolder.setVisibility(View.VISIBLE);
                    //binding.
                } else {
                  //  placeHolder.setVisibility(View.GONE);
                    addImageSlider.setVisibility(View.GONE);
                    addImmage_button.setImageResource(R.drawable.arrow_down);
                }

            }
        });
        //addImageSlider.setAdapter(new AddImageSliderAdapter(images, true,AddActivity.this ));
        //addImageSlider.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, MapActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("lvl", lvl);
                if(income_place!=null){
                    intent.putExtra("income_place",income_place);
                }else{
                    intent.putExtra("income_place","");
                }
                startActivity(intent);
            }
        });

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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddInfo();
            }
        });

    }

    private void initMapViewModel() {
        mapViewModel = new ViewModelProvider(this)
                .get(MapViewModel.class);

    }

    private void showAddInfo() {
       // AlertDialog.Builder ch_wind = new AlertDialog.Builder(this);
       // ch_wind.setTitle("Добавление данных");
       // ch_wind.setMessage("Введите данные");

        //LayoutInflater inflater = LayoutInflater.from(this);
        //View Ch_Window = inflater.inflate(R.layout.activity_add,null);

        //ch_wind.setView(Ch_Window);
/*
         EditText AddPlace = findViewById(R.id.place_for_add);
         EditText AddMetan = findViewById(R.id.metan_for_add);
         EditText AddSerd = findViewById(R.id.serd_for_add);
         EditText AddAzd = findViewById(R.id.azd_for_add);
         EditText AddLat = findViewById(R.id.lat_for_add);
         EditText AddLng = findViewById(R.id.lng_for_add);
         ViewPager2 addImageSlider = findViewById(R.id.addImageSlider);
         ImageButton addImmage_button = findViewById(R.id.imageArrow);
         RelativeLayout placeHolder = findViewById(R.id.placeHolder);

////////////////////////////////////////////////////////////////////////////////////////

//

//////////////////////////////////////////////////////////////////////////////////////////


        //exit.setNegativeButton("Вернуться", new DialogInterface.OnClickListener() {
         //   @Override
          //  public void onClick(DialogInterface dialogInterface, int which) {
               // dialogInterface.dismiss();
           // }
        //});
        //Button back;
        //back=findViewById(R.id.back_button);
        //  back.setOnClickListener(new DialogInterface() {
        //      @Override
        //      public void onClick(DialogInterface dialogInterface, int which) {
        //          dialogInterface.dismiss();
        //     }
        // });


                //возможные ошибки (переделать в свитч)
                if (TextUtils.isEmpty(AddPlace.getText().toString())) {
                    Snackbar.make(green, "Введите место", Snackbar.LENGTH_SHORT).show();
                   // showAddInfo();
                    return;
                }
                double met = Double.parseDouble(AddMetan.getText().toString());
                if (met < 0 ) {
                    Snackbar.make(green, "Значения метана не верны", Snackbar.LENGTH_SHORT).show();
                   // showAddInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddMetan.getText().toString())) {
                    Snackbar.make(green, "Введите значения метана", Snackbar.LENGTH_SHORT).show();
                   // showAddInfo();
                    return;
                }
                double serd = Double.parseDouble(AddSerd.getText().toString());
                if (serd <0 ) {
                    Snackbar.make(green, "Значения серы не верны", Snackbar.LENGTH_SHORT).show();
                   // showAddInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddSerd.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                   // showAddInfo();
                    return;
                }

                if (TextUtils.isEmpty(AddAzd.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                   // showAddInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddLat.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                   // showAddInfo();
                    return;
                }
                if (TextUtils.isEmpty(AddLng.getText().toString())) {
                    Snackbar.make(green, "Значения отсутствуют", Snackbar.LENGTH_SHORT).show();
                   // showAddInfo();
                    return;
                }
                double azd = Double.parseDouble(AddAzd.getText().toString());
                if (azd < 0) {
                    Snackbar.make(green, "Значения азота неверны", Snackbar.LENGTH_SHORT).show();
                   // showChangeInfo();
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
                    mapViewModel.placeFireLiveData.observe(AddActivity.this, new Observer<PlaceF>() {
                        @Override
                        public void onChanged(PlaceF place) {
                            if (place.isMapResult()) {
                                Snackbar.make(green, "Данные добавлены",
                                        Snackbar.LENGTH_LONG).show();
                                place.setMapResult(false);
                               // find.setText(AddPlace.getText().toString());
                                Log.e("AddPart","added");
                                income_place= AddPlace.getText().toString();
                               // showPoint();
                            } else {
                                Snackbar.make(green, "Не удалось добавить",
                                        Snackbar.LENGTH_LONG).show();
                                Log.e("AddPart","not added");
                            }
                        }
                    });
                }
            }
       // });
       // ch_wind.show();
    //}
}
        */