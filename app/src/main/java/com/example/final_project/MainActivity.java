package com.example.final_project;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference recipesRef;
    public interface FavoritesCallback {
        void onResult(ArrayList<String> favorites);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        recipesRef = FirebaseDatabase.getInstance().getReference("recipes");
    }

    public void login() {
        EditText emailEt = findViewById(R.id.username);
        EditText passEt = findViewById(R.id.password);

        String email = emailEt.getText().toString().trim();
        String password = passEt.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                        NavController navController =
                                Navigation.findNavController(this, R.id.fragmentContainerView);
                        navController.navigate(R.id.action_fragment_Login_Page_to_fragment_Home_Page);
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void register(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
                        writeToDB(email);
                        NavController navController =
                                Navigation.findNavController(this, R.id.fragmentContainerView);
                        navController.navigate(R.id.action_fragment_Register_to_fragment_Main_Page);
                    } else {
                        Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void writeToDB(String email) {
        DatabaseReference ref =
                FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(email.replace(".", "_"));

        User user = new User(email);
        ref.setValue(user);
    }

    public void getAllRecipes(ValueEventListener listener) {
        recipesRef.addListenerForSingleValueEvent(listener);
    }
    public void addToFavorites(String recipeName) {
        DatabaseReference favoritesRef =
                FirebaseDatabase.getInstance().getReference("favorites");

        favoritesRef.child(recipeName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(MainActivity.this,
                                    "Recipe already in favorites",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            favoritesRef.child(recipeName).setValue(recipeName);
                            Toast.makeText(MainActivity.this,
                                    "Added to favorites",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }


    public void getFavoritesNames(MainActivity.FavoritesCallback callback) {
        ArrayList<String> favorites = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("favorites");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String name = child.getValue(String.class);
                    favorites.add(name);
                }
                callback.onResult(favorites);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



}
