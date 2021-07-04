package com.aisolutions.bloodcare.Adapters;/*
Created by Akila Ishan on 2021-06-19.
*/

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aisolutions.bloodcare.Objects.AmbulanceDetails;
import com.aisolutions.bloodcare.Objects.BloodBankDetails;
import com.aisolutions.bloodcare.R;

import java.util.ArrayList;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.MyViewHolder> {
    Context context;
    ArrayList<BloodBankDetails> bloodBankDetails;

    public BloodBankAdapter(Context context, ArrayList<BloodBankDetails> bloodBankDetails) {
        this.context = context;
        this.bloodBankDetails = bloodBankDetails;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.list_bank, parent, false);
        final BloodBankAdapter.MyViewHolder viewHolder = new BloodBankAdapter.MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BloodBankAdapter.MyViewHolder holder, int position) {
        holder.name.setText(bloodBankDetails.get(position).getName());
        holder.location.setText(bloodBankDetails.get(position).getLocation());
        holder.address.setText(bloodBankDetails.get(position).getAddress());
        holder.number.setText(bloodBankDetails.get(position).getNumber());

        holder.bItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + bloodBankDetails.get(position).getNumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodBankDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, number, address;
        CardView bItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.bank_name);
            location = itemView.findViewById(R.id.bank_location);
            number = itemView.findViewById(R.id.bank_telephone);
            address = itemView.findViewById(R.id.bank_address);
            bItem = itemView.findViewById(R.id.bank_details);
        }
    }
}
