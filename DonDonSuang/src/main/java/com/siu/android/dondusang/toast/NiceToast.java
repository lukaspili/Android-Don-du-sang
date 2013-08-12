package com.siu.android.dondusang.toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lukas on 6/27/13.
 */
public class NiceToast {

    public static Toast makeText(Context context, int text, int duration) {
        return makeText(context, context.getString(text), duration);
    }

    public static Toast makeText(Context context, String text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);

        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setGravity(Gravity.CENTER);

        return toast;
    }
}
