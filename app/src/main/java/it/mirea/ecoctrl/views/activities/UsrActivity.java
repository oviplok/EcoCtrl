package it.mirea.ecoctrl.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import it.mirea.ecoctrl.models.User;
import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.viewModels.UsrViewModel;

public class UsrActivity extends AppCompatActivity {
    private static final String PASSWORD = "password";
    private static final String SHARED_PREF = "sharedPrefs";
    private static final String EMAIL = "email";
    private static final String LVL = "lvl";
    private UsrViewModel usrViewModel;
    ConstraintLayout USR;
    String lvl;
    int back_tap;
  //  String lvl ="level", pass_check="password",UsrPos="UserPos";
    TextView mail,level,pos;
    EditText old_pass,new_pass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference user = db.collection("Users");
    ImageButton back;
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usr);
        mail = findViewById(R.id.email_view);
        level = findViewById(R.id.lvl_view);
        pos = findViewById(R.id.pos_view);
        change = findViewById(R.id.pass_change);
        back = findViewById(R.id.back_butt);
        old_pass = findViewById(R.id.old_pass);
        new_pass = findViewById(R.id.new_pass);
        USR = findViewById(R.id.usr_action);
        String email = getIntent().getStringExtra("email");

        initUsrViewModel();
        Check_Acc(email);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_tap++;
                if (back_tap==2){
                    Back_but(email);
                    back_tap=0;
                }
                else
                {
                    Snackbar.make(USR, "If you want exit from your account, tap again",
                            Snackbar.LENGTH_LONG).show();
                }
            }


        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change_Pass(email,old_pass.getText().toString(),new_pass.getText().toString());
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////
    private void initUsrViewModel() {
        usrViewModel = new ViewModelProvider(this)
                .get(UsrViewModel.class);
    }
//////////////////////////////////////////////////////////////////////////////

    private void Check_Acc(String email){
        usrViewModel.Check_acc(email);
        usrViewModel.userLiveData.observe(UsrActivity.this, new Observer<User>() {

            @Override
            public void onChanged(User user) {
                if (user.isUsrResult()) {
                    lvl = user.getLvl();
                    mail.setText(getString(R.string.your_email)+user.getEmail());
                    level.setText(getString(R.string.your_lvl)+lvl);
                    pos.setText(getString(R.string.your_job)+ user.getJob());
                    user.setUsrResult(false);

                } else {
                    Snackbar.make(USR, "Не удалось загрузить пользователя",
                            Snackbar.LENGTH_LONG).show();

                }

            }
        });

    }


    private void Back_but(String email){
        Intent intent = new Intent(UsrActivity.this, MainActivity.class);
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(EMAIL, "");
        editor.putString(PASSWORD, "");
        editor.putString(LVL, "");
        editor.apply();
        switch (lvl){
            case ("red"):
                intent.putExtra("email", email);
                intent.putExtra("lvl","red");
                startActivity(intent);
                break;
            case("green"):
                //Intent intent = new Intent(UsrActivity.this, MapActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("lvl","green");
                startActivity(intent);
                break;
            default:
                Snackbar.make(USR, "Произошла ошибка!",
                        Snackbar.LENGTH_LONG).show();
                break;

        }
    }

       /* String lvl =UsrViewModel.back_check(level.getText().toString());
        String gr="green",rd="red";

            Intent intent = new Intent(UsrActivity.this, MapActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("lvl","green");
            startActivity(intent);

        if (lvl == "error")
        {
            Snackbar.make(USR, "Ошибка сервера!",
                    Snackbar.LENGTH_LONG).show();
        }*/




    private void Change_Pass(String email,String oldPass,String newPass){
        if (TextUtils.isEmpty(new_pass.getText().toString())) {
            new_pass.setHint("Введите пароль");
            new_pass.setHintTextColor(Color.parseColor("#CC0000"));
        }
        if (TextUtils.isEmpty(old_pass.getText().toString())) {
            old_pass.setHint("Введите пароль");
            old_pass.setHintTextColor(Color.parseColor("#CC0000"));
        }

        usrViewModel.Change_pass(email,oldPass,newPass);
        usrViewModel.userLiveData.observe(UsrActivity.this, new Observer<User>() {


            @Override
            public void onChanged(User user) {
                if (user.isUsrResult()) {
                    Snackbar.make(USR, "Пароль установлен!",
                            Snackbar.LENGTH_LONG).show();
                    user.setUsrResult(false);

                } else {
                    old_pass.setHint("Неверный пароль");
                    old_pass.setHintTextColor(Color.parseColor("#CC0000"));
                }

            }
        });

    }
}
