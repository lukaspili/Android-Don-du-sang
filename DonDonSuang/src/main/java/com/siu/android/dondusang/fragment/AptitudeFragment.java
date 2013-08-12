package com.siu.android.dondusang.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.siu.android.dondusang.R;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AptitudeFragment extends Fragment {

    private TextView titleTextView;
    private TextView subTitleTextView;
    private TextView contentTextView;
    private ImageView imageView;
    private Button yesButton;
    private Button noButton;
    private Button uniqueButton;

    private int question = 0;
    private boolean failed;

    private String[] questions;
    private String[] failures;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);

        questions = getResources().getStringArray(R.array.questions_questions);
        failures = getResources().getStringArray(R.array.questions_failures);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.questions_tab_fragment, viewGroup, false);

        titleTextView = (TextView) view.findViewById(R.id.questions_tab_title);
        subTitleTextView = (TextView) view.findViewById(R.id.questions_tab_subtitle);
        contentTextView = (TextView) view.findViewById(R.id.questions_tab_content);
        imageView = (ImageView) view.findViewById(R.id.questions_tab_image);
        yesButton = (Button) view.findViewById(R.id.questions_tab_button_yes);
        noButton = (Button) view.findViewById(R.id.questions_tab_button_no);
        uniqueButton = (Button) view.findViewById(R.id.questions_tab_button_unique);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question++;

                if (question >= 10) {
                    loadSuccess();
                } else {
                    loadQuestion();
                }
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFailure();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (question == 0) {
            loadBegin();
        } else if (question == 10) {
            loadSuccess();
        } else {
            if (failed) {
                loadFailure();
            } else {
                loadQuestion();
            }
        }
    }

    private void loadBegin() {
        subTitleTextView.setText(R.string.questions_tab_begin_subtitle);
        contentTextView.setText(R.string.questions_tab_begin_rules);

        showUniqueButton(R.string.questions_tab_begin_button, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question++;
                hideUniqueButton();
                loadQuestion();
            }
        });
    }

    private void loadQuestion() {
        subTitleTextView.setText(String.format(getString(R.string.questions_tab_subtitle), question));
        contentTextView.setText(questions[question - 1]);
    }

    private void loadSuccess() {
        showImage(R.drawable.im_valid);
        contentTextView.setText(R.string.questions_tab_success);

        showUniqueButton(R.string.questions_tab_restart, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart();
            }
        });
    }

    private void loadFailure() {
        failed = true;
        showImage(R.drawable.im_deny);
        contentTextView.setText(failures[question - 1]);

        showUniqueButton(R.string.questions_tab_restart, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                failed = false;
                restart();
            }
        });
    }

    private void showImage(int resource) {
        titleTextView.setVisibility(View.GONE);
        subTitleTextView.setVisibility(View.GONE);

        imageView.setImageResource(resource);
        imageView.setVisibility(View.VISIBLE);
    }

    private void hideImage() {
        imageView.setVisibility(View.GONE);
        titleTextView.setVisibility(View.VISIBLE);
        subTitleTextView.setVisibility(View.VISIBLE);
    }

    private void showUniqueButton(int text, View.OnClickListener listener) {
        yesButton.setVisibility(View.GONE);
        noButton.setVisibility(View.GONE);

        uniqueButton.setText(text);
        uniqueButton.setOnClickListener(listener);
        uniqueButton.setVisibility(View.VISIBLE);
    }

    private void hideUniqueButton() {
        uniqueButton.setVisibility(View.GONE);
        yesButton.setVisibility(View.VISIBLE);
        noButton.setVisibility(View.VISIBLE);
    }

    private void restart() {
        question = 1;
        hideImage();
        hideUniqueButton();
        loadQuestion();
    }
}
