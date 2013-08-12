package com.siu.android.dondusang.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.siu.android.dondusang.R;
import com.siu.android.dondusang.dao.model.News;

import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, R.layout.news_row, news);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        if (null == row) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.news_row, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder) row.getTag();

        if (null == viewHolder) {
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }

        News news = getItem(position);

        viewHolder.title.setText(news.getTitle());
//        viewHolder.date.setText(DateUtils.formatAsFull(news.getDate()));

        return row;
    }

    private static class ViewHolder {
        private TextView title;
        private TextView date;

        private ViewHolder(View row) {
            title = (TextView) row.findViewById(R.id.news_row_text);
            date = (TextView) row.findViewById(R.id.news_row_date);
        }
    }
}
