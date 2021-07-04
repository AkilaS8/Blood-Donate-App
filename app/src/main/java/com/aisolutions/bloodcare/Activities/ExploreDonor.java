package com.aisolutions.bloodcare.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aisolutions.bloodcare.Adapters.DonorAdapter;
import com.aisolutions.bloodcare.Details.ArraySets;
import com.aisolutions.bloodcare.Objects.DonorDetails;
import com.aisolutions.bloodcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class ExploreDonor extends AppCompatActivity {
    ImageView backMain;
    TextView search, alterText;
    RecyclerView exDonorList, exDonorListAlter;
    Spinner exBloodGroup, exProvince, exDistrict;
    RelativeLayout emptyDataView;

    FirebaseFirestore exploreDonors = FirebaseFirestore.getInstance();
    ArrayList<DonorDetails> arrayListDonor = new ArrayList<>();
    DonorAdapter adapter;

    //alternative
    ArrayList<DonorDetails> arrayListDonorAlter = new ArrayList<>();
    DonorAdapter adapterAlter;

    ArraySets arraySets = new ArraySets();

    //------------------------------------------------------------
    ArrayAdapter<String> arrayAdapter_Province;
    ArrayAdapter<String> arrayAdapter_District;
    ArrayAdapter<String> arrayAdapter_BloodGroup;
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

    ArrayList<String> arrayList_BloodGroup = new ArrayList<>();

    //-------------------------------------------------------------

    String TAG_bloodGroup = "";
    String TAG_province = "";
    String TAG_district = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_donor);

        search = findViewById(R.id.exDonor_search);
        exDonorList = findViewById(R.id.donorList_exDonor);
        exBloodGroup = findViewById(R.id.exDonor_bloodGroup);
        exProvince = findViewById(R.id.exDonor_province);
        exDistrict = findViewById(R.id.exDonor_district);

        //--Alter
        alterText = findViewById(R.id.exDonor_alter_txt);
        exDonorListAlter = findViewById(R.id.donorList_exDonor_alter);
        alterText.setVisibility(View.GONE);
        exDonorListAlter.setVisibility(View.GONE);

        //---empty view
        emptyDataView = findViewById(R.id.search_empty_donor);
        emptyDataView.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        exDonorList.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        exDonorListAlter.setLayoutManager(layoutManager1);

        //-----------Loading data into array Lists------------------------------------------------->
        //---data loading into province list
        for (int x = 0; x < arraySets.exDonor_province.length; x++) {
            arrayList_Province.add(arraySets.exDonor_province[x]);
        }

        //---data loading into district list
        //---(01)
        for (int x = 0; x < arraySets.exDonor_districts_Central.length; x++) {
            arrayList_District_Central.add(arraySets.exDonor_districts_Central[x]);
        }
        //---(02)
        for (int x = 0; x < arraySets.exDonor_districts_Eastern.length; x++) {
            arrayList_District_Eastern.add(arraySets.exDonor_districts_Eastern[x]);
        }
        //---(03)
        for (int x = 0; x < arraySets.exDonor_districts_North_Central.length; x++) {
            arrayList_District_North_Central.add(arraySets.exDonor_districts_North_Central[x]);
        }
        //---(04)
        for (int x = 0; x < arraySets.exDonor_districts_Northern.length; x++) {
            arrayList_District_Northern.add(arraySets.exDonor_districts_Northern[x]);
        }
        //---(05)
        for (int x = 0; x < arraySets.exDonor_districts_North_Western.length; x++) {
            arrayList_District_North_Western.add(arraySets.exDonor_districts_North_Western[x]);
        }
        //---(06)
        for (int x = 0; x < arraySets.exDonor_districts_Sabaragamuwa.length; x++) {
            arrayList_District_Sabaragamuwa.add(arraySets.exDonor_districts_Sabaragamuwa[x]);
        }
        //---(07)
        for (int x = 0; x < arraySets.exDonor_districts_Southern.length; x++) {
            arrayList_District_Southern.add(arraySets.exDonor_districts_Southern[x]);
        }
        //---(08)
        for (int x = 0; x < arraySets.exDonor_districts_Uva.length; x++) {
            arrayList_District_Uva.add(arraySets.exDonor_districts_Uva[x]);
        }
        //---(09)
        for (int x = 0; x < arraySets.exDonor_districts_Western.length; x++) {
            arrayList_District_Western.add(arraySets.exDonor_districts_Western[x]);
        }

        for (int x = 0; x < arraySets.exDonor_bloodGroup.length; x++) {
            arrayList_BloodGroup.add(arraySets.exDonor_bloodGroup[x]);
        }
        //----------------------------------------------------------------------------------------->

        //--------------Array Adapters--------------------------------------------------------------
        arrayAdapter_BloodGroup = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_BloodGroup);
        exBloodGroup.setAdapter(arrayAdapter_BloodGroup);

        arrayAdapter_Province = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_Province);
        exProvince.setAdapter(arrayAdapter_Province);

        exProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                exDistrict.setAdapter(arrayAdapter_District);
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
                Intent intent = new Intent(ExploreDonor.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //---------Searching------------------------------------------------------------------------
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TAG_bloodGroup = exBloodGroup.getSelectedItem().toString();
                TAG_province = exProvince.getSelectedItem().toString();


                if (!TAG_bloodGroup.isEmpty() && !TAG_province.equals("Select Province")) {
                    TAG_district = exDistrict.getSelectedItem().toString();
                    if (TAG_bloodGroup.equals("All")) {
                        if (TAG_district.equals("All")) {
                            searchData_4(TAG_province);
                        } else {
                            searchData_3(TAG_province, TAG_district);
                        }
                    } else {
                        if (TAG_district.equals("All")) {
                            searchData_2(TAG_bloodGroup, TAG_province);
                        } else {
                            searchData_1(TAG_bloodGroup, TAG_province, TAG_district);
                        }
                    }
                } else {
                    Toast.makeText(ExploreDonor.this, "Select all options!!!", Toast.LENGTH_SHORT).show();
                }

                //----------------test run----------------------------------------------------------
//                CollectionReference collectionReference = exploreDonors.collection("Donors");
//                collectionReference
//                        .whereIn("donorBloodGroup", Arrays.asList("A+", "O-"))
//                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                if (error != null) {
//                                    return;
//                                }
//                                arrayListDonor.clear();
//                                for (QueryDocumentSnapshot documentSnapshot : value) {
//                                    DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
//                                    arrayListDonor.add(data);
//                                }
//                                adapter = new DonorAdapter(getApplicationContext(), arrayListDonor);
//                                exDonorList.setAdapter(adapter);
//                                exDonorListAlter.setAdapter(adapter);
//                            }
//                        });
            }
        });
        //------------------------------------------------------------------------------------------
    }

    private void searchData_1(String bloodGroup, String province, String district) {
        arrayListDonor.clear();
        arrayListDonorAlter.clear();

        alterText.setVisibility(View.GONE);
        exDonorListAlter.setVisibility(View.GONE);

        emptyDataView.setVisibility(View.GONE);

        CollectionReference searching = exploreDonors.collection("Donors");

        searching
                .whereEqualTo("donorBloodGroup", bloodGroup)
                .whereEqualTo("donorProvince", province)
                .whereEqualTo("donorDistrict", district)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListDonor.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                            arrayListDonor.add(data);
                        }
                        adapter = new DonorAdapter(getApplicationContext(), arrayListDonor);
                        exDonorList.setAdapter(adapter);
                    }
                });
        if (!bloodGroup.equals("O-")) {
            alternativeSearch_1(bloodGroup, province, district);
        } else {
            Query query = searching
                    .whereEqualTo("donorBloodGroup", bloodGroup)
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        }
    }

    private void searchData_2(String bloodGroup, String province) {
        arrayListDonor.clear();
        arrayListDonorAlter.clear();

        alterText.setVisibility(View.GONE);
        exDonorListAlter.setVisibility(View.GONE);

        emptyDataView.setVisibility(View.GONE);

        CollectionReference searching = exploreDonors.collection("Donors");

        searching
                .whereEqualTo("donorBloodGroup", bloodGroup)
                .whereEqualTo("donorProvince", province)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListDonor.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                            arrayListDonor.add(data);
                        }
                        adapter = new DonorAdapter(getApplicationContext(), arrayListDonor);
                        exDonorList.setAdapter(adapter);
                    }
                });

        if (!bloodGroup.equals("O-")) {
            alternativeSearch_2(bloodGroup, province);
        } else {
            Query query = searching
                    .whereEqualTo("donorBloodGroup", bloodGroup)
                    .whereEqualTo("donorProvince", province);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        }

    }

    private void searchData_3(String province, String district) {
        arrayListDonor.clear();
        arrayListDonorAlter.clear();

        alterText.setVisibility(View.GONE);
        exDonorListAlter.setVisibility(View.GONE);

        emptyDataView.setVisibility(View.GONE);

        CollectionReference searching = exploreDonors.collection("Donors");

        searching
                .whereEqualTo("donorProvince", province)
                .whereEqualTo("donorDistrict", district)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListDonor.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                            arrayListDonor.add(data);
                        }
                        adapter = new DonorAdapter(getApplicationContext(), arrayListDonor);
                        exDonorList.setAdapter(adapter);
                    }
                });

        Query query = searching
                .whereEqualTo("donorProvince", province)
                .whereEqualTo("donorDistrict", district);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean b = task.getResult().isEmpty();
                    if (b) {
                        Log.d("Status------>", "empty");
                        alterText.setVisibility(View.GONE);
                        exDonorListAlter.setVisibility(View.GONE);
                        emptyDataView.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("Status------>", "no");
                    }
                } else {
                    Log.d("Error getting documents: ", String.valueOf(task.getException()));
                }
            }
        });
    }

    private void searchData_4(String province) {
        arrayListDonor.clear();
        arrayListDonorAlter.clear();

        alterText.setVisibility(View.GONE);
        exDonorListAlter.setVisibility(View.GONE);

        emptyDataView.setVisibility(View.GONE);

        CollectionReference searching = exploreDonors.collection("Donors");

        searching
                .whereEqualTo("donorProvince", province)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListDonor.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                            arrayListDonor.add(data);
                        }
                        adapter = new DonorAdapter(getApplicationContext(), arrayListDonor);
                        exDonorList.setAdapter(adapter);
                    }
                });
        Query query = searching
                .whereEqualTo("donorProvince", province);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean b = task.getResult().isEmpty();
                    if (b) {
                        Log.d("Status------>", "empty");
                        alterText.setVisibility(View.GONE);
                        exDonorListAlter.setVisibility(View.GONE);
                        emptyDataView.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("Status------>", "no");
                    }
                } else {
                    Log.d("Error getting documents: ", String.valueOf(task.getException()));
                }
            }
        });

    }

    private void alternativeSearch_1(String bloodGroup, String province, String district) {
        arrayListDonorAlter.clear();

        CollectionReference searchingAlter = exploreDonors.collection("Donors");

        if (bloodGroup.equals("O+") || bloodGroup.equals("A-") || bloodGroup.equals("B-")) {

            if (bloodGroup.equals("O+")) {
                alterText.setText("O+ blood group recipients are able to receive blood from O+ and O- blood group donors");
            } else if (bloodGroup.equals("A-")) {
                alterText.setText("A- blood group recipients are able to receive blood from A- and O- blood group donors");
            } else if (bloodGroup.equals("B-")) {
                alterText.setText("B- blood group recipients are able to receive blood from B- and O- blood group donors");
            }

            searchingAlter
                    .whereEqualTo("donorBloodGroup", "O-")
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereEqualTo("donorBloodGroup", "O-")
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("A+")) {

            alterText.setText("A+ blood group recipients are able to receive blood from A+, A-, O+ and O- blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-"))
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-"))
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("B+")) {

            alterText.setText("B+ blood group recipients are able to receive blood from B+, B-, O+ and O- blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "B-"))
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "B-"))
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("AB-")) {

            alterText.setText("AB- blood group recipients are able to receive blood from AB-, O-, A- and B- blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "A-", "B-"))
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "A-", "B-"))
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("AB+")) {

            alterText.setText("AB+ blood group recipients are able to receive blood from all other blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-", "A+", "B-", "B+", "AB-"))
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-", "A+", "B-", "B+", "AB-"))
                    .whereEqualTo("donorProvince", province)
                    .whereEqualTo("donorDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        }
    }


    private void alternativeSearch_2(String bloodGroup, String province) {
        arrayListDonorAlter.clear();

        CollectionReference searchingAlter = exploreDonors.collection("Donors");

        if (bloodGroup.equals("O+") || bloodGroup.equals("A-") || bloodGroup.equals("B-")) {

            if (bloodGroup.equals("O+")) {
                alterText.setText("O+ blood group recipients are able to receive blood from O+ and O- blood group donors");
            } else if (bloodGroup.equals("A-")) {
                alterText.setText("A- blood group recipients are able to receive blood from A- and O- blood group donors");
            } else if (bloodGroup.equals("B-")) {
                alterText.setText("B- blood group recipients are able to receive blood from B- and O- blood group donors");
            }

            searchingAlter
                    .whereEqualTo("donorBloodGroup", "O-")
                    .whereEqualTo("donorProvince", province)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereEqualTo("donorBloodGroup", "O-")
                    .whereEqualTo("donorProvince", province);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("A+")) {

            alterText.setText("A+ blood group recipients are able to receive blood from A+, A-, O+ and O- blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-"))
                    .whereEqualTo("donorProvince", province)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-"))
                    .whereEqualTo("donorProvince", province);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("B+")) {

            alterText.setText("B+ blood group recipients are able to receive blood from B+, B-, O+ and O- blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "B-"))
                    .whereEqualTo("donorProvince", province)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "B-"))
                    .whereEqualTo("donorProvince", province);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("AB-")) {

            alterText.setText("AB- blood group recipients are able to receive blood from AB-, O-, A- and B- blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "A-", "B-"))
                    .whereEqualTo("donorProvince", province)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "A-", "B-"))
                    .whereEqualTo("donorProvince", province);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("AB+")) {

            alterText.setText("AB+ blood group recipients are able to receive blood from all other blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-", "A+", "B-", "B+", "AB-"))
                    .whereEqualTo("donorProvince", province)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListDonorAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListDonorAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListDonorAlter);
                            exDonorListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-", "A+", "B-", "B+", "AB-"))
                    .whereEqualTo("donorProvince", province);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exDonorListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exDonorListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intents = new Intent(ExploreDonor.this, MainActivity.class);
        startActivity(intents);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intents = new Intent(ExploreDonor.this, MainActivity.class);
        startActivity(intents);
        finish();
    }
}