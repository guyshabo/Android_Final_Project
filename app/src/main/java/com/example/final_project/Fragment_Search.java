package com.example.final_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class Fragment_Search extends Fragment {

    EditText etSearch;
    ListView listView;
    Button btnSearch, btnSearchWeb;
    ImageButton imgBack;
    ArrayList<String> results;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__search, container, false);

        etSearch = view.findViewById(R.id.etSearch);
        listView = view.findViewById(R.id.listResults);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearchWeb = view.findViewById(R.id.btnSearchWeb);
        imgBack = view.findViewById(R.id.imgBack);

        results = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, results);
        listView.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> searchMyRecipes());

        btnSearchWeb.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (query.isEmpty()) return;
            String url = "https://www.google.com/search?q=recipe+" + Uri.encode(query);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        imgBack.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp()
        );

        listView.setOnItemClickListener((parent, v, position, id) -> {
            Bundle b = new Bundle();
            b.putString("name", results.get(position));
            Navigation.findNavController(v)
                    .navigate(R.id.action_fragment_Search_to_fragment_Recipe_Details, b);
        });

        return view;
    }

    private void searchMyRecipes() {
        String text = etSearch.getText().toString().toLowerCase().trim();
        if (text.isEmpty()) return;

        results.clear();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("recipes")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        String ingredients = doc.getString("ingredients");

                        if ((name != null && name.toLowerCase().contains(text)) ||
                                (ingredients != null && ingredients.toLowerCase().contains(text))) {
                            results.add(name);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }


}
