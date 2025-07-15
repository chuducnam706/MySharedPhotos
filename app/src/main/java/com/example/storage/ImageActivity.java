package com.example.storage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.storage.databinding.ActivityImageBinding;
import com.google.gson.Gson;

public class ImageActivity extends AppCompatActivity {

    private ActivityImageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String dataText = intent.getStringExtra("image");

        Gson gson = new Gson();
        FolderModel data = gson.fromJson(dataText, FolderModel.class);

        Glide.with(this).load(data.getData()).into(binding.image);
    }
}
