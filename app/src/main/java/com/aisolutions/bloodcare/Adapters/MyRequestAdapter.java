package com.aisolutions.bloodcare.Adapters;/*
Created by Akila Ishan on 2021-06-17.
*/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aisolutions.bloodcare.Activities.MyRequests;
import com.aisolutions.bloodcare.Objects.RequestBlood;
import com.aisolutions.bloodcare.R;

import java.util.ArrayList;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.MyViewHolder>{

    Context context;
    ArrayList<RequestBlood> request;
    String bloodGroup = "";

    public MyRequestAdapter(Context context, ArrayList<RequestBlood> request) {
        this.context = context;
        this.request = request;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.list_requests, parent, false);
        final MyRequestAdapter.MyViewHolder viewHolder = new MyRequestAdapter.MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestAdapter.MyViewHolder holder, int position) {

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MyRequests.class);
                intent.putExtra("rID",request.get(position).getRequestID());
                intent.putExtra("rBloodGroup",request.get(position).getRequestBloodGroup());
                intent.putExtra("rFor",request.get(position).getRequestForWhom());
                intent.putExtra("rLocation",request.get(position).getRequestDistrict());

                context.startActivity(intent);
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
