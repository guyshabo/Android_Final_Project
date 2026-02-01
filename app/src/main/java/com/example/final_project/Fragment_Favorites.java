package com.example.final_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Fragment_Favorites extends Fragment {

    private ArrayList<String> favoritesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__favorites, container, false);

        ImageView imgBack3 = view.findViewById(R.id.imgBack3);
        ListView listView = view.findViewById(R.id.listViewFavorites);

        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                favoritesList
        );
        listView.setAdapter(adapter);

        imgBack3.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_fragment_Favorites_to_fragment_Home_Page)
        );


        FirebaseFirestore.getInstance()
                .collection("favorites")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    favoritesList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        if (name != null) {
                            favoritesList.add(name);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });


        return view;
    }
}
