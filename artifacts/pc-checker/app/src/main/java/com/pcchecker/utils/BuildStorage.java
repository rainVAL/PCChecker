package com.pcchecker.utils;

import android.content.Context;
import com.pcchecker.db.AppDatabase;
import com.pcchecker.db.SavedBuild;
import com.pcchecker.model.PCComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BuildStorage {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface BuildCallback {
        void onLoaded(List<SavedBuild> builds);
    }

    public static void saveBuild(Context context, String name, Map<PCComponent.Category, PCComponent> build) {
        executor.execute(() -> {
            SavedBuild entity = new SavedBuild();
            entity.name = name;
            entity.dateCreated = System.currentTimeMillis();
            
            Map<PCComponent.Category, String> ids = new HashMap<>();
            double total = 0;
            for (Map.Entry<PCComponent.Category, PCComponent> entry : build.entrySet()) {
                ids.put(entry.getKey(), entry.getValue().getId());
                total += entry.getValue().getPricePhp();
            }
            entity.componentIds = ids;
            entity.totalPrice = total;
            
            AppDatabase.getInstance(context).savedBuildDao().insert(entity);
        });
    }

    public static void getSavedBuilds(Context context, BuildCallback callback) {
        executor.execute(() -> {
            List<SavedBuild> builds = AppDatabase.getInstance(context).savedBuildDao().getAll();
            if (callback != null) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> callback.onLoaded(builds));
            }
        });
    }

    public static void deleteBuild(Context context, SavedBuild build) {
        executor.execute(() -> {
            AppDatabase.getInstance(context).savedBuildDao().delete(build);
        });
    }
}
