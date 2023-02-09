// Generated by view binder compiler. Do not edit!
package com.blackbox.pinspot.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.blackbox.pinspot.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FavPinListItemBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView imageviewFavoritePin;

  @NonNull
  public final TextView textviewFavPinLat;

  @NonNull
  public final TextView textviewFavPinLng;

  @NonNull
  public final TextView textviewFavPinTitle;

  private FavPinListItemBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView imageviewFavoritePin, @NonNull TextView textviewFavPinLat,
      @NonNull TextView textviewFavPinLng, @NonNull TextView textviewFavPinTitle) {
    this.rootView = rootView;
    this.imageviewFavoritePin = imageviewFavoritePin;
    this.textviewFavPinLat = textviewFavPinLat;
    this.textviewFavPinLng = textviewFavPinLng;
    this.textviewFavPinTitle = textviewFavPinTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FavPinListItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FavPinListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fav_pin_list_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FavPinListItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imageview_favorite_pin;
      ImageView imageviewFavoritePin = ViewBindings.findChildViewById(rootView, id);
      if (imageviewFavoritePin == null) {
        break missingId;
      }

      id = R.id.textview_fav_pin_lat;
      TextView textviewFavPinLat = ViewBindings.findChildViewById(rootView, id);
      if (textviewFavPinLat == null) {
        break missingId;
      }

      id = R.id.textview_fav_pin_lng;
      TextView textviewFavPinLng = ViewBindings.findChildViewById(rootView, id);
      if (textviewFavPinLng == null) {
        break missingId;
      }

      id = R.id.textview_fav_pin_title;
      TextView textviewFavPinTitle = ViewBindings.findChildViewById(rootView, id);
      if (textviewFavPinTitle == null) {
        break missingId;
      }

      return new FavPinListItemBinding((ConstraintLayout) rootView, imageviewFavoritePin,
          textviewFavPinLat, textviewFavPinLng, textviewFavPinTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}