package com.example.storage;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.storage.databinding.ActivityFileBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileActivity extends AppCompatActivity implements onClickItem {
    private ActivityFileBinding binding;
    private FileAdapter adapter;
    private FolderViewModel viewModel;
    private FolderData data;
    private static final int Camera_Permision_Code = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FolderViewModel.class);

        Intent intent = getIntent();
        String dataText = intent.getStringExtra("data");

        Gson gs = new Gson();
        data = gs.fromJson(dataText, FolderData.class);
        adapter = new FileAdapter();

        adapter.setClickItem(this);
        binding.lstImage.setAdapter(adapter);
        initData(data);

        initializeEvent();

    }

    private void initializeEvent() {
        binding.btnBack.setOnClickListener(e -> finish());
        binding.btnOpenCamera.setOnClickListener(e -> openCamera());
    }

    private void initData(FolderData data) {
        List<FolderModel> models = data.getImages();
        for (FolderModel model : models) {
            String fullPath = model.getData();
            String[] paths = fullPath.split("/");
            String pathParent = paths[paths.length - 1];
            model.setTitle(pathParent);
        }
        adapter.setData(models);
    }

    private void addImg(Bitmap bitmap) {
        String getPath = data.getName();
        viewModel.addImage(this, bitmap, getPath);
        initData(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null) return;

        if (requestCode == Camera_Permision_Code) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            addImg(photo);
        }
    }

    private void openCamera() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FileActivity.this, new String[]{Manifest.permission.CAMERA}, Camera_Permision_Code);
            return;
        }
        Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    private void upDateFileName(FolderModel model) {
        String path = model.getData();
        String[] paths = path.split("/");
        String oldFileName = paths[paths.length - 1];
        showInputNewFileName(oldFileName);
    }

    private void showInputNewFileName(String oldFileName){
        final EditText editText = new EditText(this);
        editText.setHint("nhập tên file ");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" nhập tên");
        builder.setView(editText);

        builder.setPositiveButton("update", (dialog, which) -> {
            String newFileName = editText.getText().toString().trim();
            if(newFileName.isEmpty()){
                Toast.makeText(this, "không được để trống", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.upDateFileNameImage(this, oldFileName , newFileName + ".jpg");
            }
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void deleteImage(FolderModel model) {
        String path = model.getData();
        String[] paths = path.split("/");
        String fileName = paths[paths.length - 1];
        viewModel.deleteImage(this, fileName);
    }

    private void showPopupImage(View achor, FolderModel model) {
        PopupMenu popupMenu = new PopupMenu(FileActivity.this, achor);
        popupMenu.getMenuInflater().inflate(R.menu.popup_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(MenuItem -> {
            int id = MenuItem.getItemId();
            if (id == R.id.upDateFileName) {
                upDateFileName(model);
                return true;
            } else {
                deleteImage(model);
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onClick(FolderData data) {
    }

    @Override
    public void onClick(FolderModel model) {
        Gson gson = new Gson();
        String sentData = gson.toJson(model);
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("image", sentData);
        startActivity(intent);
    }

    @Override
    public void onClickOption(FolderModel model, View view) {
        showPopupImage(view, model);
    }

}
