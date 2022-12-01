package it.mirea.ecoctrl.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.databinding.AdminFragmentBinding;
import it.mirea.ecoctrl.di.AppExecutors;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.room.MapRoomDatabase;
import it.mirea.ecoctrl.viewModels.AdminViewModel;
import it.mirea.ecoctrl.views.activities.MainActivity;
import it.mirea.ecoctrl.views.activities.UsrActivity;
import it.mirea.ecoctrl.views.adapters.UserListAdapter;

public class AdminFragment extends Fragment {

    private static final String PASSWORD = "password";
    private static final String SHARED_PREF = "sharedPrefs";
    private static final String EMAIL = "email";
    private static final String LVL = "lvl";

    private MapRoomDatabase mapRoomDatabase;
    private UserListAdapter listAdapter;
    RecyclerView all_usrs;
    List<User> users;

    Context context;
    int back_tap;

    private AdminViewModel adminViewModel;
    private AdminFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapRoomDatabase = MapRoomDatabase.getInstance(getActivity().getApplicationContext());
        initAdminViewModel();
        context= new UsrActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= AdminFragmentBinding.inflate(inflater, container, false);

        binding.backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (back_tap==1){
                    Back_but();
                    back_tap=0;
                }
                else
                {
                    back_tap++;
                    Snackbar.make(getActivity().findViewById(R.id.content), "If you want log out from your account, tap again",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });

        listAdapter= new UserListAdapter(users,((UsrActivity) requireActivity()));
        all_usrs.setAdapter(listAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

//            public void onClick(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        int position = viewHolder.getAdapterPosition();
//                        RecyclerView recyclerView = new RecyclerView();
//                        recyclerView.findViewHolderForAdapterPosition(position).itemView.performClick();
//
//                    }
//                });
//            }

            @Override
            //MAKE SWIPE CHECKER
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<User> tasks = listAdapter.getData();
                        adminViewModel.deleteUser(position,tasks);

                    }
                });
            }
        }).attachToRecyclerView(all_usrs);
        retrievePlaces();
        return binding.getRoot();
    }

    private void initPlistViewModel() {
        adminViewModel = new ViewModelProvider(this)
                .get(AdminViewModel.class);
    }

    private void retrievePlaces() {
        adminViewModel.getAllUsers();
        adminViewModel.adminLiveData.observe(getViewLifecycleOwner(), users -> listAdapter.setData(users));

    }

        //return binding.getRoot();
    //}



    private void initAdminViewModel() {
        adminViewModel = new ViewModelProvider(this)
                .get(AdminViewModel.class);
    }

    private void Back_but(){
        Intent intent = new Intent((UsrActivity) requireActivity(), MainActivity.class);
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(EMAIL, "");
        editor.putString(PASSWORD, "");
        editor.putString(LVL, "");
        editor.apply();
        startActivity(intent);
    }
}
