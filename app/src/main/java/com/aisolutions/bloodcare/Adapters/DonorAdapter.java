package com.aisolutions.bloodcare.Adapters;/*
Created by Akila Ishan on 2021-05-26.
*/

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aisolutions.bloodcare.Objects.DonorDetails;
import com.aisolutions.bloodcare.R;

import java.util.ArrayList;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.MyViewHolder> {

    Context context;
    ArrayList<DonorDetails> donor;
    String bloodGroup = "";

    public DonorAdapter(Context context, ArrayList<DonorDetails> donor) {
        this.context = context;
        this.donor = donor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.list_donors, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DonorAdapter.MyViewHolder holder, int position) {

        holder.dName.setText(donor.get(position).getDonorName());
        holder.dAge.setText(donor.get(position).getDonorAge() + " years old");
        holder.dDistrict.setText(donor.get(position).getDonorDistrict());

        bloodGroup = donor.get(position).getDonorBloodGroup();

        if (bloodGroup.equals("A+")) {
            holder.dBloodGroup.setBackgroundResource(R.drawable.aplus_c);
        } else if (bloodGroup.equals("A-")) {
            holder.dBloodGroup.setBackgroundResource(R.drawable.aneg_c);
        } else if (bloodGroup.equals("B+")) {
            holder.dBloodGroup.setBackgroundResource(R.drawable.bplus_c);
        } else if (bloodGroup.equals("B-")) {
            holder.dBloodGroup.setBackgroundResource(R.drawable.bneg_c);
        } else if (bloodGroup.equals("O+")) {
            holder.dBloodGroup.setBackgroundResource(R.drawable.oplus_c);
        } else if (bloodGroup.equals("O-")) {
            holder.dBloodGroup.setBackgroundResource(R.drawable.oneg_c);
        } else if (bloodGroup.equals("AB+")) {
            holder.dBloodGroup.setBackgroundResource(R.drawable.abplus_c);
        } else if (bloodGroup.equals("AB-")) {
            holder.dBloodGroup.setBackgroundResource(R.drawable.abneg_c);
        }

        holder.dItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View detailsDialog = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_box_donor, null);

                TextView dialog_name = detailsDialog.findViewById(R.id.donorDialog_name);
                TextView dialog_age = detailsDialog.findViewById(R.id.donorDialog_age);
                TextView dialog_district = detailsDialog.findViewById(R.id.donorDialog_district);
                TextView dialog_telephone = detailsDialog.findViewById(R.id.donorDialog_telephone);
                TextView dialog_address = detailsDialog.findViewById(R.id.donorDialog_address);
                LinearLayout dialog_contact = detailsDialog.findViewById(R.id.donorDialog_contact);
                LinearLayout dialog_direction = detailsDialog.findViewById(R.id.donorDialog_direction);
                ImageView dialog_bloodGroup = detailsDialog.findViewById(R.id.donorDialog_bloodGroup);

                dialog_name.setText(donor.get(position).getDonorName());
                dialog_age.setText(donor.get(position).getDonorAge() + " years old");
                dialog_district.setText(donor.get(position).getDonorDistrict() + " , " + donor.get(position).getDonorProvince());
                dialog_telephone.setText(donor.get(position).getDonorTelephone());
                dialog_address.setText(donor.get(position).getDonorAddress());

                String blood = donor.get(position).getDonorBloodGroup();

                if (blood.equals("A+")) {
                    dialog_bloodGroup.setBackgroundResource(R.drawable.aplus_u);
                } else if (blood.equals("A-")) {
                    dialog_bloodGroup.setBackgroundResource(R.drawable.aneg_u);
                } else if (blood.equals("B+")) {
                    dialog_bloodGroup.setBackgroundResource(R.drawable.bplus_u);
                } else if (blood.equals("B-")) {
                    dialog_bloodGroup.setBackgroundResource(R.drawable.bneg_u);
                } else if (blood.equals("O+")) {
                    dialog_bloodGroup.setBackgroundResource(R.drawable.oplus_u);
                } else if (blood.equals("O-")) {
                    dialog_bloodGroup.setBackgroundResource(R.drawable.oneg_u);
                } else if (blood.equals("AB+")) {
                    dialog_bloodGroup.setBackgroundResource(R.drawable.abplus_u);
                } else if (blood.equals("AB-")) {
                    dialog_bloodGroup.setBackgroundResource(R.drawable.abneg_u);
                }

                //---------------contact donor---------------------------
                dialog_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel: " + donor.get(position).getDonorTelephone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });

                //-------------donor location-----------------------------
                dialog_direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String lat = donor.get(position).getDonorLat().toString();
                        String lng = donor.get(position).getDonorLan().toString();

                        float latitude = Float.parseFloat(lat);
                        float longitude = Float.parseFloat(lng);

                        Intent intentD = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + latitude + "," + longitude + "&model=b"));
                        intentD.setPackage("com.google.android.apps.maps");
                        intentD.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (intentD.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intentD);
                        }
                    }
                });

                builder.setView(detailsDialog);
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
    }

    @Override
    public int getItemCount() {
        return donor.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dName, dAge, dDistrict;
        ImageView dBloodGroup;
        CardView dItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dName = itemView.findViewById(R.id.name_dList);
            dAge = itemView.findViewById(R.id.age_dList);
            dDistrict = itemView.findViewById(R.id.district_dList);
            dBloodGroup = itemView.findViewById(R.id.bloodGroup_dList);
            dItem = itemView.findViewById(R.id.dList_details);
        }

    }
}
