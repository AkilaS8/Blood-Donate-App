package com.aisolutions.bloodcare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aisolutions.bloodcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    ImageView menu1, menu2, menu3, menu4, menu5, menu6;
    TextView findDonor, seeRequest, donorCount, requestCount;
    String Status = "";
    Dialog dialog;

    SharedPreferences donorStatus;
    SharedPreferences.Editor editor;

    FirebaseFirestore statusFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);

        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);
        menu4 = findViewById(R.id.menu4);
        menu5 = findViewById(R.id.menu5);
        menu6 = findViewById(R.id.menu6);

        findDonor = findViewById(R.id.find_donor);
        seeRequest = findViewById(R.id.see_request);
        donorCount = findViewById(R.id.donor_count);
        requestCount = findViewById(R.id.request_count);

        //----configure the dialog view on donor status-----------------------
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_box_donor_status);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView okay = dialog.findViewById(R.id.dialog_status_ok);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationDialog;
        //---------------------------------------------------------------------

        //-----Find Donor------------
        findDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDonor.startAnimation(animation);
                Intent intent = new Intent(MainActivity.this, ExploreDonor.class);
                startActivity(intent);
            }
        });
        //-----See Request--------
        seeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeRequest.startAnimation(animation);
                Intent intent = new Intent(MainActivity.this, ExploreRequest.class);
                startActivity(intent);
            }
        });

        //-----Menu 1 --> Become Donor
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status.equals("YES")) {
                    //TODO:show dialogbox
                    dialog.show();
                } else {
                    menu1.startAnimation(animation);
                    Intent intent = new Intent(MainActivity.this, BecomeDonor.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //-----Menu 2 --> Add Request
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu2.startAnimation(animation);
                Intent intent = new Intent(MainActivity.this, AddRequest.class);
                startActivity(intent);
                finish();
            }
        });

        //-----Menu 3 --> Explore Donors
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu3.startAnimation(animation);
                Intent intent = new Intent(MainActivity.this, ExploreDonor.class);
                startActivity(intent);
                finish();
            }
        });

        //-----Menu 4 --> Explore Requests
        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu4.startAnimation(animation);
                Intent intent = new Intent(MainActivity.this, ExploreRequest.class);
                startActivity(intent);
                finish();
            }
        });

        //-----Menu 5 --> Details
        menu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu5.startAnimation(animation);
                Intent intent = new Intent(MainActivity.this, Details.class);
                startActivity(intent);
                finish();
            }
        });

        //-----Menu 6 --> Profile
        menu6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu6.startAnimation(animation);
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void checkStatus() {

        SharedPreferences preferences = getSharedPreferences("loginUser", MODE_PRIVATE);
        String USER_ID = preferences.getString("USER_ID", "");
        Log.d("USER_ID", USER_ID);

        statusFirestore.collection("Donors").document(USER_ID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            Status = "YES";

                        } else {
                            Status = "NO";
                        }
                        donorStatus = getSharedPreferences("donorStatus",MODE_PRIVATE);
                        editor = donorStatus.edit();
                        editor.putString("donorStatus",Status);
                        editor.commit();
                    }
                });
    }

    public void countDonors() {
        statusFirestore.collection("Donors").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int dCount = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                dCount++;
                            }
                            donorCount.setText(String.valueOf(dCount));
                        } else {
                            Log.d("Error getting documents: ", String.valueOf(task.getException()));
                        }
                    }
                });
    }

    public void countRequest() {
        statusFirestore.collection("Requests").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int rCount = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                rCount++;
                            }
                            requestCount.setText(String.valueOf(rCount));
                        } else {
                            Log.d("Error getting documents: ", String.valueOf(task.getException()));
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        checkStatus();
        countDonors();
        countRequest();
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}