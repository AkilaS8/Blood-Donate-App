package com.aisolutions.bloodcare.Fragments;

import android.content.Intent;
import android.net.Uri;
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

import com.aisolutions.bloodcare.Adapters.MyRequestAdapter;
import com.aisolutions.bloodcare.Adapters.NewsAdapter;
import com.aisolutions.bloodcare.Objects.NewsDetails;
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

public class Details_News extends Fragment {

    View view;

    RecyclerView newsList;

    FirebaseFirestore news = FirebaseFirestore.getInstance();
    ArrayList<NewsDetails> arrayListNews = new ArrayList<>();
    NewsAdapter adapter;

    RelativeLayout show, empty;
    TextView addNews;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Details_News() {
        // Required empty public constructor
    }

    public static Details_News newInstance(String param1, String param2) {
        Details_News fragment = new Details_News();
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
        view = inflater.inflate(R.layout.fragment_details__news, container, false);

        newsList = view.findViewById(R.id.newsList);
        show = view.findViewById(R.id.showViewN);
        empty = view.findViewById(R.id.emptyViewN);
        addNews = view.findViewById(R.id.notify);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(layoutManager);
        arrayListNews.clear();

        CollectionReference reference = news.collection("News");
        reference
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        arrayListNews.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            NewsDetails data = documentSnapshot.toObject(NewsDetails.class);
                            arrayListNews.add(data);
                        }
                        adapter = new NewsAdapter(getContext(), arrayListNews);
                        newsList.setAdapter(adapter);
                    }
                });
        Query query = reference
                .orderBy("date", Query.Direction.DESCENDING);
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

        //----------add news----->
        addNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] address = {"donateblood15@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, address);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Request for adding News");
                intent.putExtra(Intent.EXTRA_TEXT, "//Please enter your news details here in detail.");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        return view;
    }
}