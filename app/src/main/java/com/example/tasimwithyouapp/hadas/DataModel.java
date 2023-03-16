package com.example.tasimwithyouapp.hadas;

interface OnDataModelClick {
    void onClick();
}

public class DataModel {
    String name;
    int id_;
    int image;

    public OnDataModelClick onDataModelClick;




    public DataModel(String name, int id_, int image, OnDataModelClick onDataModelClick) {
        this.name = name;
        this.id_ = id_;
        this.image = image;
        this.onDataModelClick = onDataModelClick;
    }

    public OnDataModelClick getOnDataModelClick() {
        return onDataModelClick;
    }

    public void setOnDataModelClick(OnDataModelClick onDataModelClick) {
        this.onDataModelClick = onDataModelClick;
    }

    public String getName() {
        return name;
    }



    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }
}

