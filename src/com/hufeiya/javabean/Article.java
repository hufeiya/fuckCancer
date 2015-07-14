package com.hufeiya.javabean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by hufeiya on 15-6-22.
 */
public class Article implements Serializable{
    private transient Drawable imageSource;
    private int id;
    private String title;
    private int type;
    private String image;
    private int columnx;
    private int groups;

    public Article() {

    }

    public Article(String title, String image) {
        this.title = title;
        this.image = image;
    }
    public int getId(){return this.id;}
    public String getImageUri() {
        return this.image;
    }

    public void setImageUri(String imageUri) {
        this.image = imageUri;
    }

    public Drawable getImageSource() {
        return imageSource;
    }

    public void setImageSource(Drawable imageSource) {
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getColumnx() {
        return this.columnx;
    }

    public void setColumnx(int columnx) {
        this.columnx = columnx;
    }

    public int getGroups() {
        return this.groups;
    }

    public void setGroups(int groups) {
        this.groups = groups;
    }

    public void setId(int id) {
        this.id = id;
    }



}
