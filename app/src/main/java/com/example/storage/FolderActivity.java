    package com.example.storage;

    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModel;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.LinearLayoutManager;

    import com.example.storage.databinding.ActivityFolderBinding;
    import com.google.gson.Gson;

    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    public class FolderActivity extends AppCompatActivity implements onClickItem {

        private ActivityFolderBinding binding;
        private FolderAdapter adapter;
        private FolderViewModel viewModel;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityFolderBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            adapter = new FolderAdapter();
            binding.lstFoder.setAdapter(adapter);
            adapter.setOnClickItem(this);

            viewModel = new ViewModelProvider(this).get(FolderViewModel.class);
            initData();

            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_image);
            //viewModel.addImage(this, bitmap);
        }


        private void initData() {
            List<FolderData> data = viewModel.getDataFromExternal(this);
            adapter.setData(data);
        }

        @Override
        public void onClick(FolderData data) {
            Gson gs = new Gson();
            String temp = gs.toJson(data);
            Intent intent = new Intent(this, FileActivity.class);
            intent.putExtra("data", temp);
            startActivity(intent);
        }

        @Override
        public void onClick(FolderModel model) {}

    }
