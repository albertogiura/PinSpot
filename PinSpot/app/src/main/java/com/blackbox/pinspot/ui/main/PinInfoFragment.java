package com.blackbox.pinspot.ui.main;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.Toast;

import com.blackbox.pinspot.R;
import com.blackbox.pinspot.databinding.FragmentPinInfoBinding;
import com.blackbox.pinspot.model.Example;
import com.blackbox.pinspot.model.Main;
import com.blackbox.pinspot.model.Weather;
import com.blackbox.pinspot.model.weatherapi;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PinInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinInfoFragment extends Fragment {

    String url = "api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}";
    String apikey = "4f6ec18ab9eb724adb869edca9cbbf63";
    LocationManager manager;
    LocationListener locationListener;
    Double latitude, longitude;
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
            fragmentPinInfoBinding.loginText.setText(pinID);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection("pins4").document(pinID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            latitude = document.getDouble("lat");
                            longitude = document.getDouble("lon");
                            //fragmentPinInfoBinding.pinLatTextView.setText(String.valueOf(latitude));
                            //fragmentPinInfoBinding.pinLongTextView.setText(String.valueOf(longitude));
                            Retrofit retrofit=new Retrofit.Builder()
                                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            weatherapi myapi=retrofit.create(weatherapi.class);
                            Call<Example> examplecall = myapi.getweather(Double.parseDouble(String.valueOf(latitude)),Double.parseDouble(String.valueOf(longitude)), apikey);
                            examplecall.enqueue(new Callback<Example>() {
                                @Override
                                public void onResponse(Call<Example> call, retrofit2.Response<Example> response) {
                                     if (response.code() == 404) {
                                        Toast.makeText(PinInfoFragment.newInstance().requireContext(), "Please Enter a valid City", Toast.LENGTH_LONG).show();
                                    } else if (!(response.isSuccessful())) {
                                        Toast.makeText(PinInfoFragment.newInstance().requireContext(), response.code() + " ", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Example mydata = response.body();

                                    Main main = mydata.getMain();
                                    Weather[] weather = mydata.getWeather();


                                    Double temp = main.getTemp();

                                    String description = weather[0].getDescription();
                                    String description2 = weather[0].getMain();

                                    Integer temperature = (int) (temp - 273.15);
                                    Context context = getActivity();
                                    SharedPreferences sharedPref = context.getSharedPreferences(
                                            "settings", Context.MODE_PRIVATE); //DAMETTEREINUNACOSTANTE
                                    Boolean celsiusSettings = sharedPref.getBoolean("celsius", true);
                                    if(celsiusSettings == true){
                                        fragmentPinInfoBinding.temperatura.setText(String.valueOf(temperature) + " °C");
                                    }else{
                                        fragmentPinInfoBinding.temperatura.setText(String.valueOf(celsToFar(temperature)) + " °F");
                                    }

                                    fragmentPinInfoBinding.meteo.setText(description);
                                    // descrizione2.setText(description2);
                                }

                                @Override
                                public void onFailure(Call<Example> call, Throwable t) {
                                    Toast.makeText(PinInfoFragment.newInstance().requireContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                                }

                            });
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
            fragmentPinInfoBinding.loginText.setText("NULL");
        }
    }
    int celsToFar(int c){
        return (int) ((c * 1.8) + 32);
    }
}