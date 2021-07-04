package com.aisolutions.bloodcare.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aisolutions.bloodcare.Activities.BecomeDonor;
import com.aisolutions.bloodcare.Activities.MainActivity;
import com.aisolutions.bloodcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class Profile_DonorProfile extends Fragment {
    View view;
    RelativeLayout emptyView, profile;
    TextView beDonor, delete, bloodGroup, age;
    EditText weight;
    ToggleButton editWeight;
    FrameLayout donorView;

    String Status = "";
    String userID = "";

    FirebaseFirestore donorProfile = FirebaseFirestore.getInstance();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Profile_DonorProfile() {
        // Required empty public constructor
    }

    public static Profile_DonorProfile newInstance(String param1, String param2) {
        Profile_DonorProfile fragment = new Profile_DonorProfile();
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
        view = inflater.inflate(R.layout.fragment_profile__donor_profile, container, false);

        emptyView = view.findViewById(R.id.emptyView);
        profile = view.findViewById(R.id.donor_profile);
        beDonor = view.findViewById(R.id.beDonor);
        weight = view.findViewById(R.id.d_profile_weight);
        delete = view.findViewById(R.id.deleteDonor);
        editWeight = view.findViewById(R.id.edit_weight);
        bloodGroup = view.findViewById(R.id.d_profile_bloodGroup);
        age = view.findViewById(R.id.d_profile_age);

        donorView = view.findViewById(R.id.donor_view);

        SharedPreferences preferencesID = getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        userID = preferencesID.getString("USER_ID", "");

        //----visibility----------------------------------->
        SharedPreferences preferences = getActivity().getSharedPreferences("donorStatus", MODE_PRIVATE);
        Status = preferences.getString("donorStatus", "");

        if (Status.equals("YES")) {
            DocumentReference documentReference = donorProfile.document("Donors/" + userID);

            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        bloodGroup.setText(documentSnapshot.getString("donorBloodGroup"));
                        age.setText(documentSnapshot.getString("donorAge") + " years old");
                        weight.setText(documentSnapshot.getString("donorWeight"));
                    }
                }
            });
            emptyView.setVisibility(View.GONE);
            profile.setVisibility(View.VISIBLE);

        } else {
            emptyView.setVisibility(View.VISIBLE);
            profile.setVisibility(View.GONE);
        }
        //------------------------------------------------->

        //----edit weight------------->
        editWeight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //enable click
                    weight.setEnabled(true);
                } else {
                    //disable
                    //TODO: update data

                    String w = weight.getText().toString();

                    if (!w.isEmpty()) {
                        if (Integer.parseInt(w) >= 50) {
                            //TODO: update Database
                            DocumentReference updateReference = donorProfile.document("Donors/" + userID);
                            updateReference
                                    .update("donorWeight", w)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Weight Updated", Toast.LENGTH_SHORT).show();
                                                weight.setEnabled(false);
                                            } else {
                                                Toast.makeText(getActivity(), String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Your weight is not in reference range.", Toast.LENGTH_SHORT).show();
                            weight.setText("");
                            weight.requestFocus();
                            editWeight.setChecked(true);
                        }

                    } else {
                        Toast.makeText(getActivity(), "Fill weight", Toast.LENGTH_SHORT).show();
                        editWeight.setChecked(true);
                    }
                }
            }
        });
        //----------------------------->

        //------become donor------------------------------------>
        beDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BecomeDonor.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        //----------------------------------------------------->

        //-------------delete donor account----------------->
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(donorView, "Are you sure to delete  Donor Account?", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO: delete account
                                DocumentReference deleteReference = donorProfile.document("Donors/" + userID);
                                deleteReference
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else {
                                                    Toast.makeText(getActivity(), String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.text_main))
                        .show();
            }
        });
        //-------------------------------------------------->

        return view;
    }

}
