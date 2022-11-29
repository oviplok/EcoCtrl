package it.mirea.ecoctrl.viewModels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.views.activities.MainActivity;
import it.mirea.ecoctrl.views.activities.MapActivity;

public class AuthViewModel extends ViewModel {

    //private static final String SHARED_PREF = "sharedPrefs";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String LVL = "lvl";


    //MainActivity mainActivity = new MainActivity();
    public void auth (String token, MainActivity activity) {
        if (token != null) {
            if (ServiceLocator.getInstance().getUser() == null) {
                ServiceLocator.getInstance().getRepository().findUser(
                        activity.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity
                ).observe(activity, (user) -> {
                    if (user != null) {
                        ServiceLocator.getInstance().setUser(user);


                        activity.getPreferences(Context.MODE_PRIVATE).edit().putString(EMAIL, user.getEmail()).putString(PASSWORD, user.getPassword())
                                .putString(LVL, user.getRole().toString()).apply();
                        //Context context= new MainActivity();
                        //SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF,0);
                        //SharedPreferences.Editor editor = sharedPref.edit();
                        //editor.putString(EMAIL, user.getEmail());
                        //editor.putString(PASSWORD, user.getPassword());
                        //editor.putString(LVL, user.getRole().toString());
                        //editor.apply();

                        Intent intent = new Intent(activity, MapActivity.class);
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("lvl", user.getRole());
                        intent.putExtra("income_place","");
                        activity.startActivity(intent);

                       // Navigation.findNavController(activity.mBinding.navHostFragment).navigate(R.id.action_authFragment_to_partyList);
                    } else {
                        activity.getPreferences(Context.MODE_PRIVATE).edit().remove("email").remove("token").apply();
                    }
                });
            }
        }
    }

    public LiveData<User> auth (String login, String password, MainActivity activity) {
        ServiceLocator.getInstance().getRepository().findUser(login, password, activity).observe(activity, (user) -> {
            if (user != null) {
                ServiceLocator.getInstance().setUser(user);
               // activity.getPreferences(Context.MODE_PRIVATE).edit().putString("email", user.getEmail());
                activity.getPreferences(Context.MODE_PRIVATE).edit().putString(EMAIL, user.getEmail()).putString(PASSWORD, user.getPassword())
                        .putString(LVL, user.getRole().toString()).apply();
            }
        });
        return ServiceLocator.getInstance().getRepository().findUser(login, password, activity);
    }
}
