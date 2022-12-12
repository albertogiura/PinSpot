package com.example.myfragmentapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfragmentapp.R;
import com.example.myfragmentapp.model.Place;


public class PlacesRecyclerViewAdapter extends
        RecyclerView.Adapter<PlacesRecyclerViewAdapter.PlaceViewHolder> {

    private Place[] placeArray;

    public PlacesRecyclerViewAdapter(Place[] placeArray){
        this.placeArray = placeArray;
    }

    @NonNull
    @Override
    public PlacesRecyclerViewAdapter.PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_place_list_item,
                parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesRecyclerViewAdapter.PlaceViewHolder holder, int position) {
        holder.bind(placeArray[position]);
    }

    @Override
    public int getItemCount() {
        if (placeArray != null) {
            return placeArray.length;
        }
        return 0;
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewPlaceName;
        private final TextView textViewPlaceAddress;
        private final TextView textViewPlaceCity;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPlaceName = itemView.findViewById(R.id.textview_place_name);
            textViewPlaceAddress = itemView.findViewById(R.id.textview_place_name);
            textViewPlaceCity = itemView.findViewById(R.id.textview_place_city);
        }

        public void bind(Place place){
            textViewPlaceName.setText(place.getPlaceName());
            textViewPlaceAddress.setText(place.getAddress());
            textViewPlaceCity.setText(place.getCity());
        }
    }
}
