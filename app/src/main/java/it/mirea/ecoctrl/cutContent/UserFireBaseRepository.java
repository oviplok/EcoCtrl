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

import it.mirea.ecoctrl.repositories.models.User;

public class UserFireBaseRepository {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference users;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference passCol = db.collection(User.getCallPath());
    private User user = new User();

    public MutableLiveData<User> AnonLogIn() {
        MutableLiveData<User> isUserAuthLiveData = new MutableLiveData<>();
        auth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                user.setUsrResult(true);
                isUserAuthLiveData.setValue(user);
            }
        });
        return isUserAuthLiveData;
    }

    public MutableLiveData<User> LogIn(String email, String password) {
        MutableLiveData<User> isUserAuthLiveData = new MutableLiveData<>();

        user.setEmail(email);
        user.setPassword(password);
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        passCol.document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                user.setLvl(documentSnapshot.getString("level"));
                                user.setUsrResult(true);
                                isUserAuthLiveData.setValue(user);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@Nonnull Exception e) {
                user.setUsrResult(false);
                Log.e("AUTH", "auth Error");
                isUserAuthLiveData.setValue(user);

            }
        });
        return isUserAuthLiveData;
    }

    public MutableLiveData<User> RegIn(String email, String password, String job) {
        MutableLiveData<User> isUserAuthLiveData = new MutableLiveData<>();
        Map<String, Object> userCode = new HashMap<>();

        user.setEmail(email);
        user.setPassword(password);
        user.setJob(job);
        user.setLvl("red");

        if (user.getEmail() != null && user.getPassword() != null && user.getJob() != null) {
            auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userCode.put("email", user.getEmail());
                            userCode.put("level", user.getLvl());
                            userCode.put("UserPos", user.getJob());
                            userCode.put("password", user.getPassword());


                            Log.e("user.", user.getEmail() + " " + user.getPassword() + " " + user.getJob());
                            ////// запись в базу пользователей
                            passCol.document(user.getEmail()).set(userCode)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            user.setUsrResult(true);
                                            isUserAuthLiveData.setValue(user);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NotNull Exception e) {
                                    user.setUsrResult(false);
                                    isUserAuthLiveData.setValue(user);
                                    Log.e("FireBase", "pass Error");
                                }
                            });
                            users = database.getReference("Users");
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user.getEmail())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            user.setUsrResult(true);
                                            isUserAuthLiveData.setValue(user);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NotNull Exception e) {
                                    user.setUsrResult(false);
                                    isUserAuthLiveData.setValue(user);
                                    Log.e("FireBase", "child Error");
                                }
                            });

                        }
                    });
        } else {
            user.setUsrResult(false);
            isUserAuthLiveData.setValue(user);
            Log.e("FireBase", "entering Error");
        }
        return isUserAuthLiveData;
    }

    public MutableLiveData<User> Check_acc(String email) {
        MutableLiveData<User> isUserCheckLiveData = new MutableLiveData<>();

        passCol.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                user.setLvl(documentSnapshot.getString("level"));
                user.setJob(documentSnapshot.getString("UserPos"));
                user.setEmail(email);
                user.setUsrResult(true);
                isUserCheckLiveData.setValue(user);
            }

        });

        return isUserCheckLiveData;
    }

    public MutableLiveData<User> Change_pass(String email, String oldPass, String newPass) {
        MutableLiveData<User> isUserCheckLiveData = new MutableLiveData<>();
        passCol.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String checker = documentSnapshot.getString("password");
                if (!oldPass.equals(checker)) {
                    user.setUsrResult(false);
                    isUserCheckLiveData.setValue(user);
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
                            user.setUsrResult(true);
                            isUserCheckLiveData.setValue(user);
                        }

                    });
                }
            }
        });
        return isUserCheckLiveData;
    }
}

