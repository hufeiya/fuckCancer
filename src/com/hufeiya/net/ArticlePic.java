package com.hufeiya.net;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hufeiya.adapter.ArticleAdapter;
import com.hufeiya.javabean.Article;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by hufeiya on 15-6-22.
 * 1.To get the pictures of each article from the Internet with their own picture uri.
 * 2.save the pictures.
 */
public class ArticlePic {
    private final static String TITLE_PATH
            = Environment.getExternalStorageDirectory() + "/fuckCancer/title_img/";
    private final static String PICTURE_PATH = Environment.getExternalStorageDirectory() + "/fuckCancer/pictures/";
    private List<Article> articles;
    private ArticleAdapter articleAdapter;
    private ImageView image;
    private View anchor;
    private Boolean isDownloadBigPicture = false;
    private Article article;
    private Drawable bigPicture;
    //when download the small pictures,use this.
    public ArticlePic(List<Article> articles, ArticleAdapter articleAdapter) {
        this.articles = articles;
        this.articleAdapter = articleAdapter;
        isDownloadBigPicture = false;
    }
    //when downloading the big picture,use this. Boolean to prevent get the error ArticlePic()
    public ArticlePic(Article article,ImageView image,View anchor,Boolean isDownloadBigPicture){
        this.article = article;
        this.image = image;
        this.anchor = anchor;
        this.isDownloadBigPicture = true;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try{
            if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private Drawable loadImageFromNetwork(Article i ) {
        String imageUrl = i.getImageUri();
        Drawable drawable = null;
        try {
            // see if picture was already saved in SD card.
            drawable = readSavedPic(imageUrl);
            //read from the Internet
            if (drawable == null) {
                int times = 3;
                if(!isDownloadBigPicture){
                    drawable = Drawable.createFromStream(//download the small picture through QiNiu API
                            new URL(imageUrl+"?imageView2/1/w/100/h/100").openStream(), null);
                    saveFile(drawable, i.getImageUri());
                }else{
                    drawable = Drawable.createFromStream(//download the small picture through QiNiu API
                            new URL(imageUrl).openStream(), null);
                    while(drawable == null && times--!=0){//download failed.try again
                        drawable = Drawable.createFromStream(//download the small picture through QiNiu API
                                new URL(imageUrl).openStream(), null);
                        Log.d("fuckInternet", "fuck for " + (3-times) + " times!");
                    }
                    saveFile(drawable,i.getImageUri());
                }

            }

        } catch (Exception e) {
            Log.d("test", e.getMessage());
        }
        if (drawable == null) {
            Log.d("test", "null drawable");
        } else {
            Log.d("test", "not null drawable");
        }

        return drawable;
    }

    private void saveFile(Drawable drawable, String uri) {
        Bitmap bm = drawableToBitmap(drawable);
        if(bm == null){
            return;
        }
        String fileName;
        File dirFile;
        fileName = uri;
        if(!isDownloadBigPicture){
            dirFile = new File(TITLE_PATH);
        }else{
            dirFile = new File(PICTURE_PATH);
        }

        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        //avoid the / is treated to path by android when reading.
        File myCaptureFile = new File(dirFile.getAbsolutePath(), fileName.replace("/","x"));
        if (!myCaptureFile.exists()) {
            try {
                myCaptureFile.createNewFile();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                bos.flush();
                bos.close();
                
            }catch (Exception e){}

        }

    }

    private Drawable readSavedPic(String uri) {
        Drawable drawable;
        String fileName = uri;
        try {
            if(!isDownloadBigPicture){
                drawable = Drawable.createFromPath(TITLE_PATH + fileName.replace("/","x"));
            }else{
                drawable = Drawable.createFromPath(PICTURE_PATH + fileName.replace("/","x"));
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


        return drawable;

    }

    public void startDownloadPic() {
        new DownloadImageTask().execute();
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... params) {
            try {
                if(!isDownloadBigPicture)
                {
                    for (Article i : articles) {
                        if (i.getImageSource() == null) {
                            i.setImageSource(loadImageFromNetwork(i));

                        }

                    }
                }else{
                    bigPicture = loadImageFromNetwork(article);

                }

                return true;
            } catch (Exception e) {
                Log.d("fuck", "download Error!");
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {
            if(!isDownloadBigPicture){
                articleAdapter.notifyDataSetChanged();//update the listview when the pictures are downloaded.


            }else{
                if(bigPicture == null || bigPicture.getIntrinsicWidth() < 10){
                    //Image have some problem,don't display
                }else{
                    image.setImageDrawable(bigPicture);
                }

               // image.setMinimumHeight(bigPicture.getMinimumHeight());

            }

            Log.d("fuck", "download Done!");
        }
    }
}
