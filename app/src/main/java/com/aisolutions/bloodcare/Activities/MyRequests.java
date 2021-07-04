package com.aisolutions.bloodcare.Activities;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aisolutions.bloodcare.Adapters.DonorAdapter;
import com.aisolutions.bloodcare.Adapters.RequestAdapter;
import com.aisolutions.bloodcare.Objects.DonorDetails;
import com.aisolutions.bloodcare.Objects.RequestBlood;
import com.aisolutions.bloodcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class MyRequests extends AppCompatActivity {
    TextView bloodGroup, forWhom, location, alterText;
    RecyclerView myRequestList, myRequestListAlter;
    RelativeLayout emptyDataView, resultsView, fullView;
    ImageView backMain, delete;

    FirebaseFirestore exploreMyRequest = FirebaseFirestore.getInstance();
    ArrayList<DonorDetails> arrayListMyRequest = new ArrayList<>();
    DonorAdapter adapter;

    ArrayList<DonorDetails> arrayListMyRequestAlter = new ArrayList<>();
    DonorAdapter adapterAlter;

    String reID = "";
    String reBloodGroup = "";
    String reLocation = "";
    String requestTelephone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        bloodGroup = findViewById(R.id.re_bloodGroup);
        forWhom = findViewById(R.id.re_forWhom);
        location = findViewById(R.id.re_location);

        myRequestList = findViewById(R.id.myRequestResults);
        myRequestListAlter = findViewById(R.id.myRequestResultsAlt);

        emptyDataView = findViewById(R.id.myRequest_empty);
        alterText = findViewById(R.id.myRequest_alt_txt);
        resultsView = findViewById(R.id.resultsView);

        backMain = findViewById(R.id.back_myRequest);
        delete = findViewById(R.id.request_delete);
        fullView = findViewById(R.id.my_request_full);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRequestList.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        myRequestListAlter.setLayoutManager(layoutManager1);

        //---Intent data----
        reID = getIntent().getStringExtra("rID");
        reBloodGroup = getIntent().getStringExtra("rBloodGroup");
        reLocation = getIntent().getStringExtra("rLocation");
        //--------------
        SharedPreferences preferencesID = MyRequests.this.getSharedPreferences("loginUser", MODE_PRIVATE);
        requestTelephone = preferencesID.getString("USER_ID", "");

        bloodGroup.setText(reBloodGroup);
        forWhom.setText(getIntent().getStringExtra("rFor"));
        location.setText(reLocation);


        //---------------searching--------------------------->
        searchData(reBloodGroup, reLocation);

        //----back menu
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //----delete request--------->
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(fullView, "Are you sure to delete  Request?", Snackbar.LENGTH_INDEFINITE)
                        .setAction("YES", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO: delete Account
                                DocumentReference deleteReference = exploreMyRequest.document("Requests/" + requestTelephone + "_" + reID);
                                deleteReference.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentReference deleteR = exploreMyRequest.document("MyRequests/" + requestTelephone + "/" + requestTelephone + "/" + reID);
                                                    deleteR.delete()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(MyRequests.this, "Completely Delete Request", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                    } else {
                                                                        Toast.makeText(MyRequests.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    Toast.makeText(MyRequests.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }).setActionTextColor(getResources().getColor(R.color.text_main))
                        .show();
            }
        });
    }

    private void searchData(String reBloodGroup, String reLocation) {
        CollectionReference collectionReference = exploreMyRequest.collection("Donors");
        collectionReference
                .whereEqualTo("donorBloodGroup", reBloodGroup)
                .whereEqualTo("donorDistrict", reLocation)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListMyRequest.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                            arrayListMyRequest.add(data);
                        }
                        adapter = new DonorAdapter(getApplicationContext(), arrayListMyRequest);
                        myRequestList.setAdapter(adapter);
                    }
                });
        if (!reBloodGroup.equals("O-")) {
            alter_searchData(reBloodGroup, reLocation);
        } else {
            Query query = collectionReference
                    .whereEqualTo("donorBloodGroup", reBloodGroup)
                    .whereEqualTo("donorDistrict", reLocation);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            resultsView.setVisibility(View.GONE);
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

    private void alter_searchData(String reBloodGroup, String reLocation) {
        arrayListMyRequestAlter.clear();

        CollectionReference searchingAlter = exploreMyRequest.collection("Donors");

        if (reBloodGroup.equals("O+") || reBloodGroup.equals("A-") || reBloodGroup.equals("B-")) {

            if (reBloodGroup.equals("O+")) {
                alterText.setText("O+ blood group recipients are able to receive blood from O+ and O- blood group donors");
            } else if (reBloodGroup.equals("A-")) {
                alterText.setText("A- blood group recipients are able to receive blood from A- and O- blood group donors");
            } else if (reBloodGroup.equals("B-")) {
                alterText.setText("B- blood group recipients are able to receive blood from B- and O- blood group donors");
            }

            searchingAlter
                    .whereEqualTo("donorBloodGroup", "O-")
                    .whereEqualTo("donorDistrict", reLocation)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListMyRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListMyRequestAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListMyRequestAlter);
                            myRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereEqualTo("donorBloodGroup", "O-")
                    .whereEqualTo("donorDistrict", reLocation);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            resultsView.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            myRequestListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (reBloodGroup.equals("A+")) {
            alterText.setText("A+ blood group recipients are able to receive blood from A+, A-, O+ and O- blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-"))
                    .whereEqualTo("donorDistrict", reLocation)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListMyRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListMyRequestAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListMyRequestAlter);
                            myRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-"))
                    .whereEqualTo("donorDistrict", reLocation);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            resultsView.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            myRequestListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });

        } else if (reBloodGroup.equals("B+")) {
            alterText.setText("B+ blood group recipients are able to receive blood from B+, B-, O+ and O- blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "B-"))
                    .whereEqualTo("donorDistrict", reLocation)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListMyRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListMyRequestAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListMyRequestAlter);
                            myRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "B-"))
                    .whereEqualTo("donorDistrict", reLocation);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            resultsView.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            myRequestListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (reBloodGroup.equals("AB-")) {
            alterText.setText("AB- blood group recipients are able to receive blood from AB-, O-, A- and B- blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "A-", "B-"))
                    .whereEqualTo("donorDistrict", reLocation)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListMyRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListMyRequestAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListMyRequestAlter);
                            myRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "A-", "B-"))
                    .whereEqualTo("donorDistrict", reLocation);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            resultsView.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            myRequestListAlter.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("Error getting documents: ", String.valueOf(task.getException()));
                    }
                }
            });
        } else if (reBloodGroup.equals("AB+")) {
            alterText.setText("AB+ blood group recipients are able to receive blood from all other blood group donors");

            searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-", "A+", "B-", "B+", "AB-"))
                    .whereEqualTo("donorDistrict", reLocation)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            arrayListMyRequestAlter.clear();
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                DonorDetails data = documentSnapshot.toObject(DonorDetails.class);
                                arrayListMyRequestAlter.add(data);
                            }
                            adapterAlter = new DonorAdapter(getApplicationContext(), arrayListMyRequestAlter);
                            myRequestListAlter.setAdapter(adapterAlter);
                        }
                    });
            Query query = searchingAlter
                    .whereIn("donorBloodGroup", Arrays.asList("O-", "O+", "A-", "A+", "B-", "B+", "AB-"))
                    .whereEqualTo("donorDistrict", reLocation);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        boolean b = task.getResult().isEmpty();
                        if (b) {
                            Log.d("Status------>", "empty");
                            resultsView.setVisibility(View.GONE);
                            emptyDataView.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("Status------>", "no");
                            alterText.setVisibility(View.VISIBLE);
                            myRequestListAlter.setVisibility(View.VISIBLE);
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
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
