package com.aisolutions.bloodcare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aisolutions.bloodcare.Objects.DonorDetails;
import com.aisolutions.bloodcare.Objects.UserDetails;
import com.aisolutions.bloodcare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BecomeDonor extends AppCompatActivity implements LocationListener {
    ImageView calender, backMain;
    TextView ageShow, join;
    EditText bodShow, telephone, name, weight, healthConditions;
    RadioButton selectGender, selectBloodGroup;
    RadioGroup gender, bloodGroup;

    Integer userAge = null;

    UserDetails loginUser;

    LocationManager locationManager;

    //------------------------------->
    private String donorName = "";
    private String donorTelephone = "";
    private String donorProvince = "";
    private String donorDistrict = "";
    private String donorCity = "";
    private String donorAddress = "";
    private String donorAge = "";
    private String donorWeight = "";
    private String donorGender = "";
    private String donorBloodGroup = "";
    private String donorHealthConditions = "";
    private Double donorLat;
    private Double donorLan;
    //------------------------------>

    FirebaseFirestore saveDonate = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_donor);

        //------------------Fill login user data----------------------------------------------------
        SharedPreferences loginUserPreferences = getSharedPreferences("loginUser", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loginUserPreferences.getString("LoginUser", "");
        loginUser = gson.fromJson(json, UserDetails.class);

        Log.d("userName-->", loginUser.getUserName());
        //------------------------------------------------------------------------------------------

        //------------Get the Current Location------------------------------------------------------
        grantPermission();
        checkLocationEnableOrNot();
        getLocation();
        //------------------------------------------------------------------------------------------

        name = findViewById(R.id.donorName);
        telephone = findViewById(R.id.donorTelephone);
        weight = findViewById(R.id.weight);
        healthConditions = findViewById(R.id.condition_txt);

        join = findViewById(R.id.becomeDonor);

        gender = findViewById(R.id.gender);
        bloodGroup = findViewById(R.id.bloodGroup);

        //fill data into text fields
        name.setText(loginUser.getUserName());
        telephone.setText(loginUser.getUserTelephone());

        //----------Intent to Main Activity--------------------------
        backMain = findViewById(R.id.backmain);
        backMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BecomeDonor.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        calender = findViewById(R.id.bodCalender);
        ageShow = findViewById(R.id.ageShowTxt);
        bodShow = findViewById(R.id.birthday);

        //---Image Calender --> select birthday
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int sYear = calendar.get(Calendar.YEAR);
                int sMonth = calendar.get(Calendar.MONTH);
                int sDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog pickerDialog = new DatePickerDialog(v.getContext(), datePickerListener, sYear, sMonth, sDay);
                pickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                pickerDialog.show();
            }
        });

        //----------------Save Data into Cloud Firestore--------------------------------------------
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGender();
                checkBloodGroup();

                if (validateData()) {

                    DocumentReference donorReference = saveDonate.document("Donors/" + donorTelephone);
                    DonorDetails donorDetails = new DonorDetails(donorName, donorTelephone, donorProvince, donorDistrict, donorCity, donorAddress, donorAge, donorWeight, donorGender, donorBloodGroup, donorHealthConditions, donorLat, donorLan);

                    donorReference.set(donorDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(BecomeDonor.this, "Congratulations !!!", Toast.LENGTH_SHORT).show();

                            //automatically close the Activity and go to LOGIN
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(BecomeDonor.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BecomeDonor.this, "Something Wrong. Try Again", Toast.LENGTH_SHORT).show();
                            Log.d("Save Donor Wrong", e.toString());
                        }
                    });
                } else {
                    Snackbar.make(v, "Set Valued Data", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        //------------------------------------------------------------------------------------------
    }

    //------select gender---------------------------------------------------------------------------
    public void checkGender() {
        int selectID = gender.getCheckedRadioButtonId();
        selectGender = findViewById(selectID);

        switch (selectID) {
            case R.id.genderMale:
                donorGender = "Male";
                break;
            case R.id.genderFemale:
                donorGender = "Female";
                break;
            default:
                donorGender = "";
                break;
        }
    }

    //-------select blood group---------------------------------------------------------------------
    public void checkBloodGroup() {
        int selectID = bloodGroup.getCheckedRadioButtonId();
        selectBloodGroup = findViewById(selectID);

        switch (selectID) {
            case R.id.a_plus:
                donorBloodGroup = "A+";
                break;
            case R.id.a_negative:
                donorBloodGroup = "A-";
                break;
            case R.id.b_plus:
                donorBloodGroup = "B+";
                break;
            case R.id.b_negative:
                donorBloodGroup = "B-";
                break;
            case R.id.o_plus:
                donorBloodGroup = "O+";
                break;
            case R.id.o_negative:
                donorBloodGroup = "O-";
                break;
            case R.id.ab_plus:
                donorBloodGroup = "AB+";
                break;
            case R.id.ab_negative:
                donorBloodGroup = "AB-";
                break;
            default:
                donorBloodGroup = "";
                break;
        }
    }
    //----------------------------------------------------------------------------------------------

    //--------------------validations check---------------------------------------------------------
    public boolean  validateData() {
        boolean resultA = false;
        boolean resultW = false;
        boolean resultG = false;
        boolean resultB = false;

        donorHealthConditions = healthConditions.getText().toString();

        donorName = loginUser.getUserName();
        donorTelephone = loginUser.getUserTelephone();
        donorProvince = loginUser.getUserProvince();
        donorDistrict = loginUser.getUserDistrict();
        donorCity = loginUser.getUserCity();
        donorAddress = loginUser.getUserAddress();

        //Age
        if (donorAge.isEmpty()) {
            Toast.makeText(this, "Set your Birthday", Toast.LENGTH_SHORT).show();
        } else {
            if (Integer.parseInt(donorAge) < 18 || Integer.parseInt(donorAge) > 50) {
                Toast.makeText(this, "Your Age is not allowed to donate blood", Toast.LENGTH_SHORT).show();
            } else {
                resultA = true;
            }
        }

        //Weight
        donorWeight = weight.getText().toString();
        if (donorWeight.isEmpty()) {
            Toast.makeText(BecomeDonor.this, "Set your Weight", Toast.LENGTH_SHORT).show();
        } else {
            if (Integer.parseInt(donorWeight) >= 50) {
                resultW = true;
            } else {
                Toast.makeText(BecomeDonor.this, "Your Weight is not allowed to donate blood", Toast.LENGTH_SHORT).show();
            }
        }

        //Gender
        if (donorGender.isEmpty()) {
            Toast.makeText(BecomeDonor.this, "Select your Gender", Toast.LENGTH_SHORT).show();
        } else {
            resultG = true;
        }

        //BloodGroup
        if (donorBloodGroup.isEmpty()) {
            Toast.makeText(BecomeDonor.this, "Select your Blood Group", Toast.LENGTH_SHORT).show();
        } else {
            resultB = true;
        }

        if (resultA && resultB && resultG && resultW) {
            return true;
        } else {
            return false;
        }

    }

    //-----Age Calculation--------------------------------------------------------------------------
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String format = new SimpleDateFormat("dd MMM YYYY").format(calendar.getTime());
            bodShow.setText(format);
            userAge = calculateAge(calendar.getTimeInMillis());
            ageShow.setText("You are " + userAge + " years old.");
            donorAge = userAge.toString();

        }
    };

    private int calculateAge(long timeInMillis) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(timeInMillis);

        Calendar today = Calendar.getInstance();

        int Age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            Age--;
        }
        if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            Age--;
        }
        return Age;
    }
    //----------------------------------------------------------------------------------------------

    //---------------------------- Get location ----------------------------------------------------
    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void checkLocationEnableOrNot() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnable = false;
        boolean networkEnable = false;

        try {
            gpsEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            networkEnable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gpsEnable && !networkEnable) {
            new AlertDialog.Builder(BecomeDonor.this).setTitle("Enable GPS Service").setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", null).show();
        }
    }

    private void grantPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {

            donorLat = location.getLatitude();
            donorLan = location.getLongitude();

            Log.d("lat--------------", String.valueOf(location.getLatitude()));
            Log.d("lng--------------", String.valueOf(location.getLongitude()));

        } catch (Exception e) {
            Log.d("Location Error -->", e.toString());
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(BecomeDonor.this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BecomeDonor.this, MainActivity.class);
        startActivity(intent);
    }
}