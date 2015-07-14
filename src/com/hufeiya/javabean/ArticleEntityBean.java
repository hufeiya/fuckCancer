package com.hufeiya.javabean;

import java.io.Serializable;

/**
 * Created by hufeiya on 15-6-30.
 */
public class ArticleEntityBean implements Serializable {
    private int id;
    private transient String image;
    private String details;
    private String author;
    public int getId(){
        return this.id;
    }
    public String getImage(){
        return this.image;
    }
    public String getDetails(){
        return this.details;
    }
    public String getAuthor(){
        return this.author;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setImage(String image){
        this.image = image;
    }
    public void setDetails(String details){
        this.details = details;
    }
    public void setAuthor(String author){
        this.author = author;
    }
}
