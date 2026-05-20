package com.pcchecker.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface SavedBuildDao {
    @Query("SELECT * FROM saved_builds ORDER BY dateCreated DESC")
    List<SavedBuild> getAll();

    @Query("SELECT * FROM saved_builds WHERE id = :id")
    SavedBuild getById(int id);

    @Insert
    long insert(SavedBuild build);

    @Update
    void update(SavedBuild build);

    @Delete
    void delete(SavedBuild build);
}
