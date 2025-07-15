package com.example.storage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storage.databinding.ActivityFileBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileActivity extends AppCompatActivity implements onClickItem{
    private ActivityFileBinding binding;
    private FileAdapter adapter;
    private FolderViewModel viewModel;
    private FolderData data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new FolderViewModel();

        Intent intent = getIntent();
        String dataText =  intent.getStringExtra("data");

        Gson gs = new Gson();
        data = gs.fromJson(dataText, FolderData.class);
        adapter = new FileAdapter();

        adapter.setClickItem(this);
        binding.lstImage.setAdapter(adapter);
        initData(data);


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(FolderData data){
        List<FolderModel> models = data.getImages();
        for(FolderModel model : models){
            String fullPath = model.getData();
            String[] paths = fullPath.split("/");
            String pathParent = paths[paths.length - 1];
            model.setTitle(pathParent);
        }
        adapter.setData(models);
    }

    @Override
    public void onClick(FolderData data) {}

    @Override
    public void onClick(FolderModel model) {
        Gson gson = new Gson();
        String sentData = gson.toJson(model);
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("image", sentData);
        startActivity(intent);
    }

}
