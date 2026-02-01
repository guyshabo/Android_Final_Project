package com.example.final_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment_Recipe_Details extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__recipe__details, container, false);

        TextView name = view.findViewById(R.id.txtName);
        TextView instructions = view.findViewById(R.id.txtInstructions);

        Button back = view.findViewById(R.id.btnBack);
        Button favorite = view.findViewById(R.id.btnFavorite);

        Bundle bundle = getArguments();
        String recipeName = bundle.getString("name");
        String recipeInstructions = bundle.getString("instructions");

        name.setText(recipeName);
        instructions.setText(recipeInstructions);

        back.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        favorite.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.addToFavorites(recipeName);
        });

        return view;
    }
}
