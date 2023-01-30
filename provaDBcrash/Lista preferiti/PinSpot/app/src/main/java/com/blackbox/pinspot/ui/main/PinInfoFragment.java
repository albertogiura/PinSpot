package com.blackbox.pinspot.ui.main;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.databinding.FragmentPinInfoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PinInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinInfoFragment extends Fragment {

    private FragmentPinInfoBinding fragmentPinInfoBinding;

    public PinInfoFragment() {
        // Required empty public constructor
    }

    public static PinInfoFragment newInstance() {
        return new PinInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPinInfoBinding = FragmentPinInfoBinding.inflate(inflater, container, false);
        return fragmentPinInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    Navigation.findNavController(requireView()).navigateUp();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        String pinID = PinInfoFragmentArgs.fromBundle(getArguments()).getPinID();
        if (pinID != null){
            fragmentPinInfoBinding.PinTitleTextView.setText(pinID);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("pins4").document(pinID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Double latitude, longitude;
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            latitude = document.getDouble("lat");
                            longitude = document.getDouble("lon");
                            fragmentPinInfoBinding.PinLatTextView.setText(String.valueOf(latitude));
                            fragmentPinInfoBinding.pinLongTextView.setText(String.valueOf(longitude));
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });



            /*db.collection("pins4")
                    .get(Source.valueOf(pinID))
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            String result = "", title = "", latitude = "", longitude = "";
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    result += document.getData();
                                    title += document.getString("title") + " ";
                                    latitude += document.getString("lat") + " ";
                                    longitude += document.getString("lon") + " ";
                                    //pos += document.get("position");

                                    //firstPinTitleValue.setText(document.get("title").toString());
                                }
                                //fragmentPinInfoBinding.PinTitleTextView.setText(title);
                                fragmentPinInfoBinding.PinLatTextView.setText(latitude);
                                fragmentPinInfoBinding.pinLongTextView.setText(longitude);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });*/
        } else{
            fragmentPinInfoBinding.PinTitleTextView.setText("NULL");
        }
    }
}