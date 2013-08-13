package com.siu.android.dondusang.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.siu.android.dondusang.R;
import com.siu.android.dondusang.model.Center;

import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class CenterAdapter extends ArrayAdapter<Center> {

    private List<Center> centers;

    public CenterAdapter(Context context, List<Center> centers) {
        super(context, R.layout.center_row, centers);
        this.centers = centers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;

        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.center_row, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder) row.getTag();

        if (null == viewHolder) {
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }

        Center center = getItem(position);

        viewHolder.title.setText(center.getTitle());
        viewHolder.description.setText(center.getSubtitle());

        return row;
    }

    @Override
    public Center getItem(int position) {
        return centers.get(position);
    }

    private class ViewHolder {

        private TextView title;
        private TextView description;

        private ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.center_row_text);
            description = (TextView) view.findViewById(R.id.center_row_description);
        }
    }
}
