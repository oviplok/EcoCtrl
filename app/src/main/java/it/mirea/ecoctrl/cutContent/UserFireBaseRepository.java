package it.mirea.ecoctrl.cutContent;

import android.util.Log;

//import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import it.mirea.ecoctrl.repositories.models.UserFireBase;

public class UserFireBaseRepository {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference users;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference passCol = db.collection(UserFireBase.getCallPath());
    private UserFireBase userFireBase = new UserFireBase();

    public MutableLiveData<UserFireBase> AnonLogIn() {
        MutableLiveData<UserFireBase> isUserAuthLiveData = new MutableLiveData<>();
        auth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                userFireBase.setUsrResult(true);
                isUserAuthLiveData.setValue(userFireBase);
            }
        });
        return isUserAuthLiveData;
    }

    public MutableLiveData<UserFireBase> LogIn(String email, String password) {
        MutableLiveData<UserFireBase> isUserAuthLiveData = new MutableLiveData<>();

        userFireBase.setEmail(email);
        userFireBase.setPassword(password);
        auth.signInWithEmailAndPassword(userFireBase.getEmail(), userFireBase.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        passCol.document(userFireBase.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                userFireBase.setLvl(documentSnapshot.getString("level"));
                                userFireBase.setUsrResult(true);
                                isUserAuthLiveData.setValue(userFireBase);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@Nonnull Exception e) {
                userFireBase.setUsrResult(false);
                Log.e("AUTH", "auth Error");
                isUserAuthLiveData.setValue(userFireBase);

            }
        });
        return isUserAuthLiveData;
    }

    public MutableLiveData<UserFireBase> RegIn(String email, String password, String job) {
        MutableLiveData<UserFireBase> isUserAuthLiveData = new MutableLiveData<>();
        Map<String, Object> userCode = new HashMap<>();

        userFireBase.setEmail(email);
        userFireBase.setPassword(password);
        userFireBase.setJob(job);
        userFireBase.setLvl("red");

        if (userFireBase.getEmail() != null && userFireBase.getPassword() != null && userFireBase.getJob() != null) {
            auth.createUserWithEmailAndPassword(userFireBase.getEmail(), userFireBase.getPassword())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userCode.put("email", userFireBase.getEmail());
                            userCode.put("level", userFireBase.getLvl());
                            userCode.put("UserPos", userFireBase.getJob());
                            userCode.put("password", userFireBase.getPassword());


                            Log.e("user.", userFireBase.getEmail() + " " + userFireBase.getPassword() + " " + userFireBase.getJob());
                            ////// запись в базу пользователей
                            passCol.document(userFireBase.getEmail()).set(userCode)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            userFireBase.setUsrResult(true);
                                            isUserAuthLiveData.setValue(userFireBase);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NotNull Exception e) {
                                    userFireBase.setUsrResult(false);
                                    isUserAuthLiveData.setValue(userFireBase);
                                    Log.e("FireBase", "pass Error");
                                }
                            });
                            users = database.getReference("Users");
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(userFireBase.getEmail())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            userFireBase.setUsrResult(true);
                                            isUserAuthLiveData.setValue(userFireBase);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NotNull Exception e) {
                                    userFireBase.setUsrResult(false);
                                    isUserAuthLiveData.setValue(userFireBase);
                                    Log.e("FireBase", "child Error");
                                }
                            });

                        }
                    });
        } else {
            userFireBase.setUsrResult(false);
            isUserAuthLiveData.setValue(userFireBase);
            Log.e("FireBase", "entering Error");
        }
        return isUserAuthLiveData;
    }

    public MutableLiveData<UserFireBase> Check_acc(String email) {
        MutableLiveData<UserFireBase> isUserCheckLiveData = new MutableLiveData<>();

        passCol.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                userFireBase.setLvl(documentSnapshot.getString("level"));
                userFireBase.setJob(documentSnapshot.getString("UserPos"));
                userFireBase.setEmail(email);
                userFireBase.setUsrResult(true);
                isUserCheckLiveData.setValue(userFireBase);
            }

        });

        return isUserCheckLiveData;
    }

    public MutableLiveData<UserFireBase> Change_pass(String email, String oldPass, String newPass) {
        MutableLiveData<UserFireBase> isUserCheckLiveData = new MutableLiveData<>();
        passCol.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String checker = documentSnapshot.getString("password");
                if (!oldPass.equals(checker)) {
                    userFireBase.setUsrResult(false);
                    isUserCheckLiveData.setValue(userFireBase);
                } else {
                    String infoLVL = documentSnapshot.getString("level");
                    String infoPos = documentSnapshot.getString("UserPos");

                    Map<String, Object> InfoChange = new HashMap<>();
                    InfoChange.put("UserPos", infoPos);
                    InfoChange.put("level", infoLVL);
                    InfoChange.put("email", email);
                    InfoChange.put("password", newPass);

                    passCol.document(email).set(InfoChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            userFireBase.setUsrResult(true);
                            isUserCheckLiveData.setValue(userFireBase);
                        }

                    });
                }
            }
        });
        return isUserCheckLiveData;
    }
}

