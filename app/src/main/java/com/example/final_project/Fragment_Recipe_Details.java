package com.example.final_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Recipe_Details extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__recipe__details, container, false);

        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtInstructions = view.findViewById(R.id.txtInstructions);
        Button btnFavorite = view.findViewById(R.id.btnFavorite);
        Button btnRemoveFavorite = view.findViewById(R.id.btnRemoveFavorite);
        Button btnBack = view.findViewById(R.id.btnBack);
        Button btnDeleteRecipe = view.findViewById(R.id.btnDeleteRecipe);

        btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );



        Bundle bundle = getArguments();
        if (bundle == null) return view;

        String recipeName = bundle.getString("name");
        txtName.setText(recipeName);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("recipes")
                .whereEqualTo("name", recipeName)
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(qs -> {
                    for (QueryDocumentSnapshot doc : qs) {
                        String ingredients = doc.getString("ingredients");
                        String instructions = doc.getString("instructions");

                        String text = "";

                        if (ingredients != null && !ingredients.isEmpty()) {
                            text += "Ingredients:\n" + ingredients + "\n\n";
                        }

                        if (instructions != null && !instructions.isEmpty()) {
                            text += "Instructions:\n" + instructions;
                        }

                        txtInstructions.setText(text);
                        break;
                    }
                });

        db.collection("favorites")
                .whereEqualTo("name", recipeName)
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(qs -> {
                    if (qs.isEmpty()) {
                        btnFavorite.setVisibility(View.VISIBLE);
                        btnRemoveFavorite.setVisibility(View.GONE);
                    } else {
                        btnFavorite.setVisibility(View.GONE);
                        btnRemoveFavorite.setVisibility(View.VISIBLE);
                    }
                });

        btnFavorite.setOnClickListener(v -> {
            Map<String, Object> fav = new HashMap<>();
            fav.put("name", recipeName);
            fav.put("userId", uid);

            db.collection("favorites")
                    .add(fav)
                    .addOnSuccessListener(d -> {
                        btnFavorite.setVisibility(View.GONE);
                        btnRemoveFavorite.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    });
        });

        btnRemoveFavorite.setOnClickListener(v -> {
            db.collection("favorites")
                    .whereEqualTo("name", recipeName)
                    .whereEqualTo("userId", uid)
                    .get()
                    .addOnSuccessListener(qs -> {
                        for (QueryDocumentSnapshot doc : qs) {
                            doc.getReference().delete();
                        }
                        btnRemoveFavorite.setVisibility(View.GONE);
                        btnFavorite.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                    });
        });

        btnDeleteRecipe.setOnClickListener(v -> {
            db.collection("recipes")
                    .whereEqualTo("name", recipeName)
                    .whereEqualTo("userId", uid)
                    .get()
                    .addOnSuccessListener(qs -> {
                        for (QueryDocumentSnapshot doc : qs) {
                            doc.getReference().delete();
                        }
                        Toast.makeText(getContext(), "Recipe deleted", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(this).navigateUp();
                    });
        });

        return view;
    }
}
