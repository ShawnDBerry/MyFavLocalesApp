package com.example.android.myfavlocalesapp.view;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.myfavlocalesapp.R;
import com.example.android.myfavlocalesapp.model.User;
import com.example.android.myfavlocalesapp.viewmodel.LocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoginFragment.LoginDelegator {

    private GoogleMap mMap;
    public static final int REQUEST_CODE = 205;

    private LocationViewModel locationViewModel;
    private LoginFragment loginFragment = new LoginFragment();

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebaseAuth = FirebaseAuth.getInstance();

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        checkIfUserLoggedIn();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void checkIfUserLoggedIn() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, "User needs to be logged in", Toast.LENGTH_LONG).show();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map, loginFragment)
                    .commit();
        } else
            Toast.makeText(this, "User: " + firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
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

                }else{
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
    }

    @Override
    public void loginUser(User loginUser) {
        firebaseAuth.signInWithEmailAndPassword(
                loginUser.getEmailAddress(), loginUser.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    firebaseAuth.getCurrentUser().getDisplayName(),
                                    Toast.LENGTH_SHORT).show();
                        }else {
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

        setUpLocation();
        mMap.addMarker(new MarkerOptions().position(homeLocation)
                .title("Current Location").snippet(snippet));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLocation, zoomLevel));

    }

    private void setUpLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
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

        if (requestCode == REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (permissions[1].equals(Manifest.permission.CAMERA) &&
                        grantResults[1] != PackageManager.PERMISSION_GRANTED)
                    requestPermission();
            } else if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    permissions[1].equals(Manifest.permission.CAMERA)) {

            }
        }
    }

}