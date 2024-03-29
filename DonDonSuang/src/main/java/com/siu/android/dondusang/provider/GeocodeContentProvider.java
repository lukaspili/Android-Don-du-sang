package com.siu.android.dondusang.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import com.siu.android.dondusang.R;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class GeocodeContentProvider extends ContentProvider {

    private Geocoder geocoder;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selectionClause, String[] selectionArgs, String sortOrder) {
        String name = uri.getLastPathSegment().toLowerCase();

        if (StringUtils.isEmpty(name) || name.equals(SearchManager.SUGGEST_URI_PATH_QUERY) || name.length() <= 3) {
            return null;
        }

        if (null == geocoder) {
            geocoder = new Geocoder(getContext(), Locale.FRANCE);
        }

        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(name, 5,
                    (getContext().getResources().getInteger(R.integer.webservice_map_france_swlat) / 1E6),
                    (getContext().getResources().getInteger(R.integer.webservice_map_france_swlon) / 1E6),
                    (getContext().getResources().getInteger(R.integer.webservice_map_france_nelat) / 1E6),
                    (getContext().getResources().getInteger(R.integer.webservice_map_france_nelon) / 1E6));
        } catch (IOException e) {
            Log.w(getClass().getName(), e);
            return null;
        }

        String[] columns = {
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA
        };

        MatrixCursor cursor = new MatrixCursor(columns);

        Set<String> suggestions = new HashSet<String>();

        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);

            if (!address.getCountryCode().equals(Locale.FRANCE.getCountry()) || null == address.getLocality()) {
                continue;
            }

            String location = address.getLocality();

            if (null != address.getAdminArea()) {
                location += " - " + address.getAdminArea();
            }

            if (suggestions.contains(location)) {
                continue;
            }

            suggestions.add(location);

            String[] row = {Integer.toString(i), location, location};

            cursor.addRow(row);
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
