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
import com.aisolutions.bloodcare.Objects.DonorDetails;
import com.aisolutions.bloodcare.R;

import java.util.ArrayList;

public class AmbulanceAdapter extends RecyclerView.Adapter<AmbulanceAdapter.MyViewHolder> {

    Context context;
    ArrayList<AmbulanceDetails> ambulanceDetails;

    public AmbulanceAdapter(Context context, ArrayList<AmbulanceDetails> ambulanceDetails) {
        this.context = context;
        this.ambulanceDetails = ambulanceDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.list_ambulance, parent, false);
        final AmbulanceAdapter.MyViewHolder viewHolder = new AmbulanceAdapter.MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AmbulanceAdapter.MyViewHolder holder, int position) {
        holder.name.setText(ambulanceDetails.get(position).getName());
        holder.location.setText(ambulanceDetails.get(position).getLocation());
        holder.number.setText(ambulanceDetails.get(position).getNumber());

        holder.aItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + ambulanceDetails.get(position).getNumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ambulanceDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, number;
        CardView aItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ambulance_name);
            location = itemView.findViewById(R.id.ambulance_location);
            number = itemView.findViewById(R.id.ambulance_number);
            aItem = itemView.findViewById(R.id.ambulance_details);
        }
    }
}
