package com.example.final_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class Fragment_Recipe_Details extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__recipe__details, container, false);

        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtInstructions = view.findViewById(R.id.txtInstructions);
        Button btnBack = view.findViewById(R.id.btnBack);
        Button btnFavorite = view.findViewById(R.id.btnFavorite);
        Button btnRemoveFavorite = view.findViewById(R.id.btnRemoveFavorite);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            String recipeName = bundle.getString("name");
            String recipeInstructions = bundle.getString("instructions");

            txtName.setText(recipeName);
            txtInstructions.setText(recipeInstructions);


            btnFavorite.setOnClickListener(v -> {
                db.collection("favorites")
                        .whereEqualTo("name", recipeName)
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            if (querySnapshot.isEmpty()) {
                                Map<String, Object> fav = new HashMap<>();
                                fav.put("name", recipeName);

                                db.collection("favorites").add(fav);
                                Toast.makeText(getContext(),
                                        "Added to favorites", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(),
                                        "Recipe already in favorites",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            });


            btnRemoveFavorite.setOnClickListener(v -> {
                db.collection("favorites")
                        .whereEqualTo("name", recipeName)
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            for (var doc : querySnapshot.getDocuments()) {
                                doc.getReference().delete();
                            }
                            Toast.makeText(getContext(),
                                    "Removed from favorites", Toast.LENGTH_SHORT).show();
                        });
            });
        }


        btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(Fragment_Recipe_Details.this)
                        .navigate(R.id.action_fragment_Recipe_Details_to_fragment_Recipe_List)
        );

        return view;
    }
}
