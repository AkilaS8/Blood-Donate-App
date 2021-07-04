package com.aisolutions.bloodcare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aisolutions.bloodcare.R;
import com.bumptech.glide.Glide;

public class News extends AppCompatActivity {
    TextView topic, description, date;
    ImageView image, back;

    String nTopic = "";
    String nDescription = "";
    String nDate = "";
    String nImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        topic = findViewById(R.id.news_topic);
        description = findViewById(R.id.news_description);
        date = findViewById(R.id.news_date);
        image = findViewById(R.id.news_image);
        back = findViewById(R.id.back_news);

        //Intent Data
        nTopic = getIntent().getStringExtra("topic");
        nDescription = getIntent().getStringExtra("description");
        nDate = getIntent().getStringExtra("date");
        nImage = getIntent().getStringExtra("imageUrl");
        //----------------------

        topic.setText(nTopic);
        description.setText(nDescription);
        date.setText(nDate);

        if (!nImage.isEmpty()) {
            setImage(nImage);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setImage(final String nImage) {
        Glide.with(News.this).load(nImage).into(image);
    }
}