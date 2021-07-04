package com.aisolutions.bloodcare.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aisolutions.bloodcare.Activities.AddRequest;
import com.aisolutions.bloodcare.Adapters.MyRequestAdapter;
import com.aisolutions.bloodcare.Adapters.RequestAdapter;
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
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Profile_MyRequest extends Fragment {

    View view;

    RecyclerView exMyRequest;

    FirebaseFirestore exploreMyRequest = FirebaseFirestore.getInstance();
    ArrayList<RequestBlood> arrayListMyRequest = new ArrayList<>();
    MyRequestAdapter adapter;

    RelativeLayout show, empty;
    TextView addRequest;

    String userID = "";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Profile_MyRequest() {
        // Required empty public constructor
    }

    public static Profile_MyRequest newInstance(String param1, String param2) {
        Profile_MyRequest fragment = new Profile_MyRequest();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile__my_request, container, false);

        exMyRequest = view.findViewById(R.id.myRequestList);
        show = view.findViewById(R.id.showView);
        empty = view.findViewById(R.id.emptyViewRe);
        addRequest = view.findViewById(R.id.addRe);

        SharedPreferences preferencesID = getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        userID = preferencesID.getString("USER_ID", "");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        exMyRequest.setLayoutManager(layoutManager);
        arrayListMyRequest.clear();
        CollectionReference reference = exploreMyRequest.collection("MyRequests/" + userID + "/" + userID);
        reference
                .orderBy("requestDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListMyRequest.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            RequestBlood data = documentSnapshot.toObject(RequestBlood.class);
                            arrayListMyRequest.add(data);
                        }
                        adapter = new MyRequestAdapter(getContext(), arrayListMyRequest);
                        exMyRequest.setAdapter(adapter);
                    }
                });
        Query query = reference.orderBy("requestDate", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean b = task.getResult().isEmpty();
                    if (b) {
                        Log.d("Status------>", "empty");
                        empty.setVisibility(View.VISIBLE);
                        show.setVisibility(View.GONE);

                    } else {
                        Log.d("Status------>", "no");
                        empty.setVisibility(View.GONE);
                        show.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("Error getting documents: ", String.valueOf(task.getException()));
                }
            }
        });

        //---------Add request--------------------->
        addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddRequest.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        //-----------------------------------------

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        CollectionReference reference = exploreMyRequest.collection("MyRequests/" + userID + "/" + userID);
        Query query = reference.orderBy("requestDate", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean b = task.getResult().isEmpty();
                    if (b) {
                        Log.d("Status------>", "empty");
                        empty.setVisibility(View.VISIBLE);
                        show.setVisibility(View.GONE);

                    } else {
                        Log.d("Status------>", "no");
                        empty.setVisibility(View.GONE);
                        show.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("Error getting documents: ", String.valueOf(task.getException()));
                }
            }
        });
    }
}