package com.example.final_project;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class Fragment_Search extends Fragment {

    EditText etSearch;
    ListView listView;
    ArrayList<String> results;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__search, container, false);

        etSearch = view.findViewById(R.id.etSearch);
        listView = view.findViewById(R.id.listResults);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        ImageView imgBack = view.findViewById(R.id.imgBack);

        results = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, results);
        listView.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> searchRecipe());

        listView.setOnItemClickListener((parent, v, position, id) -> {
            String recipeName = results.get(position);
            Bundle b = new Bundle();
            b.putString("recipeName", recipeName);
            Navigation.findNavController(v)
                    .navigate(R.id.action_fragment_Search_to_fragment_Recipe_Details, b);
        });

        imgBack.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_fragment_Search_to_fragment_Home_Page)
        );

        return view;
    }

    private void searchRecipe() {
        String text = etSearch.getText().toString();
        results.clear();

        FirebaseDatabase.getInstance()
                .getReference("recipes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot s : snapshot.getChildren()) {
                            Recipe r = s.getValue(Recipe.class);
                            if (r != null && r.getName() != null) {
                                String ingredients = r.ingredients == null ? "" : r.ingredients;
                                if (r.getName().contains(text) || ingredients.contains(text)) {
                                    results.add(r.getName());
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
    }
}
