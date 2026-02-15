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
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Recipe_List extends Fragment {

    private ArrayList<String> recipeNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__recipe__list, container, false);

        ImageView imgBack2 = view.findViewById(R.id.imgBack2);
        ListView listView = view.findViewById(R.id.listViewRecipes);

        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, recipeNames);
        listView.setAdapter(adapter);

        imgBack2.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_fragment_Recipe_List_to_fragment_Home_Page));

        // 1. אתחול ה-Database עם הכתובת המדויקת מה-Console שלך
        mDatabase = FirebaseDatabase.getInstance("https://finalproject-d22b8-default-rtdb.firebaseio.com/")
                .getReference("recipes");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 2. שליפת הנתונים מה-Realtime Database
        mDatabase.orderByChild("userId").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeNames.clear();
                for (DataSnapshot doc : snapshot.getChildren()) {
                    // שליפת שם המתכון מתוך האובייקט
                    String name = doc.child("name").getValue(String.class);
                    if (name != null) {
                        recipeNames.add(name);
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
            bundle.putString("name", recipeNames.get(position));
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_fragment_Recipe_List_to_fragment_Recipe_Details, bundle);
        });

        return view;
    }
}