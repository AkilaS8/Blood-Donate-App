package com.aisolutions.bloodcare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aisolutions.bloodcare.Details.ArraySets;
import com.aisolutions.bloodcare.Objects.RequestBlood;
import com.aisolutions.bloodcare.Objects.UserDetails;
import com.aisolutions.bloodcare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddRequest extends AppCompatActivity {
    ImageView backMain;
    Spinner bloodFor, district;
    RadioGroup bloodGroup;
    RadioButton selectBloodGroup;
    TextView addRequest;

    UserDetails loginUser;

    ArraySets arraySets = new ArraySets();

    ArrayAdapter<String> arrayAdapterBloodFor;
    ArrayAdapter<String> arrayAdapterRequestDistrict;

    ArrayList<String> arrayList_bloodFor = new ArrayList<>();
    ArrayList<String> arrayList_requestDistrict = new ArrayList<>();

    //--------------------->
    public String requestName = "";
    public String requestTelephone = "";
    public String requestDistrict = "";
    public String requestForWhom = "";
    public String requestBloodGroup = "";
    public String requestDate = "";
    //--------------------->

    FirebaseFirestore requestBlood = FirebaseFirestore.getInstance();
    FirebaseFirestore myRequestBlood = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        bloodFor = findViewById(R.id.bd_for);
        district = findViewById(R.id.requestDistrict);
        bloodGroup = findViewById(R.id.requestBloodGroup);
        addRequest = findViewById(R.id.addRequest);

        //------------------Fill login user data----------------------------------------------------
        SharedPreferences loginUserPreferences = getSharedPreferences("loginUser", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loginUserPreferences.getString("LoginUser", "");
        loginUser = gson.fromJson(json, UserDetails.class);

        Log.d("userName-->", loginUser.getUserName());
        //------------------------------------------------------------------------------------------


        //----------Loading data into ArrayList-----------------------------------------------------
        //----blood for
        for (int x = 0; x < arraySets.bloodFor.length; x++) {
            arrayList_bloodFor.add(arraySets.bloodFor[x]);
        }
        //----district
        for (int y = 0; y < arraySets.requestDistrict.length; y++) {
            arrayList_requestDistrict.add(arraySets.requestDistrict[y]);
        }
        //------------------------------------------------------------------------------------------

        //--------------Array Adapters--------------------------------------------------------------
        arrayAdapterBloodFor = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_bloodFor);
        bloodFor.setAdapter(arrayAdapterBloodFor);

        bloodFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                requestForWhom = arraySets.bloodFor[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        arrayAdapterRequestDistrict = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_requestDistrict);
        district.setAdapter(arrayAdapterRequestDistrict);

        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                requestDistrict = arraySets.requestDistrict[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //------------------------------------------------------------------------------------------

        //----------Intent to Main Activity--------------------------
        backMain = findViewById(R.id.backmain);
        backMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRequest.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //-------------------------------------------------------------

        //-----Set Date----------------------------

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        requestDate = formatter.format(todayDate);
        //-----------------------------------------

        //-----------------Save Data into cloud Store-----------------------------------------------
        addRequest.setOnClickListener(new View.OnClickListener() {
            String re_id = "";

            @Override
            public void onClick(View v) {
                checkBloodGroup();
                if (isEmptyCheck()) {
                    re_id = String.valueOf(System.currentTimeMillis());
                    DocumentReference requestReference = requestBlood.document("Requests/" + requestTelephone + "_" + re_id);
                    RequestBlood requestBlood = new RequestBlood(re_id, requestName, requestTelephone, requestDistrict, requestForWhom, requestBloodGroup, requestDate);

                    requestReference.set(requestBlood).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //add data to my request collection
                            DocumentReference myRequestReference = myRequestBlood.document("MyRequests/" + requestTelephone + "/" + requestTelephone + "/" + re_id);
                            myRequestReference.set(requestBlood).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddRequest.this, "Your Request is Successfully Added", Toast.LENGTH_SHORT).show();

                                    //automatically close the Activity and go to LOGIN
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(AddRequest.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 2000);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddRequest.this, "Something Wrong. Try Again", Toast.LENGTH_SHORT).show();
                                    Log.d("Request Add problem ", e.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddRequest.this, "Something Wrong. Try Again", Toast.LENGTH_SHORT).show();
                            Log.d("Request Add problem ", e.toString());
                        }
                    });
                } else {
                    Toast.makeText(AddRequest.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //-------select blood group---------------------------------------------------------------------
    public void checkBloodGroup() {
        int selectID = bloodGroup.getCheckedRadioButtonId();
        selectBloodGroup = findViewById(selectID);

        switch (selectID) {
            case R.id.a_plus_r:
                requestBloodGroup = "A+";
                break;
            case R.id.a_negative_r:
                requestBloodGroup = "A-";
                break;
            case R.id.b_plus_r:
                requestBloodGroup = "B+";
                break;
            case R.id.b_negative_r:
                requestBloodGroup = "B-";
                break;
            case R.id.o_plus_r:
                requestBloodGroup = "O+";
                break;
            case R.id.o_negative_r:
                requestBloodGroup = "O-";
                break;
            case R.id.ab_plus_r:
                requestBloodGroup = "AB+";
                break;
            case R.id.ab_negative_r:
                requestBloodGroup = "AB-";
                break;
            default:
                requestBloodGroup = "";
                break;
        }
    }
    //----------------------------------------------------------------------------------------------

    //-------------Check all data is fill-----------------------------------------------------------
    public boolean isEmptyCheck() {

        requestName = loginUser.getUserName();
        requestTelephone = loginUser.getUserTelephone();

        if (!requestBloodGroup.isEmpty() && !requestForWhom.equals(arraySets.bloodFor[0]) && !requestDistrict.equals(arraySets.requestDistrict[0])) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(AddRequest.this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddRequest.this, MainActivity.class);
        startActivity(intent);
    }
}