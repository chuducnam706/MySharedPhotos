package com.example.storage;

import android.view.View;

public interface onClickItem {
    void onClick(FolderData data);
    void onClick(FolderModel model);
    void onClickOption(FolderModel model, View view);
}
