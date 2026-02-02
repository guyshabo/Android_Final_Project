package com.example.final_project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        imgBack1.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_fragment_Add_Recipe_to_fragment_Home_Page)
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
                Toast.makeText(getContext(), "Name and ingredients are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // בדיקה אם קיים מתכון בשם זהה לאותו משתמש
            db.collection("recipes")
                    .whereEqualTo("userId", uid)
                    .whereEqualTo("name", name)
                    .get()
                    .addOnSuccessListener(qs -> {
                        if (!qs.isEmpty()) {
                            Toast.makeText(getContext(),
                                    "You already have a recipe with this name",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Map<String, Object> recipe = new HashMap<>();
                        recipe.put("name", name);
                        recipe.put("ingredients", ingredients);
                        recipe.put("instructions", instructions);
                        recipe.put("userId", uid);
                        if (imageUri != null) {
                            recipe.put("imageUrl", imageUri.toString());
                        }

                        db.collection("recipes")
                                .add(recipe)
                                .addOnSuccessListener(doc -> {
                                    Toast.makeText(getContext(), "Recipe saved", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(v)
                                            .navigate(R.id.action_fragment_Add_Recipe_to_fragment_Home_Page);
                                });
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
