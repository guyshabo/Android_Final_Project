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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Add_Recipe extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imagePreview;
    private DatabaseReference mDatabase;

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


        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        imgBack1.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_fragment_Add_Recipe_to_fragment_Home_Page)
        );

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String ingredients = etIngredients.getText().toString().trim();
            String instructions = etInstructions.getText().toString().trim();

            if (name.isEmpty() || ingredients.isEmpty()) {
                Toast.makeText(requireContext(), "Name and ingredients are required", Toast.LENGTH_SHORT).show();
                return;
            }

            mDatabase.child("recipes").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean exists = false;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Recipe r = ds.getValue(Recipe.class);
                                if (r != null && r.name != null && r.name.equalsIgnoreCase(name)) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (exists) {
                                Toast.makeText(requireContext(), "You already have a recipe with this name", Toast.LENGTH_SHORT).show();
                            } else {

                                String recipeId = mDatabase.child("recipes").push().getKey();
                                Recipe newRecipe = new Recipe(name, ingredients, instructions,
                                        imageUri != null ? imageUri.toString() : "", uid);
                                newRecipe.id = recipeId;

                                if (recipeId != null) {
                                    mDatabase.child("recipes").child(recipeId).setValue(newRecipe)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(requireContext(), "Recipe saved", Toast.LENGTH_SHORT).show();
                                                Navigation.findNavController(v).navigate(R.id.action_fragment_Add_Recipe_to_fragment_Home_Page);
                                            })
                                            .addOnFailureListener(e ->
                                                    Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(requireContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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