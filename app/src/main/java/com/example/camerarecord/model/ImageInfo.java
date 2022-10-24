package com.example.camerarecord.model;


public class ImageInfo {
    private String id;
    private String date;
    private byte[] image;
    private boolean isSelected;


    public ImageInfo(String id, String date, byte[] image) {
        this.id = id;
        this.image = image;
        this.date = date;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
