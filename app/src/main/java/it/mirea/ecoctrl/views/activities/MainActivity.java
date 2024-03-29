package it.mirea.ecoctrl.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;

import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.models.User;
import it.mirea.ecoctrl.viewModels.MainViewModel;


public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREF = "sharedPrefs";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String LVL = "lvl";
    private MainViewModel mainViewModel;

    String income_place;
    Button to_reg, to_login;
    boolean connected = false;
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainViewModel();

        SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        String Email =sharedPreferences.getString(EMAIL,"");
        String LEVEL=sharedPreferences.getString(LVL,"");
        String Password=sharedPreferences.getString(PASSWORD,"");

        to_reg = findViewById(R.id.to_registr);
        to_login = findViewById(R.id.to_login);
        root = findViewById(R.id.root_action);

        Uri income = getIntent().getData();

        if (income != null) {
            String[] parts = income.toString().split("/");
            income_place = parts[parts.length - 1];
            Log.e("Map_app", income_place);
        }
       // Log.e("Place_app", "ServiceLocator.getInstance().getGson().toJson(income)");
       // Log.e("Place_app", ServiceLocator.getInstance().getGson().toJson(income));

        //CONNECTION *****WIP*****
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            if (income_place==null){
                income_place="";
            }
            if(!Email.equals("") && !LEVEL.equals("") && !Password.equals("")){
                mainViewModel.LogInWindow(Email,Password);
                mapAct(Email,LEVEL,income_place);
            }
        }
        else {
            connected = false;
            if(!Email.equals("") && !LEVEL.equals("") && !Password.equals("")){
                mapAct(Email,LEVEL,income_place);
            }
            else{
                mapAct("anon","red",income_place);
            }
        }

        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogInWindow();
            }
        });
        to_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegWindow();

            }
        });
    }

    private void initMainViewModel() {
        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
    }

    private void mapAct(String email,String lvl,String income_place){
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("lvl", lvl);
        intent.putExtra("income_place",income_place);
        startActivity(intent);
    }

    //Регистрация
    private void showRegWindow() {

        AlertDialog.Builder reg_act = new AlertDialog.Builder(this);
        reg_act.setTitle(R.string.registration);
        reg_act.setMessage(R.string.insert_authData);

        LayoutInflater inflater = LayoutInflater.from(this);
        View SigIn_Window = inflater.inflate(R.layout.activity_reg, null);
        reg_act.setView(SigIn_Window);

        final EditText email = SigIn_Window.findViewById(R.id.email_sig);
        final EditText password = SigIn_Window.findViewById(R.id.password_sig);
        final EditText userjob = SigIn_Window.findViewById(R.id.job_sig);

        reg_act.setNegativeButton("Вернуться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        reg_act.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //возможные ошибки при регистрации
                if (TextUtils.isEmpty(email.getText().toString())) {

                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }
                //Проверка почты на сколько это почта
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Snackbar.make(root, "Не похоже на почту", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }

                if (TextUtils.isEmpty(userjob.getText().toString())) {
                    Snackbar.make(root, "Введите должность", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }


                if (password.getText().toString().length() < 4) {

                    Snackbar.make(root, "Пароль недостаточно сложный(", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }

                //Регистрация юзера
                mainViewModel.RegInWindow(email.getText().toString(),
                        password.getText().toString(),userjob.getText().toString());
                mainViewModel.userLiveData.observe(MainActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user.isUsrResult()){

                            SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(EMAIL, user.getEmail());
                            editor.putString(PASSWORD, user.getPassword());
                            editor.putString(LVL, user.getLvl());
                            editor.apply();
                            mapAct(EMAIL,LVL,"");
                        }
                        else {
                            Snackbar.make(root,"Ошибка регистрации.",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        reg_act.show();
    }

    //вход
    private void showLogInWindow(){

        AlertDialog.Builder log_act = new AlertDialog.Builder(this);
        log_act.setTitle("Вход");
        log_act.setMessage("Введите данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View SigIn_Window = inflater.inflate(R.layout.activity_login, null);
        log_act.setView(SigIn_Window);

        final EditText email = SigIn_Window.findViewById(R.id.email_log);
        final EditText password = SigIn_Window.findViewById(R.id.password_log);
        log_act.setNegativeButton("Вернуться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });


        log_act.setNeutralButton("Войти как гость", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainViewModel.LogInWindow("anon",null);
                mainViewModel.userLiveData.observe(MainActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user.isUsrResult()){
                            mapAct("anon","red","");

                        }
                    }
                });
            }
        });
        log_act.setPositiveButton("Вход", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //возможные ошибки при регистрации
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    showLogInWindow();
                    return;
                }
                //Проверка почты на сколько это почта
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Snackbar.make(root, "Не похоже на почту", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }

                if (TextUtils.isEmpty(password.getText().toString())) {
                    Snackbar.make(root, "Введите пароль", Snackbar.LENGTH_SHORT).show();
                    showLogInWindow();
                    return;
                }

                mainViewModel.LogInWindow(email.getText().toString(),password.getText().toString());
                mainViewModel.userLiveData.observe(MainActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user.isUsrResult()){

                            SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(EMAIL, user.getEmail());
                            editor.putString(PASSWORD, user.getPassword());
                            editor.putString(LVL, user.getLvl());
                            editor.apply();
                            mapAct(user.getEmail(), user.getLvl(),"");
                        }
                        else{
                            Snackbar.make(root,getString(R.string.auth_error)+user.getEmail()+" "+user.getLvl(),Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        log_act.show();
    }
}
