/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hufeiya.fuckcancer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hufeiya.adapter.ArticleAdapter;
import com.hufeiya.database.MydatabaseHelper;
import com.hufeiya.javabean.Article;
import com.hufeiya.net.ArticleTitle;
import com.umeng.analytics.MobclickAgent;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SuperAwesomeCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    @InjectView(com.hufeiya.fuckcancer.R.id.list_view)
    ListView listView;
    private List<Article> articles = new ArrayList<Article>();
    private ArticleAdapter articleAdapter;
    private int position;
    private MydatabaseHelper dbHelper;
    private View footer;            //the footer of list view
    private int lastitem = 0; //the lastitem can be seen on the listview
    private AbsListView.OnScrollListener scrollListener;
    private Bundle indexBundle;//the current Sub page ,-88 means the start
    @InjectView(R.id.pull_to_refresh)
    PullToRefreshView mPullToRefreshView;
    private int REFRESH_DELAY = 1000;

    public static SuperAwesomeCardFragment newInstance(int position) {
        SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        Log.d("frag", position + "onCreate is exe!!!");
        dbHelper = new MydatabaseHelper(getActivity(), "fuckCancer.db", null, 1);
        dbHelper.getWritableDatabase();
        footer = getLayoutInflater(null).inflate(R.layout.footer, null);
        indexBundle = new Bundle();
        indexBundle.putInt("index",-88);
        scrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int state) {
                if (lastitem == articleAdapter.getCount() && state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (indexBundle.getInt(("index")) < 0 || articles.isEmpty()) {
                        listView.removeFooterView(footer);
                    } else {
                        //indexBundle.putInt("index",indexBundle.getInt("index")-1);
                        Log.i("msg", "index=" + indexBundle.getInt("index"));
                        initArticle(0,indexBundle);
                        if (indexBundle.getInt("index") <= 0 || articles.isEmpty()) {
                            listView.removeFooterView(footer);
                        }
                    }
                    Log.i("msg", "onScrollStateChanged--lastitem" + lastitem);
                    Log.i("msg", "onScrollStateChanged--adapter.getCount" + articleAdapter.getCount());
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastitem = firstVisibleItem + visibleItemCount - 1;
                Log.i("msg", "lastitem=" + lastitem);
            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.hufeiya.fuckcancer.R.layout.fragment_card, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        if(indexBundle.getInt("biggestId",0) != 0){
                            if(indexBundle.getInt("biggestIdAlreadyExe",0) != 0 && //already refrashing
                                    indexBundle.getInt("biggestId",0) <= indexBundle.getInt("biggestIdAlreadyExe",0)){
                                return;
                            }
                            indexBundle.putInt("biggestIdAlreadyExe", indexBundle.getInt("biggestId",0));
                            indexBundle.putInt("oldIndex",indexBundle.getInt("index",0));
                            indexBundle.putInt("index",-99);
                            initArticle(indexBundle.getInt("biggestId",0),indexBundle);
                        }
                    }
                }, REFRESH_DELAY);
            }
        });




    Log.d("frag", position + "onCreateView is exe!!!");
        articleAdapter = new ArticleAdapter(getActivity(), R.layout.article_item, articles);
        listView.setOnScrollListener(scrollListener);
        listView.addFooterView(footer);
        if (articles.isEmpty()) {
            initArticle(0,indexBundle);

        }
        listView.setAdapter(articleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = articles.get(position);
                Intent intent = new Intent(getActivity(), ArticleReadingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("article", article);
                intent.putExtra("color",((MainActivity)getActivity()).getCurrentColor());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Fragment " + position);
        Log.d("frag", position + "onPause is exe!!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Fragment " + position);
        Log.d("frag", position + "onResume is exe!!!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("frag", position + "onDestroyView is exe!!!");
        //ViewGroup.MarginLayoutParams fuck  = (ViewGroup.MarginLayoutParams)refreshableView.getLayoutParams();
       // fuck.topMargin = 0;
        //descriptionTextView.setLayoutParams(fuck);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("frag", position + "onDestroy is exe!!!");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("frag", position + " onSaveInstanceState is exe!!!");
    }

    // test the Internet connection
    private void initArticle(int id,Bundle indexBundle) {
        SharedPreferences shpf = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        new ArticleTitle(id, shpf.getInt("type",0), position, indexBundle, articleAdapter, articles).startDownloadArticles();

    }
}