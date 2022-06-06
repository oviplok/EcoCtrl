package it.mirea.ecoctrl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class UsrActivity extends AppCompatActivity {
    ConstraintLayout USR;
    String lvl ="level", pass_check="password",UsrPos="UserPos";
    TextView mail,level,pos;
    EditText old_pass,new_pass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference user = db.collection("Users");
    ImageButton back;
    Button change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usr);
        mail = findViewById(R.id.email_view);
        level = findViewById(R.id.lvl_view);
        pos = findViewById(R.id.pos_view);
        change = findViewById(R.id.pass_change);
        back = findViewById(R.id.back_butt);
        old_pass = findViewById(R.id.old_pass);
        new_pass = findViewById(R.id.new_pass);
        String email = getIntent().getStringExtra("email");
    user.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            mail.setText("Ваша почта: "+email);
            String infoLVL = documentSnapshot.getString(lvl);
            String infoPos = documentSnapshot.getString(UsrPos);
            level.setText("Ваш уроваень доступа: "+infoLVL);
            pos.setText("Должность: "+ infoPos);
        }
    });
    back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (level.getText().toString().equals("green")){
                Intent intent = new Intent(UsrActivity.this, MapActivityGreen.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
            else if(level.getText().toString().equals("red")){
                Intent intent = new Intent(UsrActivity.this, MapActivityRed.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(UsrActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    });
    change.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            user.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (TextUtils.isEmpty(new_pass.getText().toString())) {
                        new_pass.setHint("Введите пароль");
                        new_pass.setHintTextColor(Color.parseColor("#CC0000"));
                    }
                    if (TextUtils.isEmpty(old_pass.getText().toString())) {
                       // Snackbar.make(USR, "Введите пароль", Snackbar.LENGTH_SHORT).show();
                        old_pass.setHint("Введите пароль");
                        old_pass.setHintTextColor(Color.parseColor("#CC0000"));
                    }
                    String checker = documentSnapshot.getString(pass_check);
                    if (!old_pass.getText().toString().equals(checker)){
                        old_pass.setHint("Неверный пароль");
                        //Snackbar.make(USR, "Старый пароль неверный", Snackbar.LENGTH_SHORT).show();
                        old_pass.setHintTextColor(Color.parseColor("#CC0000"));
                    }
                    else {
                        String infoLVL = documentSnapshot.getString(lvl);
                        String infoPos = documentSnapshot.getString(UsrPos);

                        Map<String, Object> InfoChange = new HashMap<>();
                        InfoChange.put("UserPos", infoPos);
                        InfoChange.put("level", infoLVL);
                        InfoChange.put("email", email);
                        InfoChange.put("password", new_pass.getText().toString());

                        user.document(email).set(InfoChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Snackbar.make(USR, "Перепись прошла успешно!",
                                        Snackbar.LENGTH_LONG).show();
                            }

                        });
                    }
                }
            });
        }
    });
    }
}