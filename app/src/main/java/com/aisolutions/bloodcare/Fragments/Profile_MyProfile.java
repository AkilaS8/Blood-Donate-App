package com.aisolutions.bloodcare.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aisolutions.bloodcare.Activities.Login;
import com.aisolutions.bloodcare.Activities.Profile;
import com.aisolutions.bloodcare.Objects.UserDetails;
import com.aisolutions.bloodcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;


public class Profile_MyProfile extends Fragment {
    View view;
    UserDetails loginUser;
    TextView name, telephone, address;
    EditText cu_password, nw_password, re_nw_password;
    RelativeLayout passwordView;
    ToggleButton editPassword;

    FirebaseFirestore updateProfile = FirebaseFirestore.getInstance();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Profile_MyProfile() {
        // Required empty public constructor
    }

    public static Profile_MyProfile newInstance(String param1, String param2) {
        Profile_MyProfile fragment = new Profile_MyProfile();
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
        view = inflater.inflate(R.layout.fragment_profile__my_profile, container, false);

        name = view.findViewById(R.id.profile_name);
        telephone = view.findViewById(R.id.profile_telephone);
        address = view.findViewById(R.id.profile_address);

        cu_password = view.findViewById(R.id.profile_cu_password);
        nw_password = view.findViewById(R.id.profile_nw_password);
        re_nw_password = view.findViewById(R.id.profile_nw_re_password);
        re_nw_password = view.findViewById(R.id.profile_nw_re_password);

        passwordView = view.findViewById(R.id.profile_password);
        editPassword = view.findViewById(R.id.edit_password);

        passwordView.setVisibility(View.GONE);

        //------------------Fill login user data----------------------------------------------------
        SharedPreferences loginUserPreferences = this.getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = loginUserPreferences.getString("LoginUser", "");
        loginUser = gson.fromJson(json, UserDetails.class);

        name.setText(loginUser.getUserName());
        telephone.setText(loginUser.getUserTelephone());
        address.setText(loginUser.getUserAddress());
        //Log.d("userName-->", loginUser.getUserName());
        //------------------------------------------------------------------------------------------

        editPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //enable click
                    passwordView.setVisibility(View.VISIBLE);
                    cu_password.setText("");
                    nw_password.setText("");
                    re_nw_password.setText("");
                } else {
                    //disable click
                    //TODO:Save Part
                    if (cu_password.getText().toString().equals(loginUser.getUserPassword())) {
                        if (!nw_password.getText().toString().isEmpty() && !re_nw_password.getText().toString().isEmpty()) {
                            if (nw_password.getText().toString().equals(re_nw_password.getText().toString())) {
                                //TODO: database update
                                DocumentReference updateReference = updateProfile.document("Users/" + loginUser.getUserTelephone());
                                updateReference
                                        .update("userPassword", nw_password.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();
                                                    passwordView.setVisibility(View.GONE);
                                                    shutDown();
                                                } else {
                                                    Toast.makeText(getActivity(), String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(getActivity(), "Re-enter new password correctly", Toast.LENGTH_SHORT).show();
                                nw_password.setText("");
                                re_nw_password.setText("");
                                nw_password.requestFocus();
                                editPassword.setChecked(true);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
                            editPassword.setChecked(true);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Currant Password not matching", Toast.LENGTH_SHORT).show();
                        cu_password.setText("");
                        nw_password.setText("");
                        re_nw_password.setText("");
                        cu_password.requestFocus();
                        editPassword.setChecked(true);
                    }
                }
            }
        });

        return view;
    }

    public void shutDown() {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }
}