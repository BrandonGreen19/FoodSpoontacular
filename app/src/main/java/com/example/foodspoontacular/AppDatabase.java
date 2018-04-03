package com.example.foodspoontacular;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {DbRecipe.class, Category.class}, version=4)
public abstract class AppDatabase extends RoomDatabase{
    public abstract DbRecipeDao dbRecipeDao();
    public abstract CategoryDao categoryDao();

    private static AppDatabase databaseInstance;

    public static AppDatabase getDatabaseInstance(Context context)
    {
        if(databaseInstance == null)
        {
            databaseInstance = Room.databaseBuilder(context,AppDatabase.class,"demo-database").fallbackToDestructiveMigration().build();
        }
        return databaseInstance;
    }

    public void destroyInstance()
    {
        databaseInstance = null;
    }
}
