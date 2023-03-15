package com.example.fyrflyuser.activites;

import static com.example.fyrflyuser.utils.AppConstants.latitude;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.adeptars.TypeAdapter;

import com.example.fyrflyuser.databinding.ActivityMapCustomerBinding;
import com.example.fyrflyuser.modelClasses.DriverNearBy;
import com.example.fyrflyuser.modelClasses.LatLngDriver;
import com.example.fyrflyuser.modelClasses.UserDetailsRoot;
import com.example.fyrflyuser.modelClasses.googleMaps.CustomerObject;
import com.example.fyrflyuser.modelClasses.googleMaps.LocationObject;
import com.example.fyrflyuser.modelClasses.googleMaps.RideObject;
import com.example.fyrflyuser.mvvm.Mvvm;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;
import com.example.fyrflyuser.utils.SendNotification;
import com.example.fyrflyuser.utils.TypeObject;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.logicbeanzs.uberpolylineanimation.MapAnimator;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Main Activity displayed to the customer
 */
public class CustomerMapActivity extends AppCompatActivity
        implements OnMapReadyCallback, DirectionCallback {
    ActivityMapCustomerBinding binding;
    List<UserDetailsRoot.Details> profileRoot;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    int TIMEOUT_MILLISECONDS = 20000,
            CANCEL_OPTION_MILLISECONDS = 10000;
    String keyUnique;
    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SlideToActView mRequest;
    private LocationObject pickupLocation, currentLocation, destinationLocation;
    private Boolean requestBol = false;
    int bottomSheetStatus = 1;
    private Marker destinationMarker, pickupMarker;
    private LinearLayout mDriverInfo,
            mRadioLayout,
            mLocation,
            mLooking,
            mTimeout;

    private ImageView mDriverProfileImage;
    private TextView mDriverName;
    private TextView mDriverCar;
    private TextView mDriverLicense;
    private TextView mRatingText;
    private TextView autocompleteFragmentTo;
    private TextView autocompleteFragmentFrom;
    private UserDetailsRoot.Details details;

    CardView autocompleteFragmentFromContainer, mContainer;

    FloatingActionButton mCallDriver;
    FloatingActionButton mCancel;
    FloatingActionButton mCancelTimeout;
    FloatingActionButton mCurrentLocation;


    private ArrayList<LatLngDriver> locationArrayList;
    RideObject mCurrentRide;
    private TypeAdapter mAdapter;
    ArrayList<TypeObject> typeArrayList = new ArrayList<TypeObject>();
    private Boolean driverFound = false;
    private ValueEventListener driveHasEndedRefListener;
    Handler cancelHandler, timeoutHandler;
    NavigationView navigationView ;
    View header ;


    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapCustomerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mCurrentRide = new RideObject(CustomerMapActivity.this, null);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        details = App.getPrefManager().getModel(AppConstants.USER_INFO, UserDetailsRoot.Details.class);

        getUserData();
        supportMapFragment();
        findIds();
        getKey();
        bringBottomSheetUp();
        initPlacesAutocomplete();
        initRecyclerView();
        isRequestInProgress();
        apiGetDetails();
        onClicks();
        setData();
      //  App.getPrefManager().clearPreferences();

        Log.d("TOKEN_NAME","ok------"+App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN)+" "+CommonUtils.getUserId()+" "+details.getId());
        Toast.makeText(this, ""+CommonUtils.getUserId(), Toast.LENGTH_SHORT).show();

    }

    private void apiNearByDrive(List<Address> addresses) {


       new Mvvm().DriverNearBy(CustomerMapActivity.this,
               App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN)
               ,"mohali"
               ,"30.730904040975556"
        ,"76.68718121781367").observe(CustomerMapActivity.this, new Observer<DriverNearBy>() {
           @Override
           public void onChanged(DriverNearBy driverNearBy) {
               if (driverNearBy != null) {

                   if(driverNearBy.getStatus() == 1) {
                       Map<String, Object> newUserMap = new HashMap<>();
                       newUserMap.put("detail",driverNearBy.getDetails());
                       FirebaseDatabase.getInstance().getReference().child("DriverNearBy").child("40").setValue(newUserMap);

                       } else {
                   }
               } else {
               }
           }
       });

    }

    private void getKey() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        keyUnique = database.getReference("Users").child("Customers").push().getKey();
        App.getPrefManager().saveString(AppConstants.UNIQUE_ID, keyUnique);

    }


    private void apiGetDetails() {

        new Mvvm().userDetails(CustomerMapActivity.this,
                App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN),
                CommonUtils.getUserId()).observe(CustomerMapActivity.this,
                new Observer<UserDetailsRoot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(UserDetailsRoot profileRoot) {
                        if (profileRoot != null) {

                            if (profileRoot.getStatus() == 1) {

                                App.getPrefManager().saveModel(AppConstants.USER_INFO, profileRoot.getDetails());

                            } else {


                            }
                        } else {
                        }
                    }
                });
    }

    private void setData() {
        navigationView = findViewById(R.id.nav_view);
         header = navigationView.getHeaderView(0);
        CircleImageView userImage = header.findViewById(R.id.profile_photo);


            //    Glide.with(CustomerMapActivity.this).load(details.getDetails().getProfileImage()).placeholder(R.drawable.home_icon).into(userImage);




        setClicksOnNavigationItems(header);

    }

    private void setClicksOnNavigationItems(View headerview) {

        LinearLayout profile = headerview.findViewById(R.id.ProfileLay);
        LinearLayout makeBooking = headerview.findViewById(R.id.makeBooking);
        LinearLayout converToDriver = headerview.findViewById(R.id.converToDriver);
        LinearLayout myWallet = headerview.findViewById(R.id.myWalletLin);
        LinearLayout pushNotification = headerview.findViewById(R.id.pushNotification);
        LinearLayout info = headerview.findViewById(R.id.info);
        LinearLayout refer = headerview.findViewById(R.id.refer);
        LinearLayout emergencylin = headerview.findViewById(R.id.emergencylin);
        profile.setOnClickListener(v -> {

            Intent intent = new Intent(CustomerMapActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "1");
            startActivity(intent);


            Toast.makeText(this, "okk", Toast.LENGTH_SHORT).show();

        });

        makeBooking.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerMapActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "2");
            startActivity(intent);


        });

        myWallet.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerMapActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "3");
            startActivity(intent);


        });

        converToDriver.setOnClickListener(v -> {

            Intent intent = new Intent(CustomerMapActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "4");

            if(details.getDriverStatus()!= null )
            {
                intent.putExtra("driver_status", details.getDriverStatus());
            }else
            {
                intent.putExtra("driver_status", "");
            }



            startActivity(intent);
            //     binding.drawerLayout.

        });



        emergencylin.setOnClickListener(v -> {

            emergencylin.setOnClickListener(v1 -> {
                final Dialog dialog = new Dialog(CustomerMapActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.emergency_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView cancel = dialog.findViewById(R.id.cancel);
                TextView ok = dialog.findViewById(R.id.ok);

                cancel.setOnClickListener(view1 -> {
                    dialog.dismiss();

                });
                ok.setOnClickListener(view1 -> {
                    dialog.dismiss();
                });
                dialog.show();
            });


        });

        pushNotification.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerMapActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "7");
            intent.putExtra("driver_status", details.getDriverStatus());
            startActivity(intent);

        });
        info.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerMapActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "8");
            intent.putExtra("driver_status", details.getDriverStatus());
            startActivity(intent);

        });

    }

    private void onClicks() {

        mCancelTimeout.setOnClickListener(v -> {
            bottomSheetStatus = 0;
            mCurrentRide.cancelRide();
            endRide();
        });
        mRequest.setOnSlideCompleteListener(v -> {
            binding.includeq.confirmSelectedRide2.setVisibility(View.VISIBLE);
            binding.includeq.radioLayout.setVisibility(View.GONE);

        });
        binding.includeq.confirmRideselected.setOnClickListener(v -> {
            binding.includeq.confirmSelectedRide2.setVisibility(View.GONE);

            startRideRequest();


        });
        mCancel.setOnClickListener(v -> {
            bottomSheetStatus = 0;
            mCurrentRide.cancelRide();
            endRide();
        });
        mCallDriver.setOnClickListener(view -> {
            if (mCurrentRide == null) {
                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.driver_no_phone), Snackbar.LENGTH_LONG).show();
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mCurrentRide.getDriver().getPhone()));
                startActivity(intent);
            } else {
                Snackbar.make(findViewById(R.id.drawer_layout), getString(R.string.no_phone_call_permissions), Snackbar.LENGTH_LONG).show();
            }
        });

        mCurrentLocation.setOnClickListener(view -> {

            autocompleteFragmentFrom.setText(getString(R.string.current_location));
            mCurrentLocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_primary_24dp));
            pickupLocation = currentLocation;
            if (pickupLocation == null) {
                return;
            }
            fetchLocationName();

            mMap.clear();
            pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation.getCoordinates()).title("Pickup").icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(CustomerMapActivity.this, pickupLocation.getName(), null))));
            mCurrentRide.setPickup(pickupLocation);
            //pickupLocation.getName()
            autocompleteFragmentFrom.setText(pickupLocation.getName());
            if (destinationLocation != null) {
                destinationMarker = mMap.addMarker(new MarkerOptions().position(destinationLocation.getCoordinates()).title("Destination").icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(CustomerMapActivity.this, destinationLocation.getName(), null))));
                bringBottomSheetDown();

            }
            MapAnimator();
            getRouteToMarker();

            mRequest.setText(getString(R.string.call_uber));
        });

        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                apiGetDetails();

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        binding.menurelative.setOnClickListener(v -> {

            binding.drawerLayout.openDrawer(GravityCompat.START);

        });

    }

    private void findIds() {


        mDriverInfo = findViewById(R.id.driverInfo);
        mRadioLayout = findViewById(R.id.radioLayout);

        mDriverProfileImage = findViewById(R.id.driverProfileImage);

        mDriverName = findViewById(R.id.driverName);
        mDriverCar = findViewById(R.id.driverCar);
        mDriverLicense = findViewById(R.id.driverPlate);

        mCallDriver = findViewById(R.id.phone);

        mRatingText = findViewById(R.id.ratingText);

        mContainer = findViewById(R.id.container_card);

        autocompleteFragmentTo = findViewById(R.id.place_to);
        autocompleteFragmentFrom = findViewById(R.id.place_from);
        autocompleteFragmentFromContainer = findViewById(R.id.place_from_container);
        mCurrentLocation = findViewById(R.id.current_location);
        mLocation = findViewById(R.id.location_layout);
        mLooking = findViewById(R.id.looking_layout);
        mTimeout = findViewById(R.id.timeout_layout);

        mRequest = findViewById(R.id.request);
        mCancel = findViewById(R.id.cancel);
        mCancelTimeout = findViewById(R.id.cancel_looking);

    }

    private void supportMapFragment() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    /**
     * Handles stating the ride request.
     * Starts up two timers. The first will show up after CANCEL_OPTION_MILLISECONDS
     * and it will display a layout with a button for the user to be able to cancel the ride..
     * The second will cancel the ride automatically if the TIMEOUT_MILLISECONDS is reached.
     */
    private void startRideRequest() {
        cancelHandler = new Handler();
        cancelHandler.postDelayed(() -> {
            if (mCurrentRide == null) {
                return;
            }
            if (mCurrentRide.getDriver() == null) {
                runOnUiThread(() -> {
                    mTimeout.setVisibility(View.VISIBLE);
                });
            }
        }, CANCEL_OPTION_MILLISECONDS);

        timeoutHandler = new Handler();
        cancelHandler.postDelayed(() -> {
            if (mCurrentRide == null) {
                return;
            }
            if (mCurrentRide.getDriver() == null) {
                runOnUiThread(() -> {
                    bottomSheetStatus = 0;
                    mCurrentRide.cancelRide();
                    endRide();
                    new AlertDialog.Builder(CustomerMapActivity.this)
                            .setTitle(getResources().getString(R.string.no_drivers_around))
                            .setMessage(getResources().getString(R.string.no_driver_found))
                            .setPositiveButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                            .setIcon(R.drawable.ic_cancel_black_24dp)
                            .show();

                });
            }
        }, TIMEOUT_MILLISECONDS);

        bringBottomSheetDown();


        if (!requestBol) {
            sendRequestToDriver();
            mCurrentRide.setDestination(destinationLocation);
            mCurrentRide.setPickup(pickupLocation);
            mCurrentRide.setRequestService(mAdapter.getSelectedItem().getId());
            mCurrentRide.setDistance(routeData.get(0));
            mCurrentRide.setDuration(routeData.get(1));
            mCurrentRide.setRideRequest(true);

            mCurrentRide.setCustomerName(App.getPrefManager().getString(AppConstants.USER_NAME));
            if (mCurrentRide.checkRide() == -1) {
                return;
            }

            requestBol = true;

            mRequest.setText(getResources().getString(R.string.getting_driver));

            mCurrentRide.postRideInfo();

            requestListener();
        }
    }

    private void sendRequestToDriver() {


    }


    /**
     * Initializes the recyclerview that shows the costumer the
     * available car types
     */
    private void initRecyclerView() {
        typeArrayList = CommonUtils.getTypeList(CustomerMapActivity.this);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CustomerMapActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TypeAdapter(typeArrayList, CustomerMapActivity.this, routeData);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Handles showing the bottom sheet with animation.
     */
    private void bringBottomSheetUp() {
        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        mContainer.startAnimation(slideUp);
        mContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Handles hiding the bottom sheet with animation.
     * Also takes care of hiding or showing the elements in it
     * depending on the current state of the request.
     */
    private void bringBottomSheetDown() {
        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        slideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (bottomSheetStatus) {
                    case 0:
                        bottomSheetStatus = 1;
                        destinationLocation = null;
                        pickupLocation = null;
                        mCurrentRide.setCurrent(null);
                        mCurrentRide.setDestination(null);
                        autocompleteFragmentFrom.setText(getString(R.string.from));
                        autocompleteFragmentTo.setText(getString(R.string.to));
                        mCurrentLocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_grey_24dp));
                        mMap.clear();
                        MapAnimator();
                        erasePolylines();
                        mRadioLayout.setVisibility(View.GONE);
                        mLocation.setVisibility(View.VISIBLE);
                        mContainer.setBottom(200);
                        mLooking.setVisibility(View.GONE);
                        mDriverInfo.setVisibility(View.GONE);
                        break;
                    case 1:
                        bottomSheetStatus = 2;
                        mRequest.resetSlider();
                        mRadioLayout.setVisibility(View.VISIBLE);
                        mLocation.setVisibility(View.GONE);
                        mLooking.setVisibility(View.GONE);
                        mDriverInfo.setVisibility(View.GONE);
                        mTimeout.setVisibility(View.GONE);
                        break;
                    case 2:
                        bottomSheetStatus = 3;
                        mLocation.setVisibility(View.GONE);
                        mRadioLayout.setVisibility(View.GONE);
                        mLooking.setVisibility(View.VISIBLE);
                        mDriverInfo.setVisibility(View.GONE);
                        break;
                    case 3:
                        bottomSheetStatus = 0;
                        mLocation.setVisibility(View.GONE);
                        mRadioLayout.setVisibility(View.GONE);
                        mLooking.setVisibility(View.GONE);
                        mDriverInfo.setVisibility(View.VISIBLE);
                }
                bringBottomSheetUp();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mContainer.startAnimation(slideDown);
    }

    /**
     * Init Places according the updated google api and
     * listen for user inputs, when a user chooses a place change the values
     * of destination and destinationLocation so that the user can call a driver
     */
    void initPlacesAutocomplete() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        }

        autocompleteFragmentTo.setOnClickListener(v -> {
            if (requestBol) {
                return;
            }
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                    .build(getApplicationContext());
            startActivityForResult(intent, 1);
        });

        autocompleteFragmentFrom.setOnClickListener(v -> {
            if (requestBol) {
                return;
            }
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                    .build(getApplicationContext());
            startActivityForResult(intent, 2);
        });
    }


    /**
     * Fetches current user's info and populates the design elements
     */
    private void getUserData() {
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child((App.getPrefManager().getPreferences(AppConstants.USER_ID)));
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    View header = navigationView.getHeaderView(0);

                    CustomerObject mCustomer = new CustomerObject();


                    mCustomer.parseData(dataSnapshot);
                    App.getPrefManager().saveString(AppConstants.USER_NAME, mCustomer.getName());

                    TextView mUsername = header.findViewById(R.id.text_Name);
                    ImageView mProfileImage = header.findViewById(R.id.profile_photo);
                    mProfileImage.setOnClickListener(v -> {
                        Toast.makeText(CustomerMapActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    });
                    mUsername.setText(mCustomer.getName());
                    if (!mCustomer.getProfileImage().equals("default"))
                        Glide.with(getApplication()).load(mCustomer.getProfileImage()).apply(RequestOptions.circleCropTransform()).into(mProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /**
     * Checks if request is in progress by looking at the last ride_info child that the
     * current customer was a part of and if that last ride is still ongoing then
     * start all of the relevant variables up, with that ride info.
     */
    private void isRequestInProgress() {
        FirebaseDatabase.getInstance().getReference().child("ride_info").orderByChild("customerId").equalTo(App.getPrefManager().getPreferences(AppConstants.USER_ID)).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }

                for (DataSnapshot mData : dataSnapshot.getChildren()) {
                    mCurrentRide = new RideObject();
                    mCurrentRide.parseData(mData);

                    if (mCurrentRide.getCancelled() || mCurrentRide.getEnded()) {
                        mCurrentRide = new RideObject();
                        return;
                    }

                    if (mCurrentRide.getDriver() == null) {
                        mTimeout.setVisibility(View.VISIBLE);
                        bottomSheetStatus = 2;
                    } else {
                        bottomSheetStatus = 3;
                    }
                    bringBottomSheetDown();
                    requestListener();
                }

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }


    /**
     * Listener for the current request.
     */
    private void requestListener() {
        if (mCurrentRide == null) {
            return;
        }

        driveHasEndedRefListener = mCurrentRide.getRideRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                RideObject mRide = new RideObject();
                mRide.parseData(dataSnapshot);

                if (mRide.getCancelled() || mRide.getEnded()) {
                    if (!mCurrentRide.getEnded() && mRide.getEnded()) {
                        mCurrentRide.showDialog(CustomerMapActivity.this);
                    }
                    cancelHandler.removeCallbacksAndMessages(null);
                    timeoutHandler.removeCallbacksAndMessages(null);
                    bottomSheetStatus = 0;
                    endRide();

                    if (mRide.getCancelledType() == 11) {
                        new AlertDialog.Builder(CustomerMapActivity.this)
                                .setTitle(getResources().getString(R.string.no_default_payment))
                                .setMessage(getResources().getString(R.string.no_payment_available_message))
                                .setPositiveButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss())
                                .setIcon(R.drawable.ic_cancel_black_24dp)
                                .show();
                    }
                    return;
                }

                if (mCurrentRide.getDriver() == null && mRide.getDriver() != null) {
                    try {
                        mCurrentRide = (RideObject) mRide.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    cancelHandler.removeCallbacksAndMessages(null);
                    timeoutHandler.removeCallbacksAndMessages(null);

                    getDriverInfo();
                    getDriverLocation();
                }

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Get's most updated driver location and it's always checking for movements.
     * Even though we used geofire to push the location of the driver we can use a normal
     * Listener to get it's location with no problem.
     * 0 -> Latitude
     * 1 -> Longitudde
     */
    private Marker mDriverMarker;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationRefListener;

    private void getDriverLocation() {
        if (mCurrentRide.getDriver().getId() == null) {
            return;
        }
        driverLocationRef = FirebaseDatabase.getInstance().getReference().child("driversWorking").child(mCurrentRide.getDriver().getId()).child("l");
        driverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();

                    if (map == null) {
                        return;
                    }
                    double locationLat = 0;
                    double locationLng = 0;
                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LocationObject mDriverLocation = new LocationObject(new LatLng(locationLat, locationLng), "");
                    if (mDriverMarker != null) {
                        mDriverMarker.remove();
                    }
                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.getCoordinates().latitude);
                    loc1.setLongitude(pickupLocation.getCoordinates().longitude);
                    Location loc2 = new Location("");
                    loc2.setLatitude(mDriverLocation.getCoordinates().latitude);
                    loc2.setLongitude(mDriverLocation.getCoordinates().longitude);

                    float distance = loc1.distanceTo(loc2);

                    if (distance < 100) {
                        mRequest.setText(getResources().getString(R.string.driver_here));
                    } else {
                        mRequest.setText(getResources().getString(R.string.driver_found));
                    }

                    mCurrentRide.getDriver().setLocation(mDriverLocation);


                    mDriverMarker = mMap.addMarker(new MarkerOptions().position(mCurrentRide.getDriver().getLocation().getCoordinates()).title("your driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
                }

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });

    }

    /**
     * Get all the user information that we can get from the user's database.
     */
    private void getDriverInfo() {
        if (mCurrentRide == null) {
            return;
        }

        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(mCurrentRide.getDriver().getId());
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    mCurrentRide.getDriver().parseData(dataSnapshot);

                    mDriverName.setText(mCurrentRide.getDriver().getNameDash());
                    mDriverCar.setText(mCurrentRide.getDriver().getCarDash());
                    mDriverLicense.setText(mCurrentRide.getDriver().getLicenseDash());
                    if (mCurrentRide.getDriver().getProfileImage().equals("default")) {
                        mDriverProfileImage.setImageResource(R.drawable.ic_default_user);
                    } else {
                        Glide.with(getApplication())
                                .load(mCurrentRide.getDriver().getProfileImage())
                                .apply(RequestOptions.circleCropTransform())
                                .into(mDriverProfileImage);
                    }


                    mRatingText.setText(String.valueOf(mCurrentRide.getDriver().getDriverRatingString()));

                    bringBottomSheetDown();

                    new SendNotification("You have a customer waiting", "New Ride", mCurrentRide.getDriver().getNotificationKey());
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }


    /**
     * End Ride by removing all of the active listeners,
     * returning all of the values to the default state
     * and clearing the map from markers
     */
    private void endRide() {


        if (cancelHandler != null) {
            cancelHandler.removeCallbacksAndMessages(null);
        }

        if (timeoutHandler != null) {
            timeoutHandler.removeCallbacksAndMessages(null);
        }

        requestBol = false;
        if (driverLocationRefListener != null)
            driverLocationRef.removeEventListener(driverLocationRefListener);

        if (driveHasEndedRefListener != null && mCurrentRide.getRideRef() != null)
            mCurrentRide.getRideRef().removeEventListener(driveHasEndedRefListener);

        if (mCurrentRide != null && driverFound) {
            DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(mCurrentRide.getDriver().getId()).child("customerRequest");
            driverRef.removeValue();
        }

        pickupLocation = null;
        destinationLocation = null;

        driverFound = false;
        String userId = App.getPrefManager().getPreferences(AppConstants.USER_ID);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId, (key, error) -> {
        });

        if (destinationMarker != null) {
            destinationMarker.remove();
        }
        if (pickupMarker != null) {
            pickupMarker.remove();
        }
        if (mDriverMarker != null) {
            mDriverMarker.remove();
        }
        mMap.clear();
        mRequest.setText(getString(R.string.call_uber));

        mDriverName.setText("");
        mDriverCar.setText(getString(R.string.destination));
        mDriverProfileImage.setImageResource(R.drawable.ic_default_user);

        autocompleteFragmentTo.setText(getString(R.string.to));
        autocompleteFragmentFrom.setText(getString(R.string.from));
        mCurrentLocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_grey_24dp));

        mCurrentRide = new RideObject(CustomerMapActivity.this, null);
        getDriversAround();
        bringBottomSheetDown();
        zoomUpdated = false;

        mAdapter.setSelectedItem(typeArrayList.get(0));
        mAdapter.notifyDataSetChanged();

    }


    /**
     * Find and update user's location.
     * The update interval is set to 1000Ms and the accuracy is set to PRIORITY_HIGH_ACCURACY,
     * If you're having trouble with battery draining too fast then change these to lower values
     *
     * @param googleMap - Map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        }

        DatabaseReference DriverData = FirebaseDatabase.getInstance().getReference().child("LatLngDriver");
        DriverData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        LatLngDriver mDriver = postSnapshot.getValue(LatLngDriver.class);
                        locationArrayList = new ArrayList<>();
                        locationArrayList.add(mDriver);

                        // here you can access to name property like university.name
                        setDriverLocation(locationArrayList);
                    }
                    //  Toast.makeText(CustomerMapActivity.this, "ok00"+mDriver.getLatitude(), Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setDriverLocation(ArrayList<LatLngDriver> locationArrayList) {

        try {
            for (int i = 0; i < locationArrayList.size(); i++) {
                String[] latlong = locationArrayList.get(i).getLatLng().split(",");
                double latitude = Double.parseDouble(latlong[0]);
                double longitude = Double.parseDouble(latlong[1]);
                LatLng location = new LatLng(latitude, longitude);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(location).title("")
                        // below line is use to add custom marker on our map.
                        .icon(BitmapFromVector(getApplicationContext(), R.drawable.car_)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

         //   mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }

        }catch (Exception e){

            }
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    boolean zoomUpdated = false;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplication() != null) {
                    currentLocation = new LocationObject(new LatLng(location.getLatitude(), location.getLongitude()), "");
                    mCurrentRide.setCurrent(currentLocation);

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    sendCurrentLatLngfirebase(location.getLatitude(), location.getLongitude());
                    App.getPrefManager().saveString(latitude, String.valueOf(location.getLatitude()));
                    App.getPrefManager().saveString(AppConstants.longitude, String.valueOf(location.getLongitude()));

                    Geocoder geocoder = new Geocoder(CustomerMapActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses  = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                        apiNearByDrive(addresses);


                        String suburb = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String zip = addresses.get(0).getPostalCode();
                        String country = addresses.get(0).getCountryName();

                        Log.e("Log for data",""+suburb+" "+state+" "+zip+" "+country);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //   sendLatLngToFirebase(latLng);
                    if (!zoomUpdated) {
                        float zoomLevel = 17.0f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                        zoomUpdated = true;
                    }

                    if (!getDriversAroundStarted)
                        getDriversAround();

                }
            }
        }
    };

    private void sendCurrentLatLngfirebase(double latitude, double longitude) {
        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("latitude", latitude);
        newUserMap.put("longitude", longitude);
        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(CommonUtils.getUserId()).updateChildren(newUserMap).addOnCompleteListener((OnCompleteListener<Void>) task -> {

        });

    }

    /**
     * This function returns the name of location given the coordinates
     * of said location
     */
    private void fetchLocationName() {
        if (pickupLocation == null) {
            return;
        }
        try {

            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(currentLocation.getCoordinates().latitude, currentLocation.getCoordinates().longitude, 1);
            if (addresses.isEmpty()) {
                autocompleteFragmentFrom.setText(R.string.waiting_for_location);
            } else {
                addresses.size();
                if (addresses.get(0).getThoroughfare() == null) {
                    pickupLocation.setName(addresses.get(0).getLocality());
                } else if (addresses.get(0).getLocality() == null) {
                    pickupLocation.setName("Unknown Location");
                } else {
                    pickupLocation.setName(addresses.get(0).getLocality() + ", " + addresses.get(0).getThoroughfare());
                }
                autocompleteFragmentFrom.setText(pickupLocation.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get permissions for our app if they didn't previously exist.
     * requestCode -> the number assigned to the request that we've made.
     * Each request has it's own unique request code.
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(CustomerMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE}, 1))
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(CustomerMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE}, 1);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(getApplication(), "Please provide the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    boolean getDriversAroundStarted = false;
    List<Marker> markerList = new ArrayList<Marker>();

    /**
     * Displays drivers around the user's current
     * location and updates them in real time.
     */
    private void getDriversAround() {
        if (currentLocation == null) {
            return;
        }
        getDriversAroundStarted = true;
        DatabaseReference driversLocation = FirebaseDatabase.getInstance().getReference().child(("driversWorking"));


        GeoFire geoFire = new GeoFire(driversLocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(currentLocation.getCoordinates().latitude, currentLocation.getCoordinates().longitude), 10000);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (mCurrentRide != null) {
                    if (mCurrentRide.getDriver() != null) {
                        return;
                    }
                }
                for (Marker markerIt : markerList) {
                    if (markerIt.getTag() == null || key == null) {
                        continue;
                    }
                    if (markerIt.getTag().equals(key))
                        return;
                }


                checkDriverLastUpdated(key);
                LatLng driverLocation = new LatLng(location.latitude, location.longitude);


                Marker mDriverMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)).position(driverLocation).title(key));
                mDriverMarker.setTag(key);

                markerList.add(mDriverMarker);

            }

            @Override
            public void onKeyExited(String key) {
                for (Marker markerIt : markerList) {
                    if (markerIt.getTag() == null || key == null) {
                        continue;
                    }
                    if (markerIt.getTag().equals(key)) {
                        markerIt.remove();
                        markerList.remove(markerIt);
                        return;
                    }

                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker markerIt : markerList) {
                    if (markerIt.getTag() == null || key == null) {
                        continue;
                    }
                    if (markerIt.getTag().equals(key)) {
                        markerIt.setPosition(new LatLng(location.latitude, location.longitude));
                        return;
                    }
                }

                checkDriverLastUpdated(key);
                LatLng driverLocation = new LatLng(location.latitude, location.longitude);

                Marker mDriverMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)).position(driverLocation).title(key));
                mDriverMarker.setTag(key);

                markerList.add(mDriverMarker);
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    /**
     * Checks if driver has not been updated in a while, if it has been more than x time
     * since the driver location was last updated then remove it from the database.
     *
     * @param key - id of the driver
     */
    private void checkDriverLastUpdated(String key) {
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("Drivers")
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            return;
                        }

                        if (dataSnapshot.child("last_updated").getValue() != null) {
                            long lastUpdated = Long.parseLong(dataSnapshot.child("last_updated").getValue().toString());
                            long currentTimestamp = System.currentTimeMillis();

                            if (currentTimestamp - lastUpdated > 10000) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversWorking");
                                GeoFire geoFire = new GeoFire(ref);
                                geoFire.removeLocation(dataSnapshot.getKey(), (key1, error) -> {
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {
                    }
                });
    }


    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(CustomerMapActivity.this, CustomerMapActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Get Route from pickup to destination, showing the route to the user
     */
    private void getRouteToMarker() {

        String serverKey = getResources().getString(R.string.google_maps_key);
        if (mCurrentRide.getDestination() != null && mCurrentRide.getPickup() != null) {
            GoogleDirection.withServerKey(serverKey)
                    .from(mCurrentRide.getDestination().getCoordinates())
                    .to(mCurrentRide.getPickup().getCoordinates())
                    .transportMode(TransportMode.DRIVING)
                    .execute(this);
        }
    }

    private List<Polyline> polylines = new ArrayList<>();

    /**
     * Remove route polylines from the map
     */
    private void erasePolylines() {
        if (polylines == null) {
            return;
        }
        for (Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();
    }

    /**
     * Show map within the pickup and destination marker,
     * This will make sure everything is displayed to the user
     *
     * @param route - route between pickup and destination
     */
    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    /**
     * Checks if route where fetched successfully, if yes then
     * add them to the map
     *
     * @param direction - direction object to the destination
     * @param rawBody   - data of the route
     */


    ArrayList<Double> routeData;

    /**
     * Checks if route where fetched successfully, if yes then
     * add them to the map
     *
     * @param direction - direction object to the destination
     * @param rawBody   - data of the route
     */
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);


            try {
                JSONObject obj = new JSONObject(rawBody);

                routeData = parseJson(obj);

                mAdapter.setData(routeData);
                mAdapter.notifyDataSetChanged();

                Log.d("My App", obj.toString());

            } catch (Throwable ignored) {
            }

            destinationMarker = mMap.addMarker(new MarkerOptions().position(destinationLocation.getCoordinates()).icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(CustomerMapActivity.this, destinationLocation.getName(), route.getLegList().get(0).getDuration().getText()))));

            List<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();

            MapAnimator.getInstance().animateRoute(mMap, directionPositionList);

            setCameraWithCoordinationBounds(route);
        }
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

    @Override
    public void onDirectionFailure(Throwable t) {



    }
    /**
     * Override the activity's onActivityResult(), check the request code, and
     * do something with the returned place data (in this example it's place name and place ID).
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            LocationObject mLocation;

            if (currentLocation == null) {
                Snackbar.make(findViewById(R.id.drawer_layout), "First Activate GPS", Snackbar.LENGTH_LONG).show();
                return;
            }
            Place place = Autocomplete.getPlaceFromIntent(data);

            mLocation = new LocationObject(place.getLatLng() ,place.getName());

            currentLocation = new LocationObject(new LatLng(currentLocation.getCoordinates().latitude, currentLocation.getCoordinates().longitude), "");

            destinationLocation = mLocation;
            if (requestCode == 1) {
                mMap.clear();
                destinationLocation = mLocation;

                destinationMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(CustomerMapActivity.this, destinationLocation.getName(), null))).position(destinationLocation.getCoordinates()));
                mCurrentRide.setDestination(destinationLocation);
                autocompleteFragmentTo.setText(destinationLocation.getName());
                if (pickupLocation != null) {
                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation.getCoordinates()).icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(CustomerMapActivity.this, pickupLocation.getName(), null))));
                    bringBottomSheetDown();
                }
            } else if (requestCode == 2) {
                mMap.clear();
                pickupLocation = mLocation;
                pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation.getCoordinates()).icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(CustomerMapActivity.this, pickupLocation.getName(), null))));
                mCurrentRide.setPickup(pickupLocation);
                autocompleteFragmentFrom.setText(pickupLocation.getName());
                Toast.makeText(this, "places"+pickupLocation.getName(), Toast.LENGTH_SHORT).show();

                if (destinationLocation != null) {
                    destinationMarker = mMap.addMarker(new MarkerOptions().position(destinationLocation.getCoordinates()).icon(BitmapDescriptorFactory.fromBitmap(generateBitmap(CustomerMapActivity.this, destinationLocation.getName(), null))));
                    bringBottomSheetDown();
                }
            }

            MapAnimator();
            getRouteToMarker();
            getDriversAround();

            mRequest.setText(getString(R.string.call_uber));


        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
            assert status.getStatusMessage() != null;
            Log.i("PLACE_AUTOCOMPLETE", status.getStatusMessage());
        } else if (resultCode == RESULT_CANCELED) {
            initPlacesAutocomplete();
        }
        initPlacesAutocomplete();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (bottomSheetStatus == 2 || bottomSheetStatus == 3) {
                bottomSheetStatus = 0;
                bringBottomSheetDown();
            } else {
              //  super.onBackPressed();
            }
        }
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.finishAffinity();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }




}
