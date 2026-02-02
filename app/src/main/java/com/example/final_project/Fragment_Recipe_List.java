package com.example.final_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Fragment_Recipe_List extends Fragment {

    private ArrayList<String> recipeNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

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

        String uid = com.google.firebase.auth.FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .getUid();

        FirebaseFirestore.getInstance()
                .collection("recipes")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    recipeNames.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        if (name != null) recipeNames.add(name);
                    }
                    adapter.notifyDataSetChanged();
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
