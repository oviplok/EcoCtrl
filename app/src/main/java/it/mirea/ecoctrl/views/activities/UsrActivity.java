package it.mirea.ecoctrl.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

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

import it.mirea.ecoctrl.databinding.ActivityUsrBinding;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.R;
import it.mirea.ecoctrl.viewModels.UserViewModel;

public class UsrActivity extends AppCompatActivity {

    public ActivityUsrBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUsrBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
//        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
//        if(sharedPref.getString(LVL,"")=="Admin"){
//
//        }
//        else{
//
//        }

//        setContentView(R.layout.activity_usr);
//        mail = findViewById(R.id.email_view);
//        level = findViewById(R.id.lvl_view);
//        pos = findViewById(R.id.pos_view);
//        change = findViewById(R.id.pass_change);
//        back = findViewById(R.id.back_butt);
//        old_pass = findViewById(R.id.old_pass);
//        new_pass = findViewById(R.id.new_pass);
//        USR = findViewById(R.id.usr_action);
//        String email = getIntent().getStringExtra("email");
//
//        initUsrViewModel();
//        Check_Acc(email);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (back_tap==1){
//                    Back_but();
//                    back_tap=0;
//                }
//                else
//                {
//                    back_tap++;
//                    Snackbar.make(USR, "If you want exit from your account, tap again",
//                            Snackbar.LENGTH_LONG).show();
//                }
//            }
//        });
//        change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Change_Pass(email,old_pass.getText().toString(),new_pass.getText().toString());
//            }
//        });
    }

//    private void initUsrViewModel() {
//        userViewModel = new ViewModelProvider(this)
//                .get(UserViewModel.class);
//    }
//
//    private void Check_Acc(String email){
//        userViewModel.Check_acc(email,this);
//        userViewModel.userLiveData.observe(UsrActivity.this, new Observer<User>() {
//
//            @Override
//            public void onChanged(User user) {
//                if (!user.getEmail().isEmpty()) {
//                    lvl = user.getRole().toString();
//                    mail.setText(getString(R.string.your_email)+ user.getEmail());
//                    level.setText(getString(R.string.your_role)+lvl);
//                   // pos.setText(getString(R.string.your_job)+ userFireBase.getJob());
//                   // user.setUsrResult(false);
//
//                } else {
//                    Snackbar.make(USR, "Не удалось загрузить пользователя",
//                            Snackbar.LENGTH_LONG).show();
//
//                }
//
//            }
//        });
//
//    }
//
//
//    private void Back_but(){
//        Intent intent = new Intent(UsrActivity.this, MainActivity.class);
//        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(EMAIL, "");
//        editor.putString(PASSWORD, "");
//        editor.putString(LVL, "");
//        editor.apply();
//        startActivity(intent);
//    }
//
//    private void Change_Pass(String email,String oldPass,String newPass){
//        if (TextUtils.isEmpty(new_pass.getText().toString())) {
//            new_pass.setHint("Введите пароль");
//            new_pass.setHintTextColor(Color.parseColor("#CC0000"));
//        }
//        if (TextUtils.isEmpty(old_pass.getText().toString())) {
//            old_pass.setHint("Введите пароль");
//            old_pass.setHintTextColor(Color.parseColor("#CC0000"));
//        }
//
//        userViewModel.Change_pass(email,oldPass,newPass);
//        /*usrViewModel.userLiveData.observe(UsrActivity.this, new Observer<UserFireBase>() {
//
//            @Override
//            public void onChanged(UserFireBase userFireBase) {
//                if (userFireBase.isUsrResult()) {
//                    Snackbar.make(USR, "Пароль установлен!",
//                            Snackbar.LENGTH_LONG).show();
//                    userFireBase.setUsrResult(false);
//
//                } else {
//                    old_pass.setHint("Неверный пароль");
//                    old_pass.setHintTextColor(Color.parseColor("#CC0000"));
//                }
//
//            }
//        });*/
//    }
}
