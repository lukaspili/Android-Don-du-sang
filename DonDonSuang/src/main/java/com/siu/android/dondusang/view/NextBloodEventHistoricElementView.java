package com.siu.android.dondusang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.siu.android.dondusang.R;

/**
 * Created by lukas on 8/20/13.
 */
public class NextBloodEventHistoricElementView extends LinearLayout {

    private TextView mTitleTextView;
    private Spinner mNumberOfEventsSpinner;
    private View dateView;

    public NextBloodEventHistoricElementView(Context context) {
        super(context);
        init(context);
    }

    public NextBloodEventHistoricElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NextBloodEventHistoricElementView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.next_blood_event_historic_element_view, this);
        mTitleTextView = (TextView) view.findViewById(R.id.next_blood_event_historic_element_title);
        mNumberOfEventsSpinner = (Spinner) view.findViewById(R.id.next_blood_event_historic_element_number_events_spinner);
        dateView = view.findViewById(R.id.next_blood_event_historic_element_date_view);

    }

    private void update(String title) {
        mTitleTextView.setText(title);

        invalidate();
        requestLayout();
    }



    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public Spinner getNumberOfEventsSpinner() {
        return mNumberOfEventsSpinner;
    }

    public View getDateView() {
        return dateView;
    }
}
