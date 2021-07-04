package com.aisolutions.bloodcare.Adapters;/*
Created by Akila Ishan on 2021-06-20.
*/

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

import com.aisolutions.bloodcare.Activities.News;
import com.aisolutions.bloodcare.Objects.NewsDetails;
import com.aisolutions.bloodcare.Objects.RequestBlood;
import com.aisolutions.bloodcare.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{

    Context context;
    ArrayList<NewsDetails> news;

    public NewsAdapter(Context context, ArrayList<NewsDetails> news) {
        this.context = context;
        this.news = news;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.list_news, parent, false);
        final NewsAdapter.MyViewHolder viewHolder = new NewsAdapter.MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position) {
        holder.nTopic.setText(news.get(position).getTopic());
        holder.nSummery.setText(news.get(position).getSummary());
        holder.nDate.setText(news.get(position).getDate());
        holder.setImage(news.get(position).getImageUrl());

        holder.nItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), News.class);
                intent.putExtra("topic", news.get(position).getTopic());
                intent.putExtra("summary", news.get(position).getSummary());
                intent.putExtra("description", news.get(position).getDescription());
                intent.putExtra("date", news.get(position).getDate());
                intent.putExtra("imageUrl", news.get(position).getImageUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nTopic, nSummery, nDate;
        ImageView nImage;
        CardView nItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nTopic = itemView.findViewById(R.id.n_topic);
            nSummery = itemView.findViewById(R.id.n_Summary);
            nDate = itemView.findViewById(R.id.n_date);
            nImage = itemView.findViewById(R.id.n_image);
            nItem = itemView.findViewById(R.id.nList_details);
        }

        public void setImage (final String url){
            Glide.with(itemView.getContext()).load(url).into(nImage);
        }
    }
}
