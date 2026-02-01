package com.example.final_project;

public class Recipe {

    public String id;          // Firebase
    public String name;
    public String ingredients;
    public String instructions;
    public String imageUrl;
    public boolean favorite;

    // חובה ל‑Firebase
    public Recipe() {}

    // בלי תמונה
    public Recipe(String name, String ingredients, String instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = null;
        this.favorite = false;
    }

    // עם תמונה
    public Recipe(String name, String ingredients, String instructions, String imageUrl) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.favorite = false;
    }

    // getters
    public String getName() { return name; }
    public String getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    public String getImageUrl() { return imageUrl; }
    public boolean isFavorite() { return favorite; }
}
