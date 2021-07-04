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

import com.aisolutions.bloodcare.Objects.BloodBankDetails;
import com.aisolutions.bloodcare.Objects.EmergenceNumberDetails;
import com.aisolutions.bloodcare.R;

import java.util.ArrayList;

public class EmergencyNumbersAdapter extends RecyclerView.Adapter<EmergencyNumbersAdapter.MyViewHolder>{
    Context context;
    ArrayList<EmergenceNumberDetails> emNumberDetails;

    public EmergencyNumbersAdapter(Context context, ArrayList<EmergenceNumberDetails> emNumberDetails) {
        this.context = context;
        this.emNumberDetails = emNumberDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.list_em_call, parent, false);
        final EmergencyNumbersAdapter.MyViewHolder viewHolder = new EmergencyNumbersAdapter.MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyNumbersAdapter.MyViewHolder holder, int position) {
        holder.name.setText(emNumberDetails.get(position).getName());
        holder.number.setText(emNumberDetails.get(position).getNumber());

        holder.eItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + emNumberDetails.get(position).getNumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emNumberDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, number;
        CardView eItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.emCall_name);
            number = itemView.findViewById(R.id.emCall_number);
            eItem = itemView.findViewById(R.id.emCall_details);
        }
    }
}
