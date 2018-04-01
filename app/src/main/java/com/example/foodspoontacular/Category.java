package com.example.foodspoontacular;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Brandon on 2018-03-31.
 */
@Entity
public class Category {


    @PrimaryKey(autoGenerate = true)
    private int categoryId;

    @ColumnInfo(name = "name")
    private String name;

    public Category(String name)
    {
        this.name = name;
    }

    public int getCategoryId() { return categoryId; }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId ;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
