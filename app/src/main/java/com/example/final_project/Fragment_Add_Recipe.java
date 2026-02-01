package com.example.final_project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_Add_Recipe extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imagePreview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__add__recipe, container, false);

        EditText etName = view.findViewById(R.id.etRecipeName);
        EditText etIngredients = view.findViewById(R.id.etIngredients);
        EditText etInstructions = view.findViewById(R.id.etInstructions);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnChooseImage = view.findViewById(R.id.btnChooseImage);
        ImageView imgBack1 = view.findViewById(R.id.imgBack1);
        imagePreview = view.findViewById(R.id.imagePreview);

        DatabaseReference recipesRef =
                FirebaseDatabase.getInstance().getReference("recipes");

        imgBack1.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_fragment_Add_Recipe_to_fragment_Home_Page));

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String ingredients = etIngredients.getText().toString();
            String instructions = etInstructions.getText().toString();

            if (name.isEmpty() || ingredients.isEmpty()) {
                Toast.makeText(getContext(), "Name And Ingredients Are Required.", Toast.LENGTH_SHORT).show();
                return;
            }

            Recipe recipe;
            if (imageUri != null) {
                recipe = new Recipe(name, ingredients, instructions, imageUri.toString());
            } else {
                recipe = new Recipe(name, ingredients, instructions);
            }

            String id = recipesRef.push().getKey();
            recipe.id = id;
            recipesRef.child(id).setValue(recipe);

            Toast.makeText(getContext(), "Recipe Saved", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);
            imagePreview.setVisibility(View.VISIBLE);
        }
    }
}
