package com.siu.android.dondusang.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.siu.android.dondusang.model.Center;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class CenterDeserializer implements JsonDeserializer<Center> {

    /* Commons */
    private static final String CITY = "ville";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";
    private static final String DESCRIPTION = "text";

    /* Permanent */
    private static final String PHONE = "num_tel";
    private static final String EMAIL = "adr_mail";
    private static final String REGION = "region_name";

    /* Temporal */
    private static final String DATE = "c_date";
    private static final String STARTHOUR = "c_hdebutaprem";
    private static final String ENDHOUR = "c_hfin";
    private static final String TYPE = "c_type";

    @Override
    public Center deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Center center = new Center();
        center.setCity(CommonDeserializer.getString(jsonObject.get(CITY)));
        center.setLatitude(CommonDeserializer.getDouble(jsonObject.get(LATITUDE)));
        center.setLongitude(CommonDeserializer.getDouble(jsonObject.get(LONGITUDE)));
        center.setDescription(CommonDeserializer.getString(jsonObject.get(DESCRIPTION)));
        center.setPhone(CommonDeserializer.getString(jsonObject.get(PHONE)));
        center.setEmail(CommonDeserializer.getString(jsonObject.get(EMAIL)));
        center.setRegion(CommonDeserializer.getString(jsonObject.get(REGION)));
        center.setType(CommonDeserializer.getString(jsonObject.get(TYPE)));

        if (center.isTemporal()) {
            center.setDate(new Date(CommonDeserializer.getLong(jsonObject.get(DATE)) * 1000));
        }

        return center;
    }
}
