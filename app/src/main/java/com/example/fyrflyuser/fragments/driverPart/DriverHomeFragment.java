package com.example.fyrflyuser.fragments.driverPart;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.FragmentDriverHomeBinding;
import com.example.fyrflyuser.modelClasses.LatLngDriver;
import com.example.fyrflyuser.modelClasses.googleMaps.LocationObject;
import com.example.fyrflyuser.modelClasses.googleMaps.RideObject;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DriverHomeFragment extends Fragment implements OnMapReadyCallback {

    FragmentDriverHomeBinding binding;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LocationObject pickupLocation, currentLocation, destinationLocation;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    DatabaseReference rideRef;

    LatLng latLng;

    public DriverHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDriverHomeBinding.inflate(inflater, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());



        setgoogleMap();
        getDataFromFireBase();
        rideRequests();
        return binding.getRoot();
    }

    private void getDataFromFireBase() {


        DatabaseReference DriverData = FirebaseDatabase.getInstance().getReference().child("rideRequest");
        DriverData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                        // here you can access to name property like university.name
                    }
                    //  Toast.makeText(CustomerMapActivity.this, "ok00"+mDriver.getLatitude(), Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void rideRequests() {


        NearestRequestFragment fragment = new NearestRequestFragment();
        fragment.show(requireActivity().getSupportFragmentManager(), "TAG");

    }

    private void setgoogleMap() {

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                int width = 100;
//                int height = 100;
//
//                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location), width, height, false);
//                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
//
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(latLng)
//                        .icon(bitmapDescriptor);

                // mMap.addMarker(markerOptions);

                Log.d("onMapReady", "onMapReady: " + "12346767");

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);

                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().isCompassEnabled();
                mMap.getUiSettings().isMapToolbarEnabled();
                mMap.getUiSettings().setCompassEnabled(true);


            }
        }
    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                getActivity();
                currentLocation = new LocationObject(new LatLng(location.getLatitude(), location.getLongitude()), "");


                //      mCurrentRide.setCurrent(currentLocation);

                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                sendCurrentLatLngfirebase(location.getLatitude(), location.getLongitude());

                boolean zoomUpdated = false;

                if (!zoomUpdated) {
                    float zoomLevel = 17.0f; //This goes up to 21
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                    zoomUpdated = true;
                }

            }
        }
    };


    private void sendCurrentLatLngfirebase(double latitude, double longitude) {
        Map<String, Object> newUserMap = new HashMap<>();

        newUserMap.put("latLng", latitude+", "+longitude);

        FirebaseDatabase.getInstance().getReference().child("LatLngDriver").child(CommonUtils.getUserId()).setValue(newUserMap);
        FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(CommonUtils.getUserId()).updateChildren(newUserMap).addOnCompleteListener((OnCompleteListener<Void>) task -> {
            Map<String, Object> latlong = new HashMap<>();
            latlong.put("driverId", CommonUtils.getUserId());
            latlong.put("lat", latitude);
            latlong.put("lng", longitude);


            rideRef = FirebaseDatabase.getInstance().getReference().child("LatLngD");

            FirebaseDatabase.getInstance().getReference().child("LatLngD").child(CommonUtils.getUserId()).setValue(latlong);

        });
}}