package com.example.final_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Favorites extends Fragment {

    private ArrayList<String> favoritesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__favorites, container, false);

        ImageView imgBack3 = view.findViewById(R.id.imgBack3);
        ListView listView = view.findViewById(R.id.listViewFavorites);

        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, favoritesList);
        listView.setAdapter(adapter);

        imgBack3.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_fragment_Favorites_to_fragment_Home_Page));

        // 1. אתחול ה-Database עם הכתובת הייחודית שלך
        mDatabase = FirebaseDatabase.getInstance("https://finalproject-d22b8-default-rtdb.firebaseio.com/").getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 2. שליפת רשימת המועדפים מהנתיב: favorites -> {userId}
        mDatabase.child("favorites").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoritesList.clear();
                // אנחנו עוברים על שמות המתכונים שנמצאים תחת המזהה של המשתמש
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String recipeName = ds.getKey(); // המפתח הוא שם המתכון ששמרנו
                    if (recipeName != null) {
                        favoritesList.add(recipeName);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener((parent, v, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", favoritesList.get(position));
            Navigation.findNavController(v)
                    .navigate(R.id.action_fragment_Favorites_to_fragment_Recipe_Details, bundle);
        });

        return view;
    }
}