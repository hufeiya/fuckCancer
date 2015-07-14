package com.hufeiya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hufeiya.fuckcancer.R;
import com.hufeiya.javabean.Article;

import java.util.List;

/**
 * Created by hufeiya on 15-6-22.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {
    private int resourceId;

    public ArticleAdapter(Context context, int textViewResourceId, List<Article> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.article_image);
            viewHolder.textView = (TextView) view.findViewById(R.id.article_title);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.imageView.setImageDrawable(article.getImageSource());
        viewHolder.textView.setText(article.getTitle());
        return view;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
