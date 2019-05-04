package com.example.secondfirebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.example.secondfirebaseauth.ChatActivity.EXTRA_URL;
import static com.example.secondfirebaseauth.ChatActivity.EXTRA_USER_EMAIL;

public class FullImageActivity extends AppCompatActivity {

    TextView fullImageEmail;
    ImageView chatFullImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        fullImageEmail = findViewById(R.id.full_image_email);
        chatFullImage =  findViewById(R.id.chat_full_image);

        Intent intent = getIntent();
        String imageURL = intent.getStringExtra(EXTRA_URL);
        String userEmail = intent.getStringExtra(EXTRA_USER_EMAIL);

        Glide.with(this).load(imageURL).into(chatFullImage);
        fullImageEmail.setText(userEmail);

    }

}
