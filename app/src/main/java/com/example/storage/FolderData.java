package com.example.storage;

import java.util.ArrayList;
import java.util.List;

public class FolderData {
    private String name;
    private List<FolderModel> images = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FolderModel> getImages() {
        return images;
    }

    public void setImages(List<FolderModel> images) {
        this.images = images;
    }

    public void addImage(FolderModel image) {
        this.images.add(image);
    }
}
