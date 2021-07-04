package com.aisolutions.bloodcare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aisolutions.bloodcare.Details.LoadingDialog;
import com.aisolutions.bloodcare.Objects.UserDetails;
import com.aisolutions.bloodcare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {

    TextView goReg, login;
    EditText userTelephone, userPassword;
    String uTelephone, uPassword;

    FirebaseFirestore loginFirestore = FirebaseFirestore.getInstance();

    UserDetails user = new UserDetails();

    SharedPreferences loginUserPreferences;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();

    LoadingDialog loadingDialog = new LoadingDialog(Login.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //-------full Screen
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        goReg = findViewById(R.id.login_reg);
        login = findViewById(R.id.login);
        userTelephone = findViewById(R.id.userTelephoneLogin);
        userPassword = findViewById(R.id.userPasswordLogin);

        //----------Intent to Registration Activity
        goReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });

        //----------Login to the System-------------------------------------------------------------
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyCheck()) {
                    loadingDialog.startLoading();
                    DocumentReference referenceLogin = loginFirestore.document("Users/" + uTelephone);
                    referenceLogin.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String fetchPassword = documentSnapshot.getString("userPassword");
                                Log.d("Fetch Password -->", fetchPassword);

                                //check password matching
                                if (uPassword.equals(fetchPassword)) {
                                    user = documentSnapshot.toObject(UserDetails.class);

                                    //add to SharedPreferences
                                    try {
                                        loginUserPreferences = getSharedPreferences("loginUser", MODE_PRIVATE);
                                        editor = loginUserPreferences.edit();
                                        String json = gson.toJson(user);
                                        editor.putString("LoginUser", json);
                                        editor.putString("USER_ID", uTelephone);
                                        editor.commit();
                                    } catch (Exception e) {
                                        Toast.makeText(Login.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    Toast.makeText(Login.this, "Successfully Login", Toast.LENGTH_SHORT).show();

                                    //goto Main Menu
                                    loadingDialog.dismissLoading();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    loadingDialog.dismissLoading();
                                    Toast.makeText(Login.this, "Your Username or Password is wrong", Toast.LENGTH_SHORT).show();
                                    userTelephone.setText("");
                                    userPassword.setText("");
                                    userTelephone.requestFocus();
                                }

                            } else {
                                loadingDialog.dismissLoading();
                                Toast.makeText(Login.this, "Login Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Error Login", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissLoading();
                            Log.d("Error Login--->", e.toString());
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Fill all Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //------------------------------------------------------------------------------------------
    }


    public boolean isEmptyCheck() {
        uTelephone = userTelephone.getText().toString();
        uPassword = userPassword.getText().toString();

        Log.d("Telephone", uTelephone);
        Log.d("Password", uPassword);

        if (!uTelephone.isEmpty() && !uPassword.isEmpty()) {
            Log.d("Empty-->", "True");
            return true;
        } else {
            Log.d("Empty-->", "False");
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}