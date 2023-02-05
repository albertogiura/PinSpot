package com.blackbox.pinspot.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.model.Pin;
import com.blackbox.pinspot.util.GlideApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PinRecyclerViewAdapter extends RecyclerView.Adapter<PinRecyclerViewAdapter.PinViewHolder> {

    private LayoutInflater mInflater;
    private final List<Pin> pinList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;
    private static final String DBIMAGES = "gs://pinspot-demo.appspot.com/";
    private FirebaseStorage storage = FirebaseStorage.getInstance(DBIMAGES);

    public PinRecyclerViewAdapter(Context context, List<Pin> pinList, Application application,
                                  OnItemClickListener onItemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.pinList = pinList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onPinItemClick(Pin pin);
        void onFavoriteButtonPressed(int position);
    }

    @NonNull
    @Override
    public PinRecyclerViewAdapter.PinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.fav_pin_list_item,
                parent, false);
        return new PinViewHolder(this, mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PinRecyclerViewAdapter.PinViewHolder holder, int position) {
        holder.bind(pinList.get(position));
    }

    @Override
    public int getItemCount() {
        return pinList.size();
    }

    public class PinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final PinRecyclerViewAdapter mAdapter;
        private final TextView textview_fav_pin_title;
        private final TextView textview_fav_pin_lat;
        private final TextView textview_fav_pin_lng;
        private final ImageView imageView_PinPreviewImage;
        private final ImageView imageview_favorite_pin;

        public PinViewHolder(PinRecyclerViewAdapter mAdapter, @NonNull View itemView) {
            super(itemView);
            this.mAdapter = mAdapter;
            textview_fav_pin_title = itemView.findViewById(R.id.textview_fav_pin_title);
            textview_fav_pin_lat = itemView.findViewById(R.id.textview_fav_pin_lat);
            textview_fav_pin_lng = itemView.findViewById(R.id.textview_fav_pin_lng);
            imageview_favorite_pin = itemView.findViewById(R.id.imageview_favorite_pin);
            imageView_PinPreviewImage = itemView.findViewById(R.id.imageView_PinPreviewImage);
            /*textViewTitle = itemView.findViewById(R.id.textview_title);
            textViewDate = itemView.findViewById(R.id.textview_date);
            imageViewNewsCoverImage = itemView.findViewById(R.id.imageview_news_cover_image);
            imageView_PinPreviewImage = itemView.findViewById(R.id.imageview_favorite_news);*/
            itemView.setOnClickListener(this);
            imageview_favorite_pin.setOnClickListener(this);
        }

        public void bind(Pin pin) {
            textview_fav_pin_title.setText(pin.getTitle());
            textview_fav_pin_lat.setText(String.valueOf(pin.getLat()));
            textview_fav_pin_lng.setText(String.valueOf(pin.getLon()));

            //textViewDate.setText(DateTimeUtil.getDate(news.getDate()));
            //setImageViewFavoriteNews(newsList.get(getAdapterPosition()).isFavorite());

            StorageReference storageReference = storage.getReference().child("pinPhotos/"+pin.getLink()+".jpeg");
            GlideApp.with(imageView_PinPreviewImage.getContext())
                    .load(storageReference)
                    .into(imageView_PinPreviewImage);

            /*Glide.with(application)
                    .load(news.getUrlToImage())
                    .placeholder(R.drawable.ic_baseline_cloud_download_24)
                    .into(imageViewNewsCoverImage);*/
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imageview_favorite_pin) {
                //setImageViewFavoritePin(!pinList.get(getAdapterPosition()).isFavorite());
                onItemClickListener.onFavoriteButtonPressed(getAdapterPosition());
            } else {
                onItemClickListener.onPinItemClick(pinList.get(getAdapterPosition()));
            }
        }
    }
}
