package com.siu.android.dondusang.gson;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.siu.android.dondusang.model.Center;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.piliszczuk AT zenika.com>
 */
public class CentersListDeserializer implements JsonDeserializer<List<Center>> {

    @Override
    public List<Center> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Center> centers = new ArrayList<Center>();
        JsonObject object = json.getAsJsonObject();

        int results;
        try {
            results = object.get("num_results").getAsInt();
        } catch (Exception e) {
            Log.e(getClass().getName(), "Cannot get results from json", e);
            return null;
        }

        Center center;
        for (int i = 0; i < results; i++) {
            try {
                center = context.deserialize(object.get(String.valueOf(i)), Center.class);
            } catch (Exception e) {
                Log.e(getClass().getName(), "Error in center parsing", e);
                continue;
            }

//            // ignore temporal centers at same coords because the list is already ordered by date
//            if (centers.contains(center)) {
//                continue;
//            }

            centers.add(center);
        }

        return centers;

//            if (!center.isTyped()) {
//                Log.w(getClass().getName(), "Center is not typed " + center);
//                continue;
//            }
//
        // center list already contains existing center at same coords
//            if (centers.contains(center)) {
//
//                Center existingCenter = centers.get(centers.lastIndexOf(center));
//
//                // keep existing center and ignore the new one if :
//                // - existingCenter is permanent, then the new center should not be permanent too
//                // - center is not permanent
//                // - center date is not older than existing center
//                // - center start hour is not older than existing center start hour
//                if (existingCenter.isPermanent() || !center.isPermanent() || center.getDate().isAfter(existingCenter.getDate()) ||
//                        (center.getDate().equals(existingCenter.getDate()) && center.getStartHour().isAfter(existingCenter.getStartHour()))) {
//                    continue;
//                }
//
//                centers.remove(existingCenter);
//    }
    }

}
