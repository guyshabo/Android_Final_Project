package com.example.final_project;

public class Recipe {
    private String id;
    private String title;
    private String instructions;
    private boolean isFavorite;

    // חובה ל-Firebase (קונסטרקטור ריק)
    public Recipe() {
    }

    // קונסטרקטור רגיל
    public Recipe(String title, String instructions, boolean isFavorite) {
        this.title = title;
        this.instructions = instructions;
        this.isFavorite = isFavorite;
    }

    // Getters ו-Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
