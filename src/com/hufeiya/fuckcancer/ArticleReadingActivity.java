/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hufeiya.fuckcancer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.hufeiya.javabean.Article;
import com.hufeiya.net.ArticleEntityDownloading;
import com.nineoldandroids.view.ViewHelper;

public class ArticleReadingActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    private ImageView mImageView;
    private Toolbar mToolbarView;
    private View mAnchorView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private Article article;
    private int currentColor;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_reading);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mImageView = (ImageView)findViewById(R.id.image);
        mToolbarView = (Toolbar)findViewById(R.id.toolbar);
        mAnchorView =  findViewById(R.id.anchor);
        webView = (WebView)findViewById(R.id.webview);
        //mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = 180;
        Intent intent = this.getIntent();
        article = (Article)intent.getSerializableExtra("article");
        getSupportActionBar().setTitle(article.getTitle());
        currentColor = intent.getIntExtra("color",0);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, currentColor));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.setWebViewClient(new WebViewClient());
        new ArticleEntityDownloading(article,webView,mImageView,mAnchorView).startDownloadArticle();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        //int baseColor = getResources().getColor(R.color.primary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, currentColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
