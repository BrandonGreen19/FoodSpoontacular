package com.example.foodspoontacular;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DbRecipeDao
{
    @Query("SELECT * FROM dbRecipe")
    List<DbRecipe> getAll();

    @Query("SELECT dbRecipe.dbRecipeId, dbRecipe.instructions, dbRecipe.readyInMinutes, dbRecipe.title, dbRecipe.image, dbRecipe.categoryId, category.name as categoryName FROM dbrecipe "
            + "INNER JOIN category ON category.categoryId = dbrecipe.categoryId ")
    List<DbRecipe> getRecipesWithCategory();


    @Query("Select * FROM dbRecipe WHERE dbRecipeId = :dbRecipeId")
    DbRecipe findRecipe(int dbRecipeId);

    @Insert
    void insertAll(DbRecipe... dbRecipes);

    @Update
    void updateRecipe(DbRecipe dbRecipe);

    @Delete
    void delete(DbRecipe dbRecipe);
}