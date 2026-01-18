package com.example.final_project;

public class Recipe {

    private String name;
    private String instructions;
    private String imageUrl; // null אם אין תמונה
    private boolean favorite;

    // חובה ל‑Firebase
    public Recipe() {}

    // מתכון רגיל (טקסט)
    public Recipe(String name, String instructions, boolean favorite) {
        this.name = name;
        this.instructions = instructions;
        this.favorite = favorite;
        this.imageUrl = null;
    }

    // מתכון מתמונה
    public Recipe(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.instructions = "";
        this.favorite = false;
    }

    // getters & setters
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public String getImageUrl() { return imageUrl; }
    public boolean isFavorite() { return favorite; }
}
