package it.mirea.ecoctrl.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.mirea.ecoctrl.databinding.AdminFragmentBinding;
import it.mirea.ecoctrl.di.AppExecutors;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.models.Place;
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

    private UserListAdapter listAdapter;
    List<User> userss;

    int back_tap;

    private AdminViewModel adminViewModel;
    private AdminFragmentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAdminViewModel();
        ServiceLocator.getInstance().initBase(getActivity().getApplication());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= AdminFragmentBinding.inflate(inflater, container, false);
        binding.allUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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
                    Toast.makeText(getContext(), "Кликните еще раз, если хотите выйти из аккаунта", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listAdapter= new UserListAdapter(userss,((UsrActivity) requireActivity()));
        binding.allUsers.setAdapter(listAdapter);
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

            @Override            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<User> tasks = listAdapter.getData();
                        if(!tasks.get(position).getRole().toString().equals("Admin")){
                            adminViewModel.deleteUser(position,tasks);
                        }
                    }
                });
            }
        }).attachToRecyclerView(binding.allUsers);
        retrievePlaces();

    }


    private void retrievePlaces() {
        adminViewModel.getAllUsers();
        adminViewModel.adminLiveData.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> usr) {
                listAdapter.setData(usr);
                // Log.d("retr",usr.get(1).getEmail());
            }
        });

    }


    private void initAdminViewModel() {
        adminViewModel = new ViewModelProvider(this)
                .get(AdminViewModel.class);
    }

    private void Back_but(){
        Intent intent = new Intent((UsrActivity) requireActivity(), MainActivity.class);
        Context context= new MainActivity();
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(SHARED_PREF,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(EMAIL, "");
        editor.putString(PASSWORD, "");
        editor.putString(LVL, "");
        editor.apply();

        startActivity(intent);
    }
}
