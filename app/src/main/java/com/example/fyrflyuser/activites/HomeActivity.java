package com.example.fyrflyuser.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.ActivityHomeBinding;
import com.example.fyrflyuser.modelClasses.UserDetailsRoot;
import com.example.fyrflyuser.mvvm.Mvvm;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    List<UserDetailsRoot.Details>  profileRoot ;
    String[] permission = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_NETWORK_STATE};
    View headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
       hideStatusBar();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission, 10);
        }
        apiGetDetails();
        setNaigationDrwer((UserDetailsRoot.Details) profileRoot);
        onClick();
    }

    private void hideStatusBar() {

    }

    private void onClick() {

        binding.menurelative.setOnClickListener(v -> {
            binding.mainDrawerUser.openDrawer(GravityCompat.START);
        });
        binding.mainDrawerUser.addDrawerListener(new DrawerLayout.DrawerListener() {
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
   binding.home.setOnClickListener(v ->
   {
       Intent intent = new Intent(HomeActivity.this, CustomerMapActivity.class);
       startActivity(intent);


   });


    }

    private void apiGetDetails() {
        new Mvvm().userDetails(HomeActivity.this,
                App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN),
                CommonUtils.getUserId()).observe(HomeActivity.this,
                new Observer<UserDetailsRoot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(UserDetailsRoot profileRoot) {
                        if (profileRoot != null) {

                            if (profileRoot.getStatus() == 1) {
                                setData(profileRoot.getDetails());
                                setNaigationDrwer(profileRoot.getDetails());
                            } else {
                                Toast.makeText(HomeActivity.this, "get Profile Fail", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                        }
                    }
                });
    }


    private void setNaigationDrwer(UserDetailsRoot.Details details) {
         headerLayout = binding.naigationUser.inflateHeaderView(R.layout.menu_page);
         View headerview = binding.naigationUser.getHeaderView(0);

        setOnClick(headerview, details);
    }

    @SuppressLint("SetTextI18n")
    private void setData(UserDetailsRoot.Details details) {
        TextView name = headerLayout.findViewById(R.id.text_Name);
        TextView email = headerLayout.findViewById(R.id.emailEdt);
        CircleImageView userImage  = headerLayout.findViewById(R.id.profile_photo);
        name.setText(details.getFirstname()+" "+details.getLastname());
        email.setText(details.getEmail());
        Glide.with(HomeActivity.this).load(details.getProfileImage()).placeholder(R.drawable.home_icon).into(userImage);

    }


    private void setOnClick(View headerview, UserDetailsRoot.Details details) {

        LinearLayout profile = headerview.findViewById(R.id.ProfileLay);
        LinearLayout makeBooking = headerview.findViewById(R.id.makeBooking);
        LinearLayout converToDriver = headerview.findViewById(R.id.converToDriver);
        LinearLayout myWallet = headerview.findViewById(R.id.myWalletLin);
        LinearLayout pushNotification = headerview.findViewById(R.id.pushNotification);
        LinearLayout info = headerview.findViewById(R.id.info);
        LinearLayout emergencylin = headerview.findViewById(R.id.emergencylin);
        profile.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(HomeActivity.this, R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_profileFragment);
            binding.mainDrawerUser.closeDrawer(GravityCompat.START);

        });

        makeBooking.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(HomeActivity.this, R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_myBookingFragment);
            binding.mainDrawerUser.closeDrawer(GravityCompat.START);

        });

        myWallet.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(HomeActivity.this, R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_myWalletFragment);
            binding.mainDrawerUser.closeDrawer(GravityCompat.START);

        });

        pushNotification.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(HomeActivity.this, R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_pushNotificationsFragment);
            binding.mainDrawerUser.closeDrawer(GravityCompat.START);

        });

        converToDriver.setOnClickListener(v -> {
          if(details.getDriverStatus().equalsIgnoreCase("pending")) {
              Intent i = new Intent(HomeActivity.this, HomeDriverActivity.class);
              startActivity(i);
          }else{

              NavController navController = Navigation.findNavController(HomeActivity.this, R.id.frameLayoutHome);
              navController.navigateUp();
              navController.navigate(R.id.convertToDriversFragment);
              binding.mainDrawerUser.closeDrawer(GravityCompat.START);
          }
        });

        info.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(HomeActivity.this, R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.action_homeFragment2_to_aboutUsFragment);
            binding.mainDrawerUser.closeDrawer(GravityCompat.START);

        });

        emergencylin.setOnClickListener(v -> {

            emergencylin.setOnClickListener(v1 -> {
                    final Dialog dialog = new Dialog(HomeActivity.this);
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

    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.actionBar.setVisibility(View.VISIBLE);

    }

    public void openDrawer(View view) {


    }
}