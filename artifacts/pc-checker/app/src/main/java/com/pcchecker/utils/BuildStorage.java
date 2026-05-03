package com.pcchecker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pcchecker.data.ComponentDatabase;
import com.pcchecker.model.PCComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BuildStorage {
    private static final String PREF_NAME = "saved_builds";
    private static final String KEY_BUILD_LIST = "build_list";

    public static void saveBuild(Context context, String name, Map<PCComponent.Category, PCComponent> build) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> buildList = prefs.getStringSet(KEY_BUILD_LIST, new HashSet<>());
        Set<String> newBuildList = new HashSet<>(buildList);
        newBuildList.add(name);

        StringBuilder sb = new StringBuilder();
        for (PCComponent component : build.values()) {
            sb.append(component.getId()).append(",");
        }
        
        prefs.edit()
            .putStringSet(KEY_BUILD_LIST, newBuildList)
            .putString("build_" + name, sb.toString())
            .apply();
    }

    public static List<String> getSavedBuildNames(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return new ArrayList<>(prefs.getStringSet(KEY_BUILD_LIST, new HashSet<>()));
    }

    public static List<PCComponent> loadBuild(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String data = prefs.getString("build_" + name, "");
        List<PCComponent> components = new ArrayList<>();
        if (data.isEmpty()) return components;

        String[] ids = data.split(",");
        List<PCComponent> all = ComponentDatabase.getAll();
        for (String id : ids) {
            for (PCComponent c : all) {
                if (c.getId().equals(id)) {
                    components.add(c);
                    break;
                }
            }
        }
        return components;
    }

    public static void deleteBuild(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> buildList = prefs.getStringSet(KEY_BUILD_LIST, new HashSet<>());
        Set<String> newBuildList = new HashSet<>(buildList);
        newBuildList.remove(name);
        
        prefs.edit()
            .putStringSet(KEY_BUILD_LIST, newBuildList)
            .remove("build_" + name)
            .apply();
    }
}
