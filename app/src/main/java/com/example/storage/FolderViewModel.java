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

    public void addImage(Context context, Bitmap bitmap, String path) {

        String en = Environment.getExternalStorageDirectory().getAbsolutePath();
        String pathImage = path.substring(en.length());

        if (pathImage.startsWith("/")) {
            pathImage = pathImage.substring(1);
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "Hello.jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, pathImage);
        // contentValues.put(MediaStore.Images.Media.IS_PENDING, 1);

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (uri != null) {
            try {
                OutputStream outputStream = contentResolver.openOutputStream(uri);
                boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                if (isSuccess) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                }
                outputStream.close();

            } catch (FileNotFoundException e) {
                Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteImage(){

    }

    public void upDateFileNameImage(){

    }

}
