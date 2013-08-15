package com.siu.android.dondusang.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.siu.android.dondusang.gson.CenterDeserializer;
import com.siu.android.dondusang.gson.CentersListDeserializer;
import com.siu.android.dondusang.model.Center;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by Gson.
 */
public class GsonRequest<T> extends Request<T> {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<List<Center>>() {
            }.getType(), new CentersListDeserializer())
            .registerTypeAdapter(Center.class, new CenterDeserializer())
            .create();

    private Class<T> mClass;
    private Type mType;
    private Listener<T> mListener;

    public GsonRequest(String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(url, clazz, null, listener, errorListener);
    }

    public GsonRequest(String url, Type type, Listener<T> listener, ErrorListener errorListener) {
        this(url, null, type, listener, errorListener);
    }

    public GsonRequest(String url, Class<T> clazz, Type type, Listener<T> listener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mClass = clazz;
        mType = type;
        mListener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, "UTF-8");
            T result = (mClass != null) ? gson.fromJson(json, mClass) : (T) gson.fromJson(json, mType);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}