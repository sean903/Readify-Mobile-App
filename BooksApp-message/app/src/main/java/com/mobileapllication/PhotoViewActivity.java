package com.mobileapllication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mobileapllication.databinding.ActivityPhotoViewBinding;

public class PhotoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPhotoViewBinding binding = ActivityPhotoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Glide.with(this).load(getIntent().getStringExtra("uri")).into(binding.photoView);
    }
}