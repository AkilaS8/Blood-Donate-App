package com.aisolutions.bloodcare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aisolutions.bloodcare.Details.ArraySets;
import com.aisolutions.bloodcare.Objects.UserDetails;
import com.aisolutions.bloodcare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Registration extends AppCompatActivity {

    private static final String TAG = "Registration Activity";

    ImageView backlogin;
    EditText uName, uTelephone, uAddress, uPassword, uComPassword;
    Spinner uProvince, uDistrict, uCity;
    TextView uRegistration;

    ArraySets arraySets = new ArraySets();

    //-------------------------------------------------------------
    ArrayAdapter<String> arrayAdapter_Province;
    ArrayAdapter<String> arrayAdapter_District;
    ArrayAdapter<String> arrayAdapter_City;
    //-------

    ArrayList<String> arrayList_Province = new ArrayList<>();
    //-------

    ArrayList<String> arrayList_District_Central = new ArrayList<>();
    ArrayList<String> arrayList_District_Eastern = new ArrayList<>();
    ArrayList<String> arrayList_District_North_Central = new ArrayList<>();
    ArrayList<String> arrayList_District_Northern = new ArrayList<>();
    ArrayList<String> arrayList_District_North_Western = new ArrayList<>();
    ArrayList<String> arrayList_District_Sabaragamuwa = new ArrayList<>();
    ArrayList<String> arrayList_District_Southern = new ArrayList<>();
    ArrayList<String> arrayList_District_Uva = new ArrayList<>();
    ArrayList<String> arrayList_District_Western = new ArrayList<>();
    //-------

    ArrayList<String> arrayList_City_Kandy = new ArrayList<>();
    ArrayList<String> arrayList_City_Matale = new ArrayList<>();
    ArrayList<String> arrayList_City_Nuwara_Eliya = new ArrayList<>();
    ArrayList<String> arrayList_City_Ampara = new ArrayList<>();
    ArrayList<String> arrayList_City_Batticaloa = new ArrayList<>();
    ArrayList<String> arrayList_City_Trincomalee = new ArrayList<>();
    ArrayList<String> arrayList_City_Anuradhapura = new ArrayList<>();
    ArrayList<String> arrayList_City_Polonnaruwa = new ArrayList<>();
    ArrayList<String> arrayList_City_Jaffna = new ArrayList<>();
    ArrayList<String> arrayList_City_Kilinochchi = new ArrayList<>();
    ArrayList<String> arrayList_City_Mannar = new ArrayList<>();
    ArrayList<String> arrayList_City_Mulativu = new ArrayList<>();
    ArrayList<String> arrayList_City_Vavuniya = new ArrayList<>();
    ArrayList<String> arrayList_City_Kurunegala = new ArrayList<>();
    ArrayList<String> arrayList_City_Puttalam = new ArrayList<>();
    ArrayList<String> arrayList_City_Kegalle = new ArrayList<>();
    ArrayList<String> arrayList_City_Ratnapura = new ArrayList<>();
    ArrayList<String> arrayList_City_Galle = new ArrayList<>();
    ArrayList<String> arrayList_City_Hambantota = new ArrayList<>();
    ArrayList<String> arrayList_City_Matara = new ArrayList<>();
    ArrayList<String> arrayList_City_Badulla = new ArrayList<>();
    ArrayList<String> arrayList_City_Monaragala = new ArrayList<>();
    ArrayList<String> arrayList_City_Colombo = new ArrayList<>();
    ArrayList<String> arrayList_City_Gampaha = new ArrayList<>();
    ArrayList<String> arrayList_City_Kalutara = new ArrayList<>();

    //-------------------------------------------------------------

    //------------------------------->
    public String userName = "";
    public String userTelephone = "";
    public String userProvince = "";
    public String userDistrict = "";
    public String userCity = "";
    public String userAddress = "";
    public String userPassword = "";
    public String userComPassword = "";
    //------------------------------>

    FirebaseFirestore registrationUser = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        View view = getWindow().getDecorView();
        try {
            view.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        uName = findViewById(R.id.userName);
        uTelephone = findViewById(R.id.userTelephone);
        uProvince = findViewById(R.id.userProvince);
        uDistrict = findViewById(R.id.userDistrict);
        uCity = findViewById(R.id.userCity);
        uAddress = findViewById(R.id.userAddress);
        uPassword = findViewById(R.id.userPassword);
        uComPassword = findViewById(R.id.userComPassword);
        uRegistration = findViewById(R.id.userRegistration);

        //-----------Loading data into array Lists------------------------------------------------->
        //---data loading into province list
        for (int x = 0; x < arraySets.province.length; x++) {
            arrayList_Province.add(arraySets.province[x]);
        }

        //---data loading into district list
        //---(01)
        for (int x = 0; x < arraySets.districts_Central.length; x++) {
            arrayList_District_Central.add(arraySets.districts_Central[x]);
        }
        //---(02)
        for (int x = 0; x < arraySets.districts_Eastern.length; x++) {
            arrayList_District_Eastern.add(arraySets.districts_Eastern[x]);
        }
        //---(03)
        for (int x = 0; x < arraySets.districts_North_Central.length; x++) {
            arrayList_District_North_Central.add(arraySets.districts_North_Central[x]);
        }
        //---(04)
        for (int x = 0; x < arraySets.districts_Northern.length; x++) {
            arrayList_District_Northern.add(arraySets.districts_Northern[x]);
        }
        //---(05)
        for (int x = 0; x < arraySets.districts_North_Western.length; x++) {
            arrayList_District_North_Western.add(arraySets.districts_North_Western[x]);
        }
        //---(06)
        for (int x = 0; x < arraySets.districts_Sabaragamuwa.length; x++) {
            arrayList_District_Sabaragamuwa.add(arraySets.districts_Sabaragamuwa[x]);
        }
        //---(07)
        for (int x = 0; x < arraySets.districts_Southern.length; x++) {
            arrayList_District_Southern.add(arraySets.districts_Southern[x]);
        }
        //---(08)
        for (int x = 0; x < arraySets.districts_Uva.length; x++) {
            arrayList_District_Uva.add(arraySets.districts_Uva[x]);
        }
        //---(09)
        for (int x = 0; x < arraySets.districts_Western.length; x++) {
            arrayList_District_Western.add(arraySets.districts_Western[x]);
        }


        //---data loading into city list
        //---(01)
        for (int x = 0; x < arraySets.city_Kandy.length; x++) {
            arrayList_City_Kandy.add(arraySets.city_Kandy[x]);
        }
        //---(02)
        for (int x = 0; x < arraySets.city_Matale.length; x++) {
            arrayList_City_Matale.add(arraySets.city_Matale[x]);
        }
        //---(03)
        for (int x = 0; x < arraySets.city_Nuwara_Eliya.length; x++) {
            arrayList_City_Nuwara_Eliya.add(arraySets.city_Nuwara_Eliya[x]);
        }
        //---(04)
        for (int x = 0; x < arraySets.city_Ampara.length; x++) {
            arrayList_City_Ampara.add(arraySets.city_Ampara[x]);
        }
        //---(05)
        for (int x = 0; x < arraySets.city_Batticaloa.length; x++) {
            arrayList_City_Batticaloa.add(arraySets.city_Batticaloa[x]);
        }
        //---(06)
        for (int x = 0; x < arraySets.city_Trincomalee.length; x++) {
            arrayList_City_Trincomalee.add(arraySets.city_Trincomalee[x]);
        }
        //---(07)
        for (int x = 0; x < arraySets.city_Anuradhapura.length; x++) {
            arrayList_City_Anuradhapura.add(arraySets.city_Anuradhapura[x]);
        }
        //---(08)
        for (int x = 0; x < arraySets.city_Polonnaruwa.length; x++) {
            arrayList_City_Polonnaruwa.add(arraySets.city_Polonnaruwa[x]);
        }
        //---(09)
        for (int x = 0; x < arraySets.city_Jaffna.length; x++) {
            arrayList_City_Jaffna.add(arraySets.city_Jaffna[x]);
        }
        //---(10)
        for (int x = 0; x < arraySets.city_Kilinochchi.length; x++) {
            arrayList_City_Kilinochchi.add(arraySets.city_Kilinochchi[x]);
        }
        //---(11)
        for (int x = 0; x < arraySets.city_Mannar.length; x++) {
            arrayList_City_Mannar.add(arraySets.city_Mannar[x]);
        }
        //---(12)
        for (int x = 0; x < arraySets.city_Mulativu.length; x++) {
            arrayList_City_Mulativu.add(arraySets.city_Mulativu[x]);
        }
        //---(13)
        for (int x = 0; x < arraySets.city_Vavuniya.length; x++) {
            arrayList_City_Vavuniya.add(arraySets.city_Vavuniya[x]);
        }
        //---(14)
        for (int x = 0; x < arraySets.city_Kurunegala.length; x++) {
            arrayList_City_Kurunegala.add(arraySets.city_Kurunegala[x]);
        }
        //---(15)
        for (int x = 0; x < arraySets.city_Puttalam.length; x++) {
            arrayList_City_Puttalam.add(arraySets.city_Puttalam[x]);
        }
        //---(16)
        for (int x = 0; x < arraySets.city_Kegalle.length; x++) {
            arrayList_City_Kegalle.add(arraySets.city_Kegalle[x]);
        }
        //---(17)
        for (int x = 0; x < arraySets.city_Ratnapura.length; x++) {
            arrayList_City_Ratnapura.add(arraySets.city_Ratnapura[x]);
        }
        //---(18)
        for (int x = 0; x < arraySets.city_Galle.length; x++) {
            arrayList_City_Galle.add(arraySets.city_Galle[x]);
        }
        //---(19)
        for (int x = 0; x < arraySets.city_Hambantota.length; x++) {
            arrayList_City_Hambantota.add(arraySets.city_Hambantota[x]);
        }
        //---(20)
        for (int x = 0; x < arraySets.city_Matara.length; x++) {
            arrayList_City_Matara.add(arraySets.city_Matara[x]);
        }
        //---(21)
        for (int x = 0; x < arraySets.city_Badulla.length; x++) {
            arrayList_City_Badulla.add(arraySets.city_Badulla[x]);
        }
        //---(22)
        for (int x = 0; x < arraySets.city_Monaragala.length; x++) {
            arrayList_City_Monaragala.add(arraySets.city_Monaragala[x]);
        }
        //---(23)
        for (int x = 0; x < arraySets.city_Colombo.length; x++) {
            arrayList_City_Colombo.add(arraySets.city_Colombo[x]);
        }
        //---(24)
        for (int x = 0; x < arraySets.city_Gampaha.length; x++) {
            arrayList_City_Gampaha.add(arraySets.city_Gampaha[x]);
        }
        //---(25)
        for (int x = 0; x < arraySets.city_Kalutara.length; x++) {
            arrayList_City_Kalutara.add(arraySets.city_Kalutara[x]);
        }


        //----------------------------------------------------------------------------------------->

        //--------------Array Adapters--------------------------------------------------------------
        arrayAdapter_Province = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_Province);
        uProvince.setAdapter(arrayAdapter_Province);


        uProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District_Central);
                        break;
                    case 2:
                        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District_Eastern);
                        break;
                    case 3:
                        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District_North_Central);
                        break;
                    case 4:
                        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District_Northern);
                        break;
                    case 5:
                        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District_North_Western);
                        break;
                    case 6:
                        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District_Sabaragamuwa);
                        break;
                    case 7:
                        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District_Southern);
                        break;
                    case 8:
                        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District_Uva);
                        break;
                    case 9:
                        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District_Western);
                        break;
                }

                uDistrict.setAdapter(arrayAdapter_District);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((uProvince.getSelectedItem().toString()).equals("Central")) {
                    switch (position) {
                        case 1:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Kandy);
                            break;
                        case 2:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Matale);
                            break;
                        case 3:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Nuwara_Eliya);
                            break;
                    }
                } else if ((uProvince.getSelectedItem().toString()).equals("Eastern")) {
                    switch (position) {
                        case 1:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Ampara);
                            break;
                        case 2:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Batticaloa);
                            break;
                        case 3:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Trincomalee);
                            break;
                    }
                } else if ((uProvince.getSelectedItem().toString()).equals("North Central")) {
                    switch (position) {
                        case 1:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Anuradhapura);
                            break;
                        case 2:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Polonnaruwa);
                            break;
                    }
                } else if ((uProvince.getSelectedItem().toString()).equals("Northern")) {
                    switch (position) {
                        case 1:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Jaffna);
                            break;
                        case 2:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Kilinochchi);
                            break;
                        case 3:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Mannar);
                            break;
                        case 4:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Mulativu);
                            break;
                        case 5:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Vavuniya);
                            break;
                    }
                } else if ((uProvince.getSelectedItem().toString()).equals("North Western")) {
                    switch (position) {
                        case 1:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Kurunegala);
                            break;
                        case 2:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Puttalam);
                            break;
                    }
                } else if ((uProvince.getSelectedItem().toString()).equals("Sabaragamuwa")) {
                    switch (position) {
                        case 1:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Kegalle);
                            break;
                        case 2:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Ratnapura);
                            break;
                    }
                } else if ((uProvince.getSelectedItem().toString()).equals("Southern")) {
                    switch (position) {
                        case 1:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Galle);
                            break;
                        case 2:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Hambantota);
                            break;
                        case 3:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Matara);
                            break;
                    }
                } else if ((uProvince.getSelectedItem().toString()).equals("Uva")) {
                    switch (position) {
                        case 1:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Badulla);
                            break;
                        case 2:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Monaragala);
                            break;
                    }
                } else if ((uProvince.getSelectedItem().toString()).equals("Western")) {
                    switch (position) {
                        case 1:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Colombo);
                            break;
                        case 2:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Gampaha);
                            break;
                        case 3:
                            arrayAdapter_City = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_City_Kalutara);
                            break;
                    }
                }
                uCity.setAdapter(arrayAdapter_City);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //------------------------------------------------------------------------------------------

        //----------Intent to Login Activity--------------------------
        backlogin = findViewById(R.id.backlogin);
        backlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        //----------------------------------------------------------------

        //------Register the User-----------------------------------------
        uRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistration();
            }
        });
    }

    public void userRegistration() {
        if (!checkEmptyField()) {
            Toast.makeText(this, "Fill all the Fields", Toast.LENGTH_SHORT).show();
        } else {
            if (checkPassword()) {
                uPassword.setText("");
                uComPassword.setText("");
                uPassword.requestFocus();
                Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            } else {
                //Add data into Cloud firebase
                registration();
            }
        }
    }

    private void registration() {

        DocumentReference registrationRef = registrationUser.document("Users/" + userTelephone);
        UserDetails userDetails = new UserDetails(userName, userTelephone, userProvince, userDistrict, userCity, userAddress, userPassword);

        registrationRef.set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Registration.this, "Your Registration is Successful", Toast.LENGTH_SHORT).show();

                //automatically close the Activity and go to LOGIN
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Registration.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        }).addOnFailureListener(new OnFailureListener() {   //If  any fail in the process
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration.this, "Your Registration is Unsuccessful :  " + e.toString() + "", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }

    private boolean checkEmptyField() {
        userName = uName.getText().toString();
        userTelephone = uTelephone.getText().toString();
        userAddress = uAddress.getText().toString();
        userPassword = uPassword.getText().toString();
        userComPassword = uComPassword.getText().toString();
        userProvince = uProvince.getSelectedItem().toString();
        userDistrict = uDistrict.getSelectedItem().toString();
        userCity = uCity.getSelectedItem().toString();


        if (!userName.isEmpty() && !userTelephone.isEmpty() && !userAddress.isEmpty() && !userPassword.isEmpty() && !userComPassword.isEmpty() || !userProvince.equals("Your Province") || !userDistrict.equals("Your District") || !userCity.equals("Your City")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkPassword() {
        if (!userPassword.equals(userComPassword)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}