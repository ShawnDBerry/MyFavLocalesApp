package com.example.android.myfavlocalesapp.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android.myfavlocalesapp.R;
import com.example.android.myfavlocalesapp.model.Location;
import com.example.android.myfavlocalesapp.model.PlacesResponse;
import com.example.android.myfavlocalesapp.model.Result;
import com.example.android.myfavlocalesapp.model.User;
import com.example.android.myfavlocalesapp.network.PlacesRetrofit;
import com.example.android.myfavlocalesapp.viewmodel.LocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.gson.Gson;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoginFragment.LoginDelegator {

    private GoogleMap mMap;
    public static final int REQUEST_CODE = 205;
    private Observer<Location> myObserver;
    private PlacesRetrofit placesRetrofit;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private LocationViewModel locationViewModel;
    private LoginFragment loginFragment = new LoginFragment();

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @BindView(R.id.menu_icon)
    ImageView menuImageView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        setUpLocation();
        checkIfUserLoggedIn();


    }

    private void setUpMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = new SupportMapFragment();
        mapFragment.getMapAsync(this);
    }

    private void checkIfUserLoggedIn() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, "User needs to be logged in", Toast.LENGTH_LONG).show();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_framelayout, loginFragment)
                    .commit();

        } else {
            getLocationMap();
            Toast.makeText(this, "User: " + firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        }
    }

    private void getLocationMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
    }

    @Override
    public void signUpNewUser(User signUpUser) {
        firebaseAuth.createUserWithEmailAndPassword(signUpUser.getEmailAddress(),
                signUpUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(signUpUser.getUserName()).build();

                    firebaseAuth.getCurrentUser().updateProfile(profileUpdates);

                    Toast.makeText(getApplicationContext(),
                            firebaseAuth.getCurrentUser().getDisplayName() + " successfully created!",
                            Toast.LENGTH_SHORT).show();
                    removeLoginFragment();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "what had happened was " + task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeLoginFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(loginFragment)
                .commit();
        getLocationMap();
    }

    @Override
    public void loginUser(User loginUser) {
        firebaseAuth.signInWithEmailAndPassword(
                loginUser.getEmailAddress(), loginUser.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            removeLoginFragment();

                            Toast.makeText(getApplicationContext(),
                                    firebaseAuth.getCurrentUser().getDisplayName(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("TAG_C", "Map ready...");
        mMap = googleMap;

        double lat = 33.9361684;
        double lng = -84.465229;
        float zoomLevel = 17f;

        String snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Lng: %2$.5f",
                lat, lng);


        // Add a marker homeMarker and move the camera
        LatLng homeLocation = new LatLng(lat, lng);
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().position(homeLocation)
                .title("Current Location").snippet(snippet));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLocation, zoomLevel));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fave_locale_menu_items, menu);
        return true;
    }


    @OnClick(R.id.menu_icon)
    public void showMenu(View view) {
        Log.d("TAG_Q", "showMenu: in the method");
        PopupMenu favMenu = new PopupMenu(this, view);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.fave_locale_menu_items, favMenu.getMenu());
        favMenu.show();
        favMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                double lat = 33.9361684;
                double lng = -84.465229;
                String latLng = lat + "," + lng;
                switch (item.getItemId()) {
                    case R.id.food:
                        searchPlaceOfInterest(item, latLng);
                    case R.id.hospitals:
                        searchPlaceOfInterest(item, latLng);
                    case R.id.churches:
                        searchPlaceOfInterest(item, latLng);
                }

                return false;
            }
        });
    }

    private void searchPlaceOfInterest(MenuItem item, String latLng) {
        compositeDisposable.add(
                locationViewModel.getGoogleLocations(latLng, 10000,
                        item.getTitle().toString(), item.getTitle().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Consumer<PlacesResponse>() {
                                    @Override
                                    public void accept(PlacesResponse placesResponse) throws Exception {


                                        Gson gson = new Gson();
                                        String response = gson.toJson(placesResponse);

                                        for (Result r : placesResponse.getResults()) {
                                            double lat = r.getGeometry().getLocation().getLat();
                                            double lng = r.getGeometry().getLocation().getLng();
                                            LatLng location = new LatLng(lat, lng);

                                            mMap.addMarker(
                                                    new MarkerOptions()
                                                            .position(location)
                                                            .title(r.getPlaceId())
                                            );
                                        }

                                        Log.d("TAG_Y", response);

                                        Log.d("TAG_X", "Places received : " + placesResponse.getResults().size());
                                    }
                                }
                        ));
    }

   /* void setUpRv(List<Loca> results){
        MovieAdapter movieAdapter = new MovieAdapter(results, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        movieSeachRecyclerView.setAdapter(movieAdapter);
        movieSeachRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(movieSeachRecyclerView.getContext(),
                layoutManager.getOrientation());
        movieSeachRecyclerView.addItemDecoration(dividerItemDecoration);
    }
*/

    private void setUpLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
            setUpMap();

        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("TAG_X", "Something here" + permissions.length);

        if (requestCode == REQUEST_CODE && permissions.length > 0) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (permissions[1].equals(Manifest.permission.CAMERA) &&
                        grantResults[1] != PackageManager.PERMISSION_GRANTED)
                    requestPermission();
            } else if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    permissions[1].equals(Manifest.permission.CAMERA)) {
                setUpMap();
            }
        }
    }

    private void setPoiClick(GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {

            }
        });

    }


}
