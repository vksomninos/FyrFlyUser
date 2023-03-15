package com.example.fyrflyuser.fragments.home;


import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.fyrflyuser.R;
import com.example.fyrflyuser.adeptars.AdapterSelectCar;
import com.example.fyrflyuser.adeptars.TypeAdapter;
import com.example.fyrflyuser.databinding.FragmentHomeBinding;
import com.example.fyrflyuser.fragments.search.SearchDestinationFragment;
import com.example.fyrflyuser.modelClasses.googleMaps.LocationObject;
import com.example.fyrflyuser.modelClasses.googleMaps.RideObject;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.logicbeanzs.uberpolylineanimation.MapAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements OnMapReadyCallback {
    AdapterSelectCar adapter;
    FragmentHomeBinding binding;
    RecyclerView rvselectCar;
    ImageView chatImage, phoneCall;
    AppCompatButton cancelTripBtn, startTripBtn;
    FusedLocationProviderClient mFusedLocationClient;
    SupportMapFragment mapFragment;
    LocationRequest mLocationRequest;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    boolean zoomUpdated;
    private Marker destinationMarker, pickupMarker;
    RideObject mCurrentRide;
    LatLng TamWorth = new LatLng(30.704649, 75.0471016);
    LatLng BAthinda = new LatLng(30.1791154, 75.0471016);
    LatLng mohali = new LatLng(30.7041168, 76.7176885);
    private LatLng latLng;
    private GoogleMap mMap;
    BitmapDescriptor bitmapDescriptor;
    private LocationObject pickupLocation, currentLocation, destinationLocation;
    private TypeAdapter mAdapter;


    double locationLat = 0.0, locationLng = 0.0;

    private List<Address> addressList = new ArrayList<>();
    private List<Address> addresses = new ArrayList<>();
    LinearLayout confirmSelectedRide, selectCarLay, bid_bottom_sheet, ChooseYourPickUpSpot, comfirmingYourRide, pikup, moreInfoAboutRide, Cancel_Trip, startTrip;
    RelativeLayout confirmRide, payment, paymentconf, reserve_car, confirm_rideselected, confirmRide2, moreAboutride, relativeCalling;
    LabeledSwitch switchForBid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        findIds();
//        SearchDestinationFragment fragment = new SearchDestinationFragment();
//        fragment.show(requireActivity().getSupportFragmentManager(), "TAG");
        setOnClick();
        setAdapter();
        setData();
        geyRideRequest();

        Toast.makeText(requireContext(), "user id" + CommonUtils.getUserId(), Toast.LENGTH_SHORT).show();


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return binding.getRoot();
    }

    private void geyRideRequest() {


    }

    private void setData() {

    }

    private void findIds() {
        confirmRide = binding.cordinator.findViewById(R.id.confirmRide);
        confirmSelectedRide = binding.cordinator.findViewById(R.id.confirmSelectedRide2);
        selectCarLay = binding.cordinator.findViewById(R.id.selectCarLay);
        payment = binding.cordinator.findViewById(R.id.payment);
        paymentconf = binding.cordinator.findViewById(R.id.paymentconf);
        switchForBid = binding.cordinator.findViewById(R.id.switchforBid);
        bid_bottom_sheet = binding.cordinator.findViewById(R.id.bid_bottom_sheet);
        reserve_car = binding.cordinator.findViewById(R.id.reserve_car);
        confirm_rideselected = binding.cordinator.findViewById(R.id.confirm_rideselected);
        ChooseYourPickUpSpot = binding.cordinator.findViewById(R.id.ChooseYourPickUpSpot);
        confirmRide2 = binding.cordinator.findViewById(R.id.confirmRide2);
        comfirmingYourRide = binding.cordinator.findViewById(R.id.comfirmingYourRide);
        pikup = binding.cordinator.findViewById(R.id.pikup);
        moreInfoAboutRide = binding.cordinator.findViewById(R.id.moreInfoAboutRide);
        moreAboutride = binding.cordinator.findViewById(R.id.moreAboutride);
        Cancel_Trip = binding.cordinator.findViewById(R.id.Cancel_Trip);
        cancelTripBtn = binding.cordinator.findViewById(R.id.cancel_trip);
        relativeCalling = binding.cordinator.findViewById(R.id.relativeCalling);
        startTrip = binding.cordinator.findViewById(R.id.startTrip);
        startTripBtn = binding.cordinator.findViewById(R.id.startTripBtn);
        chatImage = binding.cordinator.findViewById(R.id.chatImage);
        phoneCall = binding.cordinator.findViewById(R.id.phoneCall);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnClick() {

        confirmRide.setOnClickListener(v -> {
            Toast.makeText(requireActivity(), "ok", Toast.LENGTH_SHORT).show();
            selectCarLay.setVisibility(View.GONE);
            confirmSelectedRide.setVisibility(View.VISIBLE);
        });

        payment.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_paymentFragment);
        });


        paymentconf.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_paymentFragment);
        });

        switchForBid.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    Toast.makeText(requireContext(), "ok", Toast.LENGTH_SHORT).show();
                    DialogForBid();
                }
            }
        });

        reserve_car.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_reserveAtripFragment);

        });

        confirm_rideselected.setOnClickListener(v -> {

            ChooseYourPickUpSpot.setVisibility(View.VISIBLE);
            confirmSelectedRide.setVisibility(View.GONE);

        });

        confirmRide2.setOnClickListener(v -> {

            ChooseYourPickUpSpot.setVisibility(View.GONE);
            comfirmingYourRide.setVisibility(View.VISIBLE);
        });
        comfirmingYourRide.setOnClickListener(v -> {
            comfirmingYourRide.setVisibility(View.GONE);
            pikup.setVisibility(View.VISIBLE);
        });
        moreAboutride.setOnClickListener(v -> {
            moreInfoAboutRide.setVisibility(View.VISIBLE);

        });

        cancelTripBtn.setOnClickListener(v -> {
            pikup.setVisibility(View.GONE);
            moreInfoAboutRide.setVisibility(View.GONE);
            Cancel_Trip.setVisibility(View.VISIBLE);


        });

        relativeCalling.setOnClickListener(v -> {

            Navigation.findNavController(v).navigate(R.id.action_homeFragment2_to_callingFragment);

        });

        startTripBtn.setOnClickListener(v -> {
            startTrip.setVisibility(View.VISIBLE);
            moreInfoAboutRide.setVisibility(View.GONE);
            pikup.setVisibility(View.GONE);

        });


        chatImage.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_chatFragment);


        });

        phoneCall.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_chatFragment);

        });

        phoneCall.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_callingFragment);


        });


        binding.from.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                @SuppressLint("ClickableViewAccessibility")
                NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
                navController.navigateUp();
                navController.navigate(R.id.searchFragment);
                return true;

            }
        });


        binding.to.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                @SuppressLint("ClickableViewAccessibility")
                NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
                navController.navigateUp();
                navController.navigate(R.id.searchFragment);

                return true;
            }
        });


        binding.saveLocation.setOnClickListener(v -> {
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
            zoomUpdated = false;
            pickupLocation = currentLocation;

            pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation.getCoordinates()).title("Pickup").icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(requireContext(), App.getPrefManager().getString(AppConstants.ADDRESS_FROM), null))));

            if (destinationLocation != null) {
                destinationMarker = mMap.addMarker(new MarkerOptions().position(destinationLocation.getCoordinates()).title("Destination").icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(getContext(), destinationLocation.getName(), null))));
            }
        });
    }


    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CommonUtils.requestPermission(requireActivity(), LOCATION_PERMISSION_REQUEST_CODE, android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
        }
    }


    private void DialogForBid() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.the_bid_sectionson);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView ok = dialog.findViewById(R.id.ok);


        ok.setOnClickListener(v -> {
            bid_bottom_sheet.setVisibility(View.VISIBLE);
            confirmSelectedRide.setVisibility(View.GONE);
            dialog.dismiss();

        });
        dialog.show();

        cancel.setOnClickListener(v -> {

            dialog.dismiss();
        });

    }

    private void setAdapter() {
        rvselectCar = binding.cordinator.findViewById(R.id.selectCar_rv);
        adapter = new AdapterSelectCar();
        rvselectCar.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        // requireActivity().findViewById(R.id.actionBar).setVisibility(View.VISIBLE);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));


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
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().isCompassEnabled();
                mMap.getUiSettings().isMapToolbarEnabled();


            } else {
                checkLocationPermission();
            }
        }

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CALL_PHONE)) {
                new android.app.AlertDialog.Builder(requireActivity())
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE}, 1))
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE}, 1);
            }
        }

    }

    public static Bitmap generateBitmap(Context context, String location, String duration) {
        Bitmap bitmap = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RelativeLayout view = new RelativeLayout(context);
        try {
            mInflater.inflate(R.layout.item_marker, view, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView locationTextView = (TextView) view.findViewById(R.id.location);
        TextView durationTextView = (TextView) view.findViewById(R.id.duration);
        locationTextView.setText(location);

        if (duration != null) {
            durationTextView.setText(duration);
        } else {
            durationTextView.setVisibility(View.GONE);
        }

        view.setLayoutParams(new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);

        view.draw(c);

        return bitmap;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            LocationObject mLocation;

            if (currentLocation == null) {
                //  Snackbar.make(findViewById(R.id.drawer_layout), "First Activate GPS", Snackbar.LENGTH_LONG).show();
                return;
            }
            Place place = Autocomplete.getPlaceFromIntent(data);

            mLocation = new LocationObject(place.getLatLng(), place.getName());

            currentLocation = new LocationObject(new LatLng(currentLocation.getCoordinates().latitude, currentLocation.getCoordinates().longitude), "");

            if (requestCode == 1) {
                mMap.clear();
                destinationLocation = mLocation;
                destinationMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(requireContext(), destinationLocation.getName(), null))).position(destinationLocation.getCoordinates()));
                mCurrentRide.setDestination(destinationLocation);
                //    autocompleteFragmentTo.setText(destinationLocation.getName());
                if (pickupLocation != null) {
                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation.getCoordinates()).icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(requireContext(), pickupLocation.getName(), null))));
                    //        bringBottomSheetDown();
                }
            } else if (requestCode == 2) {
                mMap.clear();
                pickupLocation = mLocation;
                pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation.getCoordinates()).icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(requireContext(), pickupLocation.getName(), null))));
                mCurrentRide.setPickup(pickupLocation);
                //   autocompleteFragmentFrom.setText(pickupLocation.getName());
                if (destinationLocation != null) {
                    destinationMarker = mMap.addMarker(new MarkerOptions().position(destinationLocation.getCoordinates()).icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(requireContext(), destinationLocation.getName(), null))));
                    //   bringBottomSheetDown();
                }
            }
            MapAnimator();
            Toast.makeText(requireContext(), "loc " + comfirmingYourRide, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                }
            } else {
                Toast.makeText(requireContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    boolean getDriversAroundStarted = false;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (requireActivity() != null) {
                    currentLocation = new LocationObject(new LatLng(location.getLatitude(), location.getLongitude()), "");

                    Log.d("onLocationResult", "onLocationResult: " + location.getLatitude() + "," + location.getLongitude());
                    //      mCurrentRide.setCurrent(currentLocation);

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


                    String latlong = String.valueOf(latLng);
                    App.getPrefManager().saveString(AppConstants.LATLONG, latlong);


                    if (!zoomUpdated) {
                        float zoomLevel = 17.0f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                        zoomUpdated = true;
                    }

                    if (!getDriversAroundStarted) ;
                    // getDriversAround();

                }
            }
        }
    };
    private List<Polyline> polylines = new ArrayList<>();


    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


    /**
     * Remove route polylines from the map
     */
    private void MapAnimator() {
        if (polylines == null) {
            return;
        }
        for (Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();
    }


    public ArrayList<Double> parseJson(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        JSONObject jDistance = null;
        JSONObject jDuration = null;
        long totalDistance = 0;
        int totalSeconds = 0;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /* Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");

                /* Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {

                    jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");

                    totalDistance = totalDistance + Long.parseLong(jDistance.getString("value"));

                    /** Getting duration from the json data */
                    jDuration = ((JSONObject) jLegs.get(j)).getJSONObject("duration");
                    totalSeconds = totalSeconds + Integer.parseInt(jDuration.getString("value"));

                }
            }

            double dist = totalDistance / 1000.0;
            Log.d("distance", "Calculated distance:" + dist);
            int days = totalSeconds / 86400;
            int hours = (totalSeconds - days * 86400) / 3600;
            int minutes = (totalSeconds - days * 86400 - hours * 3600) / 60;
            int seconds = totalSeconds - days * 86400 - hours * 3600 - minutes * 60;
            Log.d("duration", days + " days " + hours + " hours " + minutes + " mins" + seconds + " seconds");
            ArrayList<Double> list = new ArrayList<Double>();
            list.add(dist);
            list.add((double) totalSeconds);

            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}