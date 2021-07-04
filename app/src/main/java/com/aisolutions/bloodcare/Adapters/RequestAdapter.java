package com.aisolutions.bloodcare.Adapters;/*
Created by Akila Ishan on 2021-06-16.
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aisolutions.bloodcare.Objects.DonorDetails;
import com.aisolutions.bloodcare.Objects.RequestBlood;
import com.aisolutions.bloodcare.R;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    Context context;
    ArrayList<RequestBlood> request;
    String bloodGroup = "";

    public RequestAdapter(Context context, ArrayList<RequestBlood> request) {
        this.context = context;
        this.request = request;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.list_requests, parent, false);
        final RequestAdapter.MyViewHolder viewHolder = new RequestAdapter.MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.MyViewHolder holder, int position) {
        holder.rName.setText(request.get(position).getRequestName());
        holder.rDistrict.setText(request.get(position).getRequestDistrict());
        holder.rDate.setText(request.get(position).getRequestDate());

        bloodGroup = request.get(position).getRequestBloodGroup();

        if (bloodGroup.equals("A+")) {
            holder.rBloodGroup.setBackgroundResource(R.drawable.aplus_c);
        } else if (bloodGroup.equals("A-")) {
            holder.rBloodGroup.setBackgroundResource(R.drawable.aneg_c);
        } else if (bloodGroup.equals("B+")) {
            holder.rBloodGroup.setBackgroundResource(R.drawable.bplus_c);
        } else if (bloodGroup.equals("B-")) {
            holder.rBloodGroup.setBackgroundResource(R.drawable.bneg_c);
        } else if (bloodGroup.equals("O+")) {
            holder.rBloodGroup.setBackgroundResource(R.drawable.oplus_c);
        } else if (bloodGroup.equals("O-")) {
            holder.rBloodGroup.setBackgroundResource(R.drawable.oneg_c);
        } else if (bloodGroup.equals("AB+")) {
            holder.rBloodGroup.setBackgroundResource(R.drawable.abplus_c);
        } else if (bloodGroup.equals("AB-")) {
            holder.rBloodGroup.setBackgroundResource(R.drawable.abneg_c);
        }

        holder.rItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View detailsDialog = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_box_request, null);

                TextView dialog_name = detailsDialog.findViewById(R.id.requestDialog_name);
                TextView dialog_district = detailsDialog.findViewById(R.id.requestDialog_district);
                TextView dialog_detail = detailsDialog.findViewById(R.id.requestDialog_details);
                TextView dialog_date = detailsDialog.findViewById(R.id.requestDialog_date);
                TextView dialog_telephone = detailsDialog.findViewById(R.id.requestDialog_telephone);
                LinearLayout dialog_contact = detailsDialog.findViewById(R.id.requestDialog_contact);
                ImageView dialog_bloodGroup = detailsDialog.findViewById(R.id.requestDialog_bloodGroup);

                dialog_name.setText(request.get(position).getRequestName());
                dialog_district.setText(request.get(position).getRequestDistrict());
                dialog_date.setText(request.get(position).getRequestDate());
                dialog_detail.setText("Hello !, I am looking for " + request.get(position).getRequestBloodGroup() + " blood to " + request.get(position).getRequestForWhom() + ".\nIf you can help, please contact me.\nThank You !");
                dialog_telephone.setText(request.get(position).getRequestTelephone());

                String blood = request.get(position).getRequestBloodGroup();

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
                        intent.setData(Uri.parse("tel: " + request.get(position).getRequestTelephone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
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
        return request.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rName, rDistrict, rDate;
        ImageView rBloodGroup;
        CardView rItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rName = itemView.findViewById(R.id.name_rList);
            rDistrict = itemView.findViewById(R.id.district_rList);
            rDate = itemView.findViewById(R.id.date_rList);
            rBloodGroup = itemView.findViewById(R.id.bloodGroup_rList);
            rItem = itemView.findViewById(R.id.rList_details);
        }
    }
}
