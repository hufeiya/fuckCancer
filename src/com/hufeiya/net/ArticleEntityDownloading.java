package com.hufeiya.net;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hufeiya.javabean.Article;
import com.hufeiya.javabean.ArticleEntityBean;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by hufeiya on 15-6-30.
 */
public class ArticleEntityDownloading {
    private String httpServerUrl = "http://128.199.97.160:8089/fuckCancer/fuck";
    private Article article;
    private ArticleEntityBean articleEntityBean;
    private WebView body;
    private ImageView image;
    private View anchor;

    public ArticleEntityDownloading(Article article,WebView body,ImageView image,View anchor) {
        this.article = article;
        this.body = body;
        this.image = image;
        this.anchor = anchor;
    }

    public void startDownloadArticle() {
        new DownloadArticleTask().execute();
    }

    private class DownloadArticleTask extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... params) {
            try {
                Gson gson = new Gson();
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(httpServerUrl + "?id="+ article.getId());
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity);
                    articleEntityBean = gson.fromJson(response, ArticleEntityBean.class);
                    return true;
                }else{
                    return false;
                }

            } catch (Exception e) {
                Log.d("fuck", "download Error!");
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {
            String html = "<div style=\"word-break:break-all\">" + articleEntityBean.getDetails() + "</div>";
            body.loadDataWithBaseURL(null,html, "text/html", "utf-8",
                    null);
            new ArticlePic(article, image, anchor,true).startDownloadPic();
            Log.d("fuck", "articleEntity download Done!");
        }
    }
}
