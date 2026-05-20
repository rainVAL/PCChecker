package com.pcchecker.db;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pcchecker.model.PCComponent;
import java.lang.reflect.Type;
import java.util.Map;

public class Converters {
    @TypeConverter
    public static String fromMap(Map<PCComponent.Category, String> map) {
        if (map == null) return null;
        return new Gson().toJson(map);
    }

    @TypeConverter
    public static Map<PCComponent.Category, String> toMap(String value) {
        if (value == null) return null;
        Type mapType = new TypeToken<Map<PCComponent.Category, String>>() {}.getType();
        return new Gson().fromJson(value, mapType);
    }
}
