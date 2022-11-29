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

import it.mirea.ecoctrl.databinding.AuthFragmentBinding;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.viewModels.AuthViewModel;
import it.mirea.ecoctrl.views.activities.MainActivity;
import it.mirea.ecoctrl.views.activities.MapActivity;

public class AuthFragment extends Fragment {

    private static final String SHARED_PREF = "sharedPrefs";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String LVL = "lvl";

    private AuthViewModel authViewModel;
    private AuthFragmentBinding authBinding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        authBinding = AuthFragmentBinding.inflate(inflater, container, false);


        authBinding.vkAuth.setOnClickListener((view) -> ServiceLocator.getInstance().getVK_API().auth.auth((MainActivity) requireActivity()));

        authBinding.auth.setOnClickListener((view) -> {
            if (!authBinding.login.getText().toString().isEmpty() && !authBinding.password.getText().toString().isEmpty()) {
                authViewModel.auth(authBinding.login.getText().toString(), authBinding.password.getText().toString(), (MainActivity) requireActivity()).observe(getViewLifecycleOwner(), (user) -> {
                    if (user != null) {

                        Context context= new MainActivity();
                        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(SHARED_PREF,context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(EMAIL, user.getEmail());
                        editor.putString(PASSWORD, user.getPassword());
                        editor.putString(LVL, user.getRole().toString());
                        editor.apply();



                        Intent intent = new Intent((MainActivity) requireActivity(), MapActivity.class);
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("lvl", user.getRole());
                        intent.putExtra("income_place","");
                        startActivity(intent);

                    }
                });
            }
        });

        try {
            if (getActivity().getPreferences(Context.MODE_PRIVATE).contains("token")) {
                authViewModel.auth(
                        getActivity().getPreferences(Context.MODE_PRIVATE).getString("token", null),
                        (MainActivity) requireActivity()
                );
            }
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

        return authBinding.getRoot();
    }
}
