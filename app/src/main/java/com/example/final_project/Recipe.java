package com.example.final_project;

public class Recipe {

    public String id;
    public String name;
    public String ingredients;
    public String instructions;
    public String imageUrl;

    public Recipe() {
    }

    public Recipe(String name, String ingredients, String instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Recipe(String name, String ingredients, String instructions, String imageUrl) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
    }
}
