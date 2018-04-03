package com.example.foodspoontacular;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "categoryId",
        childColumns = "categoryId"))

public class DbRecipe {
    @PrimaryKey(autoGenerate = true)
    private int dbRecipeId;

    @ColumnInfo(name = "ingredients")
    private String ingredients;

    @ColumnInfo(name = "instructions")
    private String instructions;

    @ColumnInfo(name = "readyInMinutes")
    private String readyInMinutes;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "categoryId")
    public int categoryId;



    public DbRecipe(String ingredients, String instructions, String readyInMinutes, String title, String image)
    {
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.readyInMinutes = readyInMinutes;
        this.title = title;
        this.image = image;

    }

    public int getDbRecipeId()
    {
        return dbRecipeId;
    }

    public void setDbRecipeId(int dbRecipeId)
    {
        this.dbRecipeId = dbRecipeId ;
    }

    public String getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(String ingredients)
    {
        this.ingredients = ingredients;
    }

    public String getInstructions()
    {
        return instructions;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }

    public String getReadyInMinutes()
    {
        return readyInMinutes;
    }

    public void setReadyInMinutes(String readyInMinutes)
    {
        this.readyInMinutes = readyInMinutes;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public int getCategoryId() { return categoryId; }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId ;
    }

//    public String getCategoryName() { return categoryName; }
//
//    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }


}
