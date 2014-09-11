package com.siu.android.dondusang.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.siu.android.dondusang.R;
import com.siu.android.dondusang.view.NextBloodEventHistoricElementView;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lukas on 8/20/13.
 */
public class NextBloodEventActivity extends SherlockActivity {

    private Spinner sexeSpinner;
    private Spinner ageSpinner;
    private NextBloodEventHistoricElementView fullBloodElementView;
    private NextBloodEventHistoricElementView plasmaElementView;
    private NextBloodEventHistoricElementView plateletsElementView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new CalendarView(this));


        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, -10);

        CalendarPickerView dialogView = (CalendarPickerView) getLayoutInflater().inflate(R.layout.dialog, null, false);
        dialogView.init(nextYear.getTime(), new Date());
        new AlertDialog.Builder(this)
                .setTitle("I'm a dialog!")
                .setView(dialogView)
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();

//        setContentView(R.layout.next_blood_event_activity);
//
//        sexeSpinner = (Spinner) findViewById(R.id.next_blood_event_sexe_spinner);
//        ageSpinner = (Spinner) findViewById(R.id.next_blood_event_age_spinner);
//        fullBloodElementView = (NextBloodEventHistoricElementView) findViewById(R.id.next_blood_event_historic_full_blood_view);
//        plasmaElementView = (NextBloodEventHistoricElementView) findViewById(R.id.next_blood_event_historic_plasma_view);
//        plateletsElementView = (NextBloodEventHistoricElementView) findViewById(R.id.next_blood_event_historic_platelets_view);
//
//        getSupportActionBar().setTitle(R.string.next_blood_event_activity_title);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        initPersonalInfo();
//        initHistoric();
//
//        sexeSpinner.setSelection(0);
//        ageSpinner.setSelection(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initPersonalInfo() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.next_blood_event_sexe, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexeSpinner.setAdapter(adapter);
        sexeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter = ArrayAdapter.createFromResource(this, R.array.next_blood_event_age, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initHistoric() {
        // titles
        fullBloodElementView.getTitleTextView().setText(R.string.next_blood_event_full_blood);
        plasmaElementView.getTitleTextView().setText(R.string.next_blood_event_plasma);
        plateletsElementView.getTitleTextView().setText(R.string.next_blood_event_platelets);

        // spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.next_blood_event_number_events_full_blood, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fullBloodElementView.getNumberOfEventsSpinner().setAdapter(adapter);
        fullBloodElementView.getNumberOfEventsSpinner().setOnItemSelectedListener(new OnItemSelectedListener(fullBloodElementView));

        adapter = ArrayAdapter.createFromResource(this, R.array.next_blood_event_number_events_plasma, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plasmaElementView.getNumberOfEventsSpinner().setAdapter(adapter);
        plasmaElementView.getNumberOfEventsSpinner().setOnItemSelectedListener(new OnItemSelectedListener(plasmaElementView));

        adapter = ArrayAdapter.createFromResource(this, R.array.next_blood_event_number_events_platelets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plateletsElementView.getNumberOfEventsSpinner().setAdapter(adapter);
        plateletsElementView.getNumberOfEventsSpinner().setOnItemSelectedListener(new OnItemSelectedListener(plateletsElementView));
    }

    private static class OnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        private NextBloodEventHistoricElementView mNextBloodEventHistoricElementView;

        private OnItemSelectedListener(NextBloodEventHistoricElementView nextBloodEventHistoricElementView) {
            mNextBloodEventHistoricElementView = nextBloodEventHistoricElementView;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                mNextBloodEventHistoricElementView.getDateView().setVisibility(View.GONE);
            } else {
                mNextBloodEventHistoricElementView.getDateView().setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
