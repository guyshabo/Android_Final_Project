package com.example.final_project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class Fragment_Add_Recipe extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;
    private ImageView imagePreview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__add__recipe, container, false);

        EditText etName = view.findViewById(R.id.etRecipeName);
        EditText etInstructions = view.findViewById(R.id.etInstructions);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnChooseImage = view.findViewById(R.id.btnChooseImage);
        imagePreview = view.findViewById(R.id.imagePreview);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // בחירת תמונה
        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // שמירה
        btnSave.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String instructions = etInstructions.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "חובה להזין שם מתכון", Toast.LENGTH_SHORT).show();
                return;
            }

            Recipe recipe;

            if (imageUri != null) {
                // בשלב הזה שומרים רק URI (בהמשך נעלה ל‑Firebase Storage)
                recipe = new Recipe(name, imageUri.toString());
            } else {
                recipe = new Recipe(name, instructions, false);
            }

            db.collection("recipes")
                    .add(recipe)
                    .addOnSuccessListener(doc ->
                            Toast.makeText(getContext(), "המתכון נשמר", Toast.LENGTH_SHORT).show()
                    );
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null) {

            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);
            imagePreview.setVisibility(View.VISIBLE);
        }
    }
}
