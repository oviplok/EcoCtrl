package it.mirea.ecoctrl.views.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.databinding.UserFragmentBinding;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.viewModels.PlistViewModel;
import it.mirea.ecoctrl.viewModels.UserViewModel;
import it.mirea.ecoctrl.views.activities.MainActivity;
import it.mirea.ecoctrl.views.activities.UsrActivity;

public class UserFragment extends Fragment {
    private static final String PASSWORD = "password";
    private static final String SHARED_PREF = "sharedPrefs";
    private static final String EMAIL = "email";
    private static final String LVL = "lvl";

    private UserFragmentBinding userBinding;

    private UserViewModel userViewModel;



    String lvl;
    int back_tap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUserViewModel();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userBinding= UserFragmentBinding.inflate(inflater, container, false);

        Context context= new MainActivity();
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(SHARED_PREF,context.MODE_PRIVATE);
        Check_Acc(sharedPref.getString("email",""));

        userBinding.backButt.setOnClickListener(new View.OnClickListener() {
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
        userBinding.passChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_Pass(userBinding.emailView.toString(),userBinding.oldPass.getText().toString(),userBinding.newPass.getText().toString());
            }
        });

        return userBinding.getRoot();
    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(this)
                .get(UserViewModel.class);
    }

    private void Check_Acc(String email){
        userViewModel.Check_acc(email,(UsrActivity) requireActivity());
        userViewModel.userLiveData.observe(getViewLifecycleOwner(), (user) -> {
                if (!user.getEmail().isEmpty()) {
                    userBinding.idView.setText(user.getId());
                    userBinding.emailView.setText(getString(R.string.your_email)+ user.getEmail());
                    userBinding.lvlView.setText(getString(R.string.your_role)+user.getRole().toString());

                } else {
                    Toast.makeText(getContext(), "Не удалось загрузить пользователя", Toast.LENGTH_SHORT).show();
                }

        });

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

//        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF,0);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(EMAIL, "");
//        editor.putString(PASSWORD, "");
//        editor.putString(LVL, "");
//        editor.apply();
//        startActivity(intent);
    }

    private void Change_Pass(String email,String oldPass,String newPass){
        if (TextUtils.isEmpty(userBinding.newPass.getText().toString())) {
            userBinding.newPass.setHint("Введите пароль");
            userBinding.newPass.setHintTextColor(Color.parseColor("#CC0000"));
        }
        else if (TextUtils.isEmpty(userBinding.oldPass.getText().toString())) {
            userBinding.oldPass.setHint("Введите пароль");
            userBinding.oldPass.setHintTextColor(Color.parseColor("#CC0000"));
        }
        else{

            userViewModel.Check_acc(email,(UsrActivity) requireActivity());
            userViewModel.userLiveData.observe(getViewLifecycleOwner(), (user) -> {
                if (!user.getPassword().isEmpty() || user.getPassword()==oldPass) {
                    userViewModel.Change_pass(user.getEmail(),user.getId(),
                            newPass,user.getConnections().get("vk"),
                            user.getRole(),user.getPhone(),
                            user.getFirst_name(),user.getLast_name());

                } else {
                    Toast.makeText(getContext(), "Не удалось обновить пароль", Toast.LENGTH_SHORT).show();

                }

            });

        }
       // userViewModel.Change_pass(email,oldPass,newPass);
        /*usrViewModel.userLiveData.observe(UsrActivity.this, new Observer<UserFireBase>() {

            @Override
            public void onChanged(UserFireBase userFireBase) {
                if (userFireBase.isUsrResult()) {
                    Snackbar.make(USR, "Пароль установлен!",
                            Snackbar.LENGTH_LONG).show();
                    userFireBase.setUsrResult(false);

                } else {
                    old_pass.setHint("Неверный пароль");
                    old_pass.setHintTextColor(Color.parseColor("#CC0000"));
                }

            }
        });*/
    }
}

