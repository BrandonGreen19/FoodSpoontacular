package com.example.foodspoontacular;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Brandon on 2018-03-31.
 */
@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY categoryId")
    List<Category> getAll();

    @Query("Select * FROM category WHERE categoryId = :categoryId")
    Category findCategory(int categoryId);

    @Query("Select * FROM category WHERE name = :name LIMIT 1")
    Category findCategoryByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category... category);

    @Update
    void updateCategory(Category category);

    @Delete
    void delete(Category category);
}
