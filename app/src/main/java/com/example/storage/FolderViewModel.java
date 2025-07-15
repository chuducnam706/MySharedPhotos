package com.example.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FolderViewModel extends ViewModel {
    private List<FolderModel> data = new ArrayList<>();
    private List<String> folderPath = new ArrayList<>();

    public List<FolderData> getDataFromExternal(Context context) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] titles = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
        };

        Cursor cursor = context.getContentResolver().query(uri, titles, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
        if (cursor != null) {
            int indexColumId = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int indexColuimName = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);

            List<FolderModel> images = new ArrayList<>();

            while (cursor.moveToNext()) {
                String displayData = cursor.getString(indexColumId);
                String displayName = cursor.getString(indexColuimName);

                FolderModel model = new FolderModel();
                model.setData(displayData);
                model.setTitle(displayName);
                images.add(model);
            }

            data.clear();
            data.addAll(images);
        }

        folderPath.clear();
        folderPath.addAll(getParentPath());
        return groupBy();
    }


    private List<String> getParentPath() {
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String path = data.get(i).getData();
            int position = path.lastIndexOf("/");
            String parentPath = path.substring(0, position);
            if (!paths.contains(parentPath)) {
                paths.add(parentPath);
            }
            Log.d("nam", parentPath);
        }
        return paths;
    }


    public List<FolderData> groupBy() {
        List<FolderData> items = new ArrayList<>();
        for (int i = 0; i < folderPath.size(); i++) {
            FolderData item = new FolderData();
            for (int j = 0; j < data.size(); j++) {
                if (data.get(j).getData().contains(folderPath.get(i))) {
                    item.setName(folderPath.get(i));
                    item.addImage(data.get(j));
                }
            }
            items.add(item);

        }
        return items;
    }

}
