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
import com.aisolutions.bloodcare.Adapters.MyRequestAdapter;
import com.aisolutions.bloodcare.Details.ArraySets;
import com.aisolutions.bloodcare.Objects.AmbulanceDetails;
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

public class Details_Ambulance extends Fragment {

    Spinner location;
    RelativeLayout emptyScreen;
    LinearLayout viewScreen;
    RecyclerView ambulanceList;

    ArraySets arraySets = new ArraySets();
    ArrayAdapter<String> arrayAdapter_ambulance;
    ArrayList<String> arrayList_ambulance = new ArrayList<>();

    FirebaseFirestore ambulance = FirebaseFirestore.getInstance();
    ArrayList<AmbulanceDetails> arrayListAmbulance = new ArrayList<>();
    AmbulanceAdapter adapter;

    View view;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public Details_Ambulance() {
        // Required empty public constructor
    }

    public static Details_Ambulance newInstance(String param1, String param2) {
        Details_Ambulance fragment = new Details_Ambulance();
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
        view = inflater.inflate(R.layout.fragment_details__ambulance, container, false);

        location = view.findViewById(R.id.ambulanceSpinner);
        emptyScreen = view.findViewById(R.id.emptyViewAmbulance);
        viewScreen = view.findViewById(R.id.listView);
        ambulanceList = view.findViewById(R.id.ambulanceList);

        //-----------Loading data into array Lists------------------------------------------------->
        for (int x = 0; x < arraySets.all_districts.length; x++) {
            arrayList_ambulance.add(arraySets.all_districts[x]);
        }
        //--------------Array Adapters--------------------------------------------------------------
        arrayAdapter_ambulance = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_ambulance);
        location.setAdapter(arrayAdapter_ambulance);

        location.setSelection(0);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ambulanceList.setLayoutManager(layoutManager);
        arrayListAmbulance.clear();

        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            CollectionReference reference = ambulance.collection("Ambulance");

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayListAmbulance.clear();
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
                                    arrayListAmbulance.clear();

                                    for (QueryDocumentSnapshot documentSnapshot : value) {
                                        AmbulanceDetails data = documentSnapshot.toObject(AmbulanceDetails.class);
                                        arrayListAmbulance.add(data);
                                    }

                                    adapter = new AmbulanceAdapter(getContext(), arrayListAmbulance);
                                    ambulanceList.setAdapter(adapter);
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
                                    arrayListAmbulance.clear();

                                    for (QueryDocumentSnapshot documentSnapshot : value) {
                                        AmbulanceDetails data = documentSnapshot.toObject(AmbulanceDetails.class);
                                        arrayListAmbulance.add(data);
                                    }

                                    adapter = new AmbulanceAdapter(getContext(), arrayListAmbulance);
                                    ambulanceList.setAdapter(adapter);
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
                                arrayListAmbulance.clear();

                                for (QueryDocumentSnapshot documentSnapshot : value) {
                                    AmbulanceDetails data = documentSnapshot.toObject(AmbulanceDetails.class);
                                    arrayListAmbulance.add(data);
                                }

                                adapter = new AmbulanceAdapter(getContext(), arrayListAmbulance);
                                ambulanceList.setAdapter(adapter);
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