package it.mirea.ecoctrl.views.fragments;
/*
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.databinding.ActivityPlistBinding;
import it.mirea.ecoctrl.di.AppExecutors;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.models.PlaceF;
import it.mirea.ecoctrl.repositories.room.MapRoomDatabase;
import it.mirea.ecoctrl.viewModels.PlistViewModel;
import it.mirea.ecoctrl.views.activities.MapActivity;
import it.mirea.ecoctrl.views.activities.PlistActivity;
import it.mirea.ecoctrl.views.adapters.PlaceListAdapter;

public class PlistFragment {
    private PlistViewModel mViewModel;
    private ActivityPlistBinding mBinding;
    PlistViewModel plistViewModel;
    RepoTasks repository;
    private PlaceListAdapter listAdapter;
    private MapRoomDatabase mapRoomDatabase;
    Button back_map;
    RecyclerView all_plcs;
    String email;
    String lvl;
    List<PlaceF> placess;

    public static PlistFragment newInstance() {
        return new PlistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = ActivityPlistBinding.inflate(getLayoutInflater(), container, false);

        mBinding.partyListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

       // ((MapActivity) getActivity()).mBinding.fab.setImageResource(R.drawable.add);
       // ((MapActivity) getActivity()).mBinding.fab.setOnClickListener((View v) -> {
       //     Navigation.findNavController(((MainActivity) getActivity()).mBinding.navHostFragment).navigate(R.id.action_partyList_to_addParty);
       // });

        return mBinding.getRoot();
    }
    private void initPlistViewModel() {
        plistViewModel = new ViewModelProvider(this)
                .get(PlistViewModel.class);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initPlistViewModel();
        //ServiceLocator.getInstance().initBase(getApplication());
       // setContentView(R.layout.activity_plist);
       // email = getIntent().getStringExtra("email").toString();
       // lvl = getIntent().getStringExtra("lvl").toString();

        back_map=findViewById(R.id.back_to_map);
        all_plcs=findViewById(R.id.allPlaces);
        all_plcs.setLayoutManager(new LinearLayoutManager(this));
        back_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlistActivity.this, MapActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("lvl", lvl);
                startActivity(intent);
            }
        });

        listAdapter= new PlaceListAdapter(placess,this,null);
        all_plcs.setAdapter(listAdapter);
        mapRoomDatabase = MapRoomDatabase.getInstance(getApplicationContext());
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
                        List<PlaceF> tasks = listAdapter.getData();
                        plistViewModel.deletePlace(position,tasks);

                    }
                });
            }
        }).attachToRecyclerView(all_plcs);
        //retrievePlaces();
    }

        mViewModel.getAllPlaces();
        mViewModel.AllLiveData.observe(this, new Observer<List<PlaceF>>() {
            @Override
            public void onChanged(List<PlaceF> places) {
                listAdapter.setData(places);
            }
        });

        mViewModel = new ViewModelProvider(this).get(PlistViewModel.class);

        mViewModel.getAllPlaces().observe(getViewLifecycleOwner(), (List<Place> partyList) -> {
            mBinding.partyListRecycler.setAdapter(new PartyListAdapter(partyList, ((MainActivity) requireActivity())));
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
        mViewModel = null;
    }
}
*/