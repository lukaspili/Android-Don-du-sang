package com.siu.android.dondusang.volley;

import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.siu.android.dondusang.model.Center;

import java.util.List;

/**
 * Created by lukas on 8/11/13.
 */
public class CentersRequest extends GsonRequest<List<Center>> {

    public CentersRequest(String url, Response.Listener<List<Center>> listener, Response.ErrorListener errorListener) {
        super(url, new TypeToken<List<Center>>() {
        }.getType(), listener, errorListener);
    }
}
