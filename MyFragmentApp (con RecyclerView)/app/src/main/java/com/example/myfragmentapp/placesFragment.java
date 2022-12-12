package com.example.myfragmentapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfragmentapp.adapter.PlacesRecyclerViewAdapter;
import com.example.myfragmentapp.model.Place;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link placesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class placesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public placesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment placesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static placesFragment newInstance(String param1, String param2) {
        placesFragment fragment = new placesFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Place[] placesArray = new Place[100];
        for (int i=0; i < placesArray.length; i++){
            placesArray[i] = new Place("Place name " + i, "Via con num. civico " + i, "Città " + i);
        }

        RecyclerView placesRecyclerView = view.findViewById(R.id.recyclerview_places);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        placesRecyclerView.setLayoutManager(linearLayoutManager);

        PlacesRecyclerViewAdapter placesRecyclerViewAdapter = new PlacesRecyclerViewAdapter(placesArray);
        placesRecyclerView.setAdapter(placesRecyclerViewAdapter);
    }
}