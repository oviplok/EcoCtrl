package it.mirea.ecoctrl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.mirea.ecoctrl.Models.User;


public class MainActivity extends AppCompatActivity {
    Button to_signin, to_login;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference users;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference passCol = db.collection("Users");


    RelativeLayout root;
    String level = "level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        to_signin = findViewById(R.id.to_registr);
        to_login = findViewById(R.id.to_login);

        root = findViewById(R.id.root_action);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogInWindow();
            }
        });
        to_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegWindow();

            }

        });
    }

    //??????????????????????
    private void showRegWindow() {
        AlertDialog.Builder reg_act = new AlertDialog.Builder(this);
        reg_act.setTitle("??????????????????????");
        reg_act.setMessage("?????????????? ???????????? ??????????????????????");

        LayoutInflater inflater = LayoutInflater.from(this);
        View SigIn_Window = inflater.inflate(R.layout.activity_reg, null);
        reg_act.setView(SigIn_Window);

        final EditText email = SigIn_Window.findViewById(R.id.email_sig);
        final EditText password = SigIn_Window.findViewById(R.id.password_sig);
        final EditText userlevel = SigIn_Window.findViewById(R.id.lvl_sig);


        reg_act.setNegativeButton("??????????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        reg_act.setPositiveButton("????????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //?????????????????? ???????????? ?????? ??????????????????????
                if (TextUtils.isEmpty(email.getText().toString())) {

                    Snackbar.make(root, "?????????????? ???????? ??????????", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }
                //???????????????? ?????????? ???? ?????????????? ?????? ??????????
                /*if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Snackbar.make(root, "???? ???????????? ???? ??????????", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }*/
                Matcher m = Pattern.compile(
                        "[a-zA-Z0-9]+[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~.]*@[a-zA-Z0-9\\-_.]+\\.[a-zA-Z]+"
                         ).matcher(email.getText().toString());
                if(!(m.find() && m.group().equals(email.getText().toString()))){
                    Snackbar.make(root, "???? ???????????? ???? ??????????", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }

                if (TextUtils.isEmpty(userlevel.getText().toString())) {
                    Snackbar.make(root, "?????????????? ??????????????????", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }


                if (password.getText().toString().length() < 4) {

                    Snackbar.make(root, "???????????? ???????????????????????? ??????????????(", Snackbar.LENGTH_LONG).show();
                    showRegWindow();
                    return;
                }

                //?????????????????????? ??????????
                //???????? ?????????? ???? ???? ??????????????????
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setLvl(userlevel.getText().toString());
                                user.setPassword(password.getText().toString());
                                Map<String, Object> userCode = new HashMap<>();
                                userCode.put("email", email.getText().toString());
                                userCode.put("level", "red");
                                userCode.put("UserPos",userlevel.getText().toString());
                                userCode.put("password", password.getText().toString());
                                ////// ???????????? ?? ???????? ????????????????
                                passCol.document(email.getText().toString()).set(userCode).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(root,"???? ?????????????? ???????????????? ?? ???????? ????????????????", Snackbar.LENGTH_LONG).show();
                                    }
                                });

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "???????????????????????? ????????????????", Snackbar.LENGTH_LONG).show();
                                            }
                                        });

                            }
                        });

            }
        });
        reg_act.show();
        //showLogInWindow();
    }
    //????????
    private void showLogInWindow(){
        AlertDialog.Builder log_act = new AlertDialog.Builder(this);
        log_act.setTitle("????????");
        log_act.setMessage("?????????????? ???????????? ?????? ??????????");

        LayoutInflater inflater = LayoutInflater.from(this);
        View SigIn_Window = inflater.inflate(R.layout.activity_login, null);
        log_act.setView(SigIn_Window);


        final EditText email = SigIn_Window.findViewById(R.id.email_log);
        final EditText password = SigIn_Window.findViewById(R.id.password_log);
        log_act.setNegativeButton("??????????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        log_act.setNeutralButton("?????????? ?????? ??????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.signInAnonymously();
                ////////////////////////////
                startActivity(new Intent(MainActivity.this, MapActivityGreen.class));
            }
        });
        log_act.setPositiveButton("????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //?????????????????? ???????????? ?????? ??????????????????????
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "?????????????? ???????? ??????????", Snackbar.LENGTH_SHORT).show();
                    showLogInWindow();
                    return;
                }
                //???????????????? ?????????? ???? ?????????????? ?????? ??????????
                Matcher m = Pattern.compile(
                        "[a-zA-Z0-9]+[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~.]*@[a-zA-Z0-9\\-_.]+\\.[a-zA-Z]+"
                ).matcher(email.getText().toString());
                if(!(m.find() && m.group().equals(email.getText().toString()))){
                    Snackbar.make(root, "???? ???????????? ???? ??????????", Snackbar.LENGTH_LONG).show();
                    showLogInWindow();
                    return;
                }

                if (TextUtils.isEmpty(password.getText().toString())) {
                    Snackbar.make(root, "?????????????? ????????????", Snackbar.LENGTH_SHORT).show();
                    showLogInWindow();
                    return;
                }
                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                passCol.document(email.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        //String red = "red";
                                        String green = "green";
                                        String userlevel = documentSnapshot.getString(level);
                                        ///???????? ?? ?????????? ?????????????? ??????????????
                                        if (userlevel.equals(green)){
                                            startActivity(new Intent(MainActivity.this,
                                                    MapActivityGreen.class));

                                        }
                                        //if (userlevel.equals(red)){
                                        // }
                                        else
                                            startActivity(new Intent(MainActivity.this,
                                                    MapActivityRed.class));


                                    }
                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root,"???????????? ??????????????????????. "+ e.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        log_act.show();
    }




}
