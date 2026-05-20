package com.pcchecker.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Map;
import com.pcchecker.model.PCComponent;

@Entity(tableName = "saved_builds")
public class SavedBuild {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String name;
    public String description;
    public long dateCreated;
    public double totalPrice;
    public int performanceBalance;
    
    // Stored as a Map of Category to Component ID
    public Map<PCComponent.Category, String> componentIds;

    public SavedBuild() {}
}
