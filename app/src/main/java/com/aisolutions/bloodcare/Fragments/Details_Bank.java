package com.aisolutions.bloodcare.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.aisolutions.bloodcare.Adapters.AmbulanceAdapter;
import com.aisolutions.bloodcare.Adapters.BloodBankAdapter;
import com.aisolutions.bloodcare.Details.ArraySets;
import com.aisolutions.bloodcare.Objects.AmbulanceDetails;
import com.aisolutions.bloodcare.Objects.BloodBankDetails;
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

public class Details_Bank extends Fragment {

    Spinner location;
    RelativeLayout emptyScreen;
    LinearLayout viewScreen;
    RecyclerView bankList;

    ArraySets arraySets = new ArraySets();
    ArrayAdapter<String> arrayAdapter_bank;
    ArrayList<String> arrayList_bank = new ArrayList<>();

    FirebaseFirestore bank = FirebaseFirestore.getInstance();
    ArrayList<BloodBankDetails> arrayListBank = new ArrayList<>();
    BloodBankAdapter adapter;

    View view;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public Details_Bank() {
        // Required empty public constructor
    }

    public static Details_Bank newInstance(String param1, String param2) {
        Details_Bank fragment = new Details_Bank();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_details__bank, container, false);

        location = view.findViewById(R.id.bankSpinner);
        emptyScreen = view.findViewById(R.id.emptyViewBank);
        viewScreen = view.findViewById(R.id.listView);
        bankList = view.findViewById(R.id.bankList);

        //-----------Loading data into array Lists------------------------------------------------->
        for (int x = 0; x < arraySets.all_districts.length; x++) {
            arrayList_bank.add(arraySets.all_districts[x]);
        }
        //--------------Array Adapters--------------------------------------------------------------
        arrayAdapter_bank = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_bank);
        location.setAdapter(arrayAdapter_bank);

        location.setSelection(0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bankList.setLayoutManager(layoutManager);
        arrayListBank.clear();

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            CollectionReference reference = bank.collection("Blood_Bank");
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayListBank.clear();
                String locationS = arraySets.all_districts[position];


                if (locationS.equals("All")) {
                    reference
                            .orderBy("location", Query.Direction.ASCENDING)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (error != null) {
                                        return;
                                    }
                                    arrayListBank.clear();

                                    for (QueryDocumentSnapshot documentSnapshot : value) {
                                        BloodBankDetails data = documentSnapshot.toObject(BloodBankDetails.class);
                                        arrayListBank.add(data);
                                    }

                                    adapter = new BloodBankAdapter(getContext(), arrayListBank);
                                    bankList.setAdapter(adapter);
                                }
                            });
                    Query query = reference
                            .orderBy("location", Query.Direction.ASCENDING);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean b = task.getResult().isEmpty();
                                if (b) {
                                    Log.d("Status------>", "empty");
                                    emptyScreen.setVisibility(View.VISIBLE);
                                    viewScreen.setVisibility(View.GONE);

                                } else {
                                    Log.d("Status------>", "no");
                                    emptyScreen.setVisibility(View.GONE);
                                    viewScreen.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Log.d("Error getting documents: ", String.valueOf(task.getException()));
                            }
                        }
                    });

                } else {
                    reference
                            .whereIn("location", Arrays.asList("All", locationS))
                            .orderBy("location", Query.Direction.ASCENDING)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (error != null) {
                                        return;
                                    }
                                    arrayListBank.clear();

                                    for (QueryDocumentSnapshot documentSnapshot : value) {
                                        BloodBankDetails data = documentSnapshot.toObject(BloodBankDetails.class);
                                        arrayListBank.add(data);
                                    }

                                    adapter = new BloodBankAdapter(getContext(), arrayListBank);
                                    bankList.setAdapter(adapter);
                                }
                            });
                    Query query = reference
                            .whereIn("location", Arrays.asList("All", locationS));
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean b = task.getResult().isEmpty();
                                if (b) {
                                    Log.d("Status------>", "empty");
                                    emptyScreen.setVisibility(View.VISIBLE);
                                    viewScreen.setVisibility(View.GONE);

                                } else {
                                    Log.d("Status------>", "no");
                                    emptyScreen.setVisibility(View.GONE);
                                    viewScreen.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Log.d("Error getting documents: ", String.valueOf(task.getException()));
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                reference
                        .orderBy("location", Query.Direction.ASCENDING)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    return;
                                }
                                arrayListBank.clear();

                                for (QueryDocumentSnapshot documentSnapshot : value) {
                                    BloodBankDetails data = documentSnapshot.toObject(BloodBankDetails.class);
                                    arrayListBank.add(data);
                                }

                                adapter = new BloodBankAdapter(getContext(), arrayListBank);
                                bankList.setAdapter(adapter);
                            }
                        });
                Query query = reference
                        .orderBy("location", Query.Direction.ASCENDING);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean b = task.getResult().isEmpty();
                            if (b) {
                                Log.d("Status------>", "empty");
                                emptyScreen.setVisibility(View.VISIBLE);
                                viewScreen.setVisibility(View.GONE);

                            } else {
                                Log.d("Status------>", "no");
                                emptyScreen.setVisibility(View.GONE);
                                viewScreen.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.d("Error getting documents: ", String.valueOf(task.getException()));
                        }
                    }
                });
            }
        });

        return view;
    }
}