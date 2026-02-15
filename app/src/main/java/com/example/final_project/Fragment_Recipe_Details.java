package com.example.final_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Recipe_Details extends Fragment {

    private DatabaseReference mDatabase;
    private String uid;

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

        // אתחול ה-Database עם הכתובת שלך
        mDatabase = FirebaseDatabase.getInstance("https://finalproject-d22b8-default-rtdb.firebaseio.com/").getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 1. שליפת פרטי המתכון מה-Realtime Database
        mDatabase.child("recipes").orderByChild("name").equalTo(recipeName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot doc : snapshot.getChildren()) {
                            Recipe recipe = doc.getValue(Recipe.class);
                            if (recipe != null && recipe.userId.equals(uid)) {
                                String text = "";
                                if (recipe.ingredients != null && !recipe.ingredients.isEmpty()) {
                                    text += "Ingredients:\n" + recipe.ingredients + "\n\n";
                                }
                                if (recipe.instructions != null && !recipe.instructions.isEmpty()) {
                                    text += "Instructions:\n" + recipe.instructions;
                                }
                                txtInstructions.setText(text);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        // 2. בדיקה אם המתכון במועדפים (לפי UID של המשתמש)
        DatabaseReference favRef = mDatabase.child("favorites").child(uid).child(recipeName);
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    btnFavorite.setVisibility(View.GONE);
                    btnRemoveFavorite.setVisibility(View.VISIBLE);
                } else {
                    btnFavorite.setVisibility(View.VISIBLE);
                    btnRemoveFavorite.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 3. הוספה למועדפים
        btnFavorite.setOnClickListener(v -> {
            favRef.setValue(true).addOnSuccessListener(aVoid -> {
                Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
            });
        });

        // 4. הסרה מהמועדפים
        btnRemoveFavorite.setOnClickListener(v -> {
            favRef.removeValue().addOnSuccessListener(aVoid -> {
                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            });
        });

        // 5. מחיקת מתכון
        btnDeleteRecipe.setOnClickListener(v -> {
            mDatabase.child("recipes").orderByChild("name").equalTo(recipeName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot doc : snapshot.getChildren()) {
                                Recipe r = doc.getValue(Recipe.class);
                                if (r != null && r.userId.equals(uid)) {
                                    doc.getRef().removeValue(); // מחיקה מה-DB
                                }
                            }
                            Toast.makeText(getContext(), "Recipe deleted", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(Fragment_Recipe_Details.this).navigateUp();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
        });

        return view;
    }
}