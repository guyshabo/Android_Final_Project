package com.example.final_project;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;

public class Fragment_Favorites extends Fragment {

    private ArrayList<String> favoritesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__favorites, container, false);

        ImageView imgBack3 = view.findViewById(R.id.imgBack3);
        ListView listView = view.findViewById(R.id.listViewFavorites);

        imgBack3.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_fragment_Favorites_to_fragment_Home_Page)
        );

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, favoritesList);
        listView.setAdapter(adapter);

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.getFavoritesNames(favorites -> {
                favoritesList.clear();
                favoritesList.addAll(favorites);
                adapter.notifyDataSetChanged();
            });
        }

        return view;
    }
}
