package com.hufeiya.net;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hufeiya.adapter.ArticleAdapter;
import com.hufeiya.javabean.Article;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by hufeiya on 15-6-26.
 * To get the list of articleTitle info.
 */
public class ArticleTitle {
    private List<Article> articles;
    private int id;
    private int type;
    private int columnx;
    private ArticleAdapter articleAdapter;
    private List<Article> temp;
    private Bundle indexBundle;
    private String httpServerUrl = "http://128.199.97.160:8089/fuckCancer/hello";

    public ArticleTitle(int id, int type, int columnx, Bundle indexBundle, ArticleAdapter articleAdapter,
                        List<Article> articles) {
        this.id = id;
        this.type = type;
        this.columnx = columnx;
        this.articleAdapter = articleAdapter;
        this.articles = articles;
        this.indexBundle = indexBundle;
    }

    public void startDownloadArticles() {
        new DownloadArticleTask().execute();
    }

    private class DownloadArticleTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... params) {
            try {
                Gson gson = new Gson();
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(httpServerUrl + "?id=" + id + "&type=" + type + "&columnx=" +
                        columnx + "&groups=" + indexBundle.getInt("index"));
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity);
                    final Type personListType = new TypeToken<List<Article>>() {
                    }.getType();
                    temp = gson.fromJson(response, personListType);
                    Collections.reverse(temp);

                }
                return true;
            } catch (Exception e) {
                Log.d("fuck", "download Error!");
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {
            if(temp != null && temp.size() != 0){
                if(indexBundle.getInt("index") == -99){
                    articles.addAll(0,temp);
                }else{
                    articles.addAll(temp);
                }
                indexBundle.putInt("index",temp.get(temp.size()-1).getGroups()-1);
                indexBundle.putInt("biggestId",temp.get(0).getId());
                articleAdapter.notifyDataSetChanged();//update the listview when the articles are downloaded.
                indexBundle.putBoolean("isLoading",false);//loading complete.
                new ArticlePic(articles, articleAdapter).startDownloadPic();
                Log.d("fuck", "articles download Done!");
            }

            if(indexBundle.getInt("index", 0) == -99) {
                indexBundle.putInt("index",indexBundle.getInt("oldIndex",0));
            }
        }
    }
}
