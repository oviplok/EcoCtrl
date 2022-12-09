package it.mirea.ecoctrl.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.di.AppExecutors;
import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.domain.models.Place;
import it.mirea.ecoctrl.repositories.room.MapRoomDatabase;
import it.mirea.ecoctrl.viewModels.PlistViewModel;
import it.mirea.ecoctrl.views.adapters.PlaceListAdapter;

public class PlistActivity extends AppCompatActivity {
    PlistViewModel plistViewModel;
   // RepoTasks repository;
    private PlaceListAdapter listAdapter;
   // private MapRoomDatabase mapRoomDatabase;
    Button back_map;
    RecyclerView all_plcs;
    String email;
    String lvl;
    List<Place> placess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPlistViewModel();
        ServiceLocator.getInstance().initBase(getApplication());
        setContentView(R.layout.activity_plist);
        email = getIntent().getStringExtra("email").toString();
        lvl = getIntent().getStringExtra("lvl").toString();

        back_map=findViewById(R.id.back_to_map);
        all_plcs=findViewById(R.id.allPlaces);
        all_plcs.setLayoutManager(new LinearLayoutManager(this));
        back_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlistActivity.this, MapActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("lvl", lvl);
                intent.putExtra("income_place","");
                startActivity(intent);
            }
        });

        listAdapter= new PlaceListAdapter(placess,this);
        all_plcs.setAdapter(listAdapter);
        //mapRoomDatabase = MapRoomDatabase.getInstance(getApplicationContext());
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Place> tasks = listAdapter.getData();
                        plistViewModel.deletePlace(position,tasks);

                    }
                });
            }
        }).attachToRecyclerView(all_plcs);
        retrievePlaces();
    }

    private void initPlistViewModel() {
        plistViewModel = new ViewModelProvider(this)
                .get(PlistViewModel.class);
    }

    private void retrievePlaces() {
        plistViewModel.getAllPlaces();
        plistViewModel.AllLiveData.observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                listAdapter.setData(places);
               // Log.d("onBindView",placess);
            }
        });

    }
}