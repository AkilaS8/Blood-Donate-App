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
import com.aisolutions.bloodcare.Adapters.RequestAdapter;
import com.aisolutions.bloodcare.Details.ArraySets;
import com.aisolutions.bloodcare.Objects.DonorDetails;
import com.aisolutions.bloodcare.Objects.RequestBlood;
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

public class ExploreRequest extends AppCompatActivity {
    ImageView backMain;
    TextView search, alterText;
    RecyclerView exRequestList, exRequestListAlter;
    Spinner exBloodGroup, exDistrict;
    RelativeLayout emptyDataView;

    FirebaseFirestore exploreRequest = FirebaseFirestore.getInstance();
    ArrayList<RequestBlood> arrayListRequest = new ArrayList<>();
    RequestAdapter adapter;

    ArrayList<RequestBlood> arrayListRequestAlter = new ArrayList<>();
    RequestAdapter adapterAlter;

    ArraySets arraySets = new ArraySets();

    //------------------------------------------------------------
    ArrayAdapter<String> arrayAdapter_District;
    ArrayAdapter<String> arrayAdapter_BloodGroup;
    //-------

    ArrayList<String> arrayList_District = new ArrayList<>();
    ArrayList<String> arrayList_BloodGroup = new ArrayList<>();

    //-------------------------------------------------------------

    String TAG_bloodGroup = "";
    String TAG_district = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_request);

        //----------Intent to Main Activity--------------------------
        backMain = findViewById(R.id.backmain);
        backMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExploreRequest.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //----------------------------------------------------------------

        search = findViewById(R.id.exRequest_search);
        exRequestList = findViewById(R.id.requestList_exRequest);
        exBloodGroup = findViewById(R.id.exRequest_bloodGroup);
        exDistrict = findViewById(R.id.exRequest_district);

        //--Alter
        alterText = findViewById(R.id.exRequest_alter_txt);
        exRequestListAlter = findViewById(R.id.requestList_exRequest_alter);
        alterText.setVisibility(View.GONE);
        exRequestListAlter.setVisibility(View.GONE);

        //---empty view
        emptyDataView = findViewById(R.id.search_empty_request);
        emptyDataView.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        exRequestList.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        exRequestListAlter.setLayoutManager(layoutManager1);

        //-----------Loading data into array Lists------------------------------------------------->
        for (int x = 0; x < arraySets.exDonor_bloodGroup.length; x++) {
            arrayList_BloodGroup.add(arraySets.exDonor_bloodGroup[x]);
        }
        for (int x = 0; x < arraySets.requestDistrict.length; x++) {
            arrayList_District.add(arraySets.requestDistrict[x]);
        }
        //--------------Array Adapters--------------------------------------------------------------
        arrayAdapter_BloodGroup = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_BloodGroup);
        exBloodGroup.setAdapter(arrayAdapter_BloodGroup);

        arrayAdapter_District = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_District);
        exDistrict.setAdapter(arrayAdapter_District);

        //---------Searching------------------------------------------------------------------------
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TAG_bloodGroup = exBloodGroup.getSelectedItem().toString();
                TAG_district = exDistrict.getSelectedItem().toString();

                if (!TAG_bloodGroup.isEmpty() && !TAG_district.equals("Select District")) {
                    arrayListRequest.clear();
                    arrayListRequestAlter.clear();

                    if (TAG_bloodGroup.equals("All")) {
                        searchData_1(TAG_district);
                    } else {
                        searchData_2(TAG_bloodGroup, TAG_district);
                    }
                } else {
                    Toast.makeText(ExploreRequest.this, "Select all options!!!", Toast.LENGTH_SHORT).show();
                }


                //-------------test search---------------------------------------------->
//                CollectionReference collectionReference = exploreRequest.collection("Requests");
//                collectionReference
//                        .orderBy("requestDate", Query.Direction.DESCENDING)
//                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable QuerySnapshot value, FirebaseFirestoreException error) {
//                                if (error != null) {
//                                    return;
//                                }
//                                arrayListRequest.clear();
//                                for (QueryDocumentSnapshot documentSnapshot : value) {
//                                    RequestBlood data = documentSnapshot.toObject(RequestBlood.class);
//                                    arrayListRequest.add(data);
//                                }
//                                adapter = new RequestAdapter(getApplicationContext(), arrayListRequest);
//                                exRequestList.setAdapter(adapter);
//                                exRequestListAlter.setAdapter(adapter);
//                            }
//                        });
            }
        });
    }

    private void searchData_1(String district) {
        arrayListRequest.clear();
        arrayListRequestAlter.clear();


        alterText.setVisibility(View.GONE);
        exRequestListAlter.setVisibility(View.GONE);

        emptyDataView.setVisibility(View.GONE);

        CollectionReference searching = exploreRequest.collection("Requests");
        searching
                .whereEqualTo("requestDistrict", district)
                .orderBy("requestDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListRequest.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            RequestBlood data = documentSnapshot.toObject(RequestBlood.class);
                            arrayListRequest.add(data);
                        }
                        adapter = new RequestAdapter(getApplicationContext(), arrayListRequest);
                        exRequestList.setAdapter(adapter);
                        exRequestListAlter.setAdapter(adapter);
                    }
                });

        Query query = searching
                .whereEqualTo("requestDistrict", district);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean b = task.getResult().isEmpty();
                    if (b) {
                        Log.d("Status------>", "empty");
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

    private void searchData_2(String bloodGroup, String district) {
        arrayListRequest.clear();
        arrayListRequestAlter.clear();


        alterText.setVisibility(View.GONE);
        exRequestListAlter.setVisibility(View.GONE);

        emptyDataView.setVisibility(View.GONE);

        CollectionReference searching = exploreRequest.collection("Requests");
        searching
                .whereEqualTo("requestDistrict", district)
                .whereEqualTo("requestBloodGroup", bloodGroup)
                .orderBy("requestDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListRequest.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            RequestBlood data = documentSnapshot.toObject(RequestBlood.class);
                            arrayListRequest.add(data);
                        }
                        adapter = new RequestAdapter(getApplicationContext(), arrayListRequest);
                        exRequestList.setAdapter(adapter);
                        exRequestListAlter.setAdapter(adapter);
                    }
                });

        if (!bloodGroup.equals("O-")) {
            alternativeSearch_1(bloodGroup, district);
        } else {
            Query query = searching
                    .whereEqualTo("requestDistrict", district)
                    .whereEqualTo("requestBloodGroup", bloodGroup);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
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

    private void alternativeSearch_1(String bloodGroup, String district) {
        arrayListRequestAlter.clear();

        CollectionReference searchingAlter = exploreRequest.collection("Requests");
        if (bloodGroup.equals("O+") || bloodGroup.equals("A-") || bloodGroup.equals("B-")) {

            if (bloodGroup.equals("O+")) {
                alterText.setText("O+ blood group recipients are able to receive blood from O+ and O- blood group donors");
            } else if (bloodGroup.equals("A-")) {
                alterText.setText("A- blood group recipients are able to receive blood from A- and O- blood group donors");
            } else if (bloodGroup.equals("B-")) {
                alterText.setText("B- blood group recipients are able to receive blood from B- and O- blood group donors");
            }

            searchingAlter
                    .whereEqualTo("requestBloodGroup", "O-")
                    .whereEqualTo("requestDistrict", district)
                    .orderBy("requestDate", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                RequestBlood data = documentSnapshot.toObject(RequestBlood.class);
                                arrayListRequestAlter.add(data);
                            }
                            adapterAlter = new RequestAdapter(getApplicationContext(), arrayListRequestAlter);
                            exRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereEqualTo("requestBloodGroup", "O-")
                    .whereEqualTo("requestDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exRequestListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exRequestListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("A+")) {
            alterText.setText("A+ blood group recipients are able to receive blood from A+, A-, O+ and O- blood group donors");

            searchingAlter
                    .whereIn("requestBloodGroup", Arrays.asList("O-", "O+", "A-"))
                    .whereEqualTo("requestDistrict", district)
                    .orderBy("requestDate", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                RequestBlood data = documentSnapshot.toObject(RequestBlood.class);
                                arrayListRequestAlter.add(data);
                            }
                            adapterAlter = new RequestAdapter(getApplicationContext(), arrayListRequestAlter);
                            exRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("requestBloodGroup", Arrays.asList("O-", "O+", "A-"))
                    .whereEqualTo("requestDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exRequestListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exRequestListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("B+")) {

            alterText.setText("B+ blood group recipients are able to receive blood from B+, B-, O+ and O- blood group donors");

            searchingAlter
                    .whereIn("requestBloodGroup", Arrays.asList("O-", "O+", "B-"))
                    .whereEqualTo("requestDistrict", district)
                    .orderBy("requestDate", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                RequestBlood data = documentSnapshot.toObject(RequestBlood.class);
                                arrayListRequestAlter.add(data);
                            }
                            adapterAlter = new RequestAdapter(getApplicationContext(), arrayListRequestAlter);
                            exRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("requestBloodGroup", Arrays.asList("O-", "O+", "B-"))
                    .whereEqualTo("requestDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exRequestListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exRequestListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("AB-")) {
            alterText.setText("AB- blood group recipients are able to receive blood from AB-, O-, A- and B- blood group donors");

            searchingAlter
                    .whereIn("requestBloodGroup", Arrays.asList("O-", "A-", "B-"))
                    .whereEqualTo("requestDistrict", district)
                    .orderBy("requestDate", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                RequestBlood data = documentSnapshot.toObject(RequestBlood.class);
                                arrayListRequestAlter.add(data);
                            }
                            adapterAlter = new RequestAdapter(getApplicationContext(), arrayListRequestAlter);
                            exRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("requestBloodGroup", Arrays.asList("O-", "A-", "B-"))
                    .whereEqualTo("requestDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exRequestListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exRequestListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (bloodGroup.equals("AB+")) {
            alterText.setText("AB+ blood group recipients are able to receive blood from all other blood group donors");

            searchingAlter
                    .whereIn("requestBloodGroup", Arrays.asList("O-", "O+", "A-", "A+", "B-", "B+", "AB-"))
                    .whereEqualTo("requestDistrict", district)
                    .orderBy("requestDate", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                RequestBlood data = documentSnapshot.toObject(RequestBlood.class);
                                arrayListRequestAlter.add(data);
                            }
                            adapterAlter = new RequestAdapter(getApplicationContext(), arrayListRequestAlter);
                            exRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("requestBloodGroup", Arrays.asList("O-", "O+", "A-", "A+", "B-", "B+", "AB-"))
                    .whereEqualTo("requestDistrict", district);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            alterText.setVisibility(View.GONE);
                            exRequestListAlter.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            exRequestListAlter.setVisibility(View.VISIBLE);
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
        Intent intents = new Intent(ExploreRequest.this, MainActivity.class);
        startActivity(intents);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intents = new Intent(ExploreRequest.this, MainActivity.class);
        startActivity(intents);
        finish();
    }

}