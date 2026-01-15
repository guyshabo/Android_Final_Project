package com.example.final_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Add_Recipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Add_Recipe extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Add_Recipe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Add_Recipe.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Add_Recipe newInstance(String param1, String param2) {
        Fragment_Add_Recipe fragment = new Fragment_Add_Recipe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__add__recipe, container, false);

        Button Save = view.findViewById(R.id.Save);
        EditText RecipeName = view.findViewById(R.id.RecipeName);
        EditText Instructions = view.findViewById(R.id.Instructions);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Save.setOnClickListener(v -> {

            String title = RecipeName.getText().toString();
            String instructions = Instructions.getText().toString();

            Recipe recipe = new Recipe(title, instructions, false);

            db.collection("recipes").add(recipe);
        });

        return view;
    }

}