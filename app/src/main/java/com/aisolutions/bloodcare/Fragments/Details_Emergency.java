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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aisolutions.bloodcare.Adapters.AmbulanceAdapter;
import com.aisolutions.bloodcare.Adapters.EmergencyNumbersAdapter;
import com.aisolutions.bloodcare.Objects.AmbulanceDetails;
import com.aisolutions.bloodcare.Objects.EmergenceNumberDetails;
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


public class Details_Emergency extends Fragment {

    RelativeLayout emptyScreen;
    LinearLayout viewScreen;
    RecyclerView emNumberList;

    FirebaseFirestore emNumber = FirebaseFirestore.getInstance();
    ArrayList<EmergenceNumberDetails> arrayListEmNumber = new ArrayList<>();
    EmergencyNumbersAdapter adapter;

    View view;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Details_Emergency() {
        // Required empty public constructor
    }

    public static Details_Emergency newInstance(String param1, String param2) {
        Details_Emergency fragment = new Details_Emergency();
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
        view = inflater.inflate(R.layout.fragment_details__emergency, container, false);

        emptyScreen = view.findViewById(R.id.emptyView);
        viewScreen = view.findViewById(R.id.listView);
        emNumberList = view.findViewById(R.id.emCallList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        emNumberList.setLayoutManager(layoutManager);
        arrayListEmNumber.clear();

        CollectionReference reference = emNumber.collection("Emergence_number");
        reference
                .orderBy("priority", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListEmNumber.clear();

                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            EmergenceNumberDetails data = documentSnapshot.toObject(EmergenceNumberDetails.class);
                            arrayListEmNumber.add(data);
                        }

                        adapter = new EmergencyNumbersAdapter(getContext(), arrayListEmNumber);
                        emNumberList.setAdapter(adapter);
                    }
                });
        Query query = reference
                .orderBy("priority", Query.Direction.ASCENDING);
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

        return view;
    }
}