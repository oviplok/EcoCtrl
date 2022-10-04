package it.mirea.ecoctrl.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
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
import it.mirea.ecoctrl.models.User;
import it.mirea.ecoctrl.viewModels.MainViewModel;


public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;

    Button to_reg, to_login;

    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainViewModel();

        to_reg = findViewById(R.id.to_registr);
        to_login = findViewById(R.id.to_login);

        root = findViewById(R.id.root_action);

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

    //////////////////////////////////////////////////////////////////////////////
    private void initMainViewModel() {
        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);
    }
//////////////////////////////////////////////////////////////////////////////

    //Регистрация
    private void showRegWindow() {


        AlertDialog.Builder reg_act = new AlertDialog.Builder(this);
        reg_act.setTitle("Регистрация");
        reg_act.setMessage("Введите данные регистрации");

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
                        if (user.usrResult){
                            Intent intent = new Intent(MainActivity.this, MapActivity.class);
                            Log.e("Activ",email.getText().toString()+" "+user.lvl);
                            intent.putExtra("email", user.email);
                            intent.putExtra("lvl", user.lvl);
                            startActivity(intent);
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
                        if (user.usrResult){
                            Intent intent = new Intent(MainActivity.this, MapActivity.class);
                            intent.putExtra("email", "anon");
                            intent.putExtra("lvl", "red");
                            startActivity(intent);
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
                        if (user.usrResult){
                            Intent intent = new Intent(MainActivity.this, MapActivity.class);
                            intent.putExtra("email", user.email);
                            intent.putExtra("lvl", user.lvl);
                            startActivity(intent);
                        }
                        else{
                            Snackbar.make(root,"Ошибка авторизации."+user.email+" "+user.lvl,Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        log_act.show();
    }

}
