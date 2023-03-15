package com.example.fyrflyuser.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.ActivityHomeDriverBinding;
import com.example.fyrflyuser.modelClasses.LoginRoot;
import com.example.fyrflyuser.modelClasses.OtpRoot;
import com.example.fyrflyuser.modelClasses.UserDetailsRoot;
import com.example.fyrflyuser.modelClasses.onlineOfflineRoot;
import com.example.fyrflyuser.mvvm.Mvvm;
import com.example.fyrflyuser.mvvm.MvvmDriver;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeDriverActivity extends AppCompatActivity {
    ActivityHomeDriverBinding binding;
    View headerLayout;
    int onlineStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeDriverBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setNaigationDrwer();
        onClick();
        apiGetDetails();


    }

    private void apiGetDetails() {

        new Mvvm().userDetails(HomeDriverActivity.this,
                App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN),
                CommonUtils.getUserId()).observe(HomeDriverActivity.this,
                new Observer<UserDetailsRoot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(UserDetailsRoot profileRoot) {
                        if (profileRoot != null) {

                            if (profileRoot.getStatus() == 1) {
                                setData(profileRoot.getDetails());
                            } else {
                                Toast.makeText(HomeDriverActivity.this, "get Profile Fail", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                        }
                    }
                });
    }

    private void setData(UserDetailsRoot.Details details) {
        NavigationView navigationView = findViewById(R.id.navigationDriver);
        View header = navigationView.getHeaderView(0);
        CircleImageView userImage = header.findViewById(R.id.profile_photo);
        Glide.with(HomeDriverActivity.this).load(details.getProfileImage()).placeholder(R.drawable.home_icon).into(userImage);
        setClicksOnNavigationItems(header, details);


    }

    private void setClicksOnNavigationItems(View headerview, UserDetailsRoot.Details details) {

        LinearLayout profile = headerview.findViewById(R.id.ProfileLay);
        LinearLayout makeBooking = headerview.findViewById(R.id.makeBooking);
        LinearLayout converToDriver = headerview.findViewById(R.id.converToDriver);
        LinearLayout myWallet = headerview.findViewById(R.id.myWalletLin);
        LinearLayout pushNotification = headerview.findViewById(R.id.pushNotification);
        LinearLayout info = headerview.findViewById(R.id.info);
        LinearLayout refer = headerview.findViewById(R.id.refer);
        LinearLayout emergencylin = headerview.findViewById(R.id.emergencylin);
        LinearLayout logout = headerview.findViewById(R.id.logout);
        profile.setOnClickListener(v -> {

            Intent intent = new Intent(HomeDriverActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "1");
            startActivity(intent);


        });

        makeBooking.setOnClickListener(v -> {
            Intent intent = new Intent(HomeDriverActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "2");
            startActivity(intent);


        });

        myWallet.setOnClickListener(v -> {
            Intent intent = new Intent(HomeDriverActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "3");
            startActivity(intent);


        });

        converToDriver.setOnClickListener(v -> {

            Intent intent = new Intent(HomeDriverActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "4");
            intent.putExtra("driver_status", details.getDriverStatus());
            startActivity(intent);
            //     binding.drawerLayout.

        });




            emergencylin.setOnClickListener(v1 -> {
                final Dialog dialog = new Dialog(HomeDriverActivity.this);
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




        pushNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HomeDriverActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "7");
            intent.putExtra("driver_status", details.getDriverStatus());
            startActivity(intent);

        });
        info.setOnClickListener(v -> {
            Intent intent = new Intent(HomeDriverActivity.this, MainActivity.class);
            intent.putExtra("FragmentType", "8");
            intent.putExtra("driver_status", details.getDriverStatus());
            startActivity(intent);

        });

        logout.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(HomeDriverActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.logout_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView cancel = dialog.findViewById(R.id.cancel);
            TextView ok = dialog.findViewById(R.id.ok);

            cancel.setOnClickListener(view1 -> {
                dialog.dismiss();



            });
            ok.setOnClickListener(view1 -> {
                new Mvvm().logOut(HomeDriverActivity.this,
                        App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN),
                        App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN)).observe(HomeDriverActivity.this, new Observer<LoginRoot>() {
                    @Override
                    public void onChanged(LoginRoot loginRoot) {
                        if (loginRoot != null) {
                            if (loginRoot.getStatus() == 1) {
                                Toast.makeText(HomeDriverActivity.this, "" + loginRoot.getMessage(), Toast.LENGTH_SHORT).show();
                                App.getPrefManager().clearPreferences();

                                Intent switchActivityIntent = new Intent(HomeDriverActivity.this, LoginActivity.class);
                                startActivity(switchActivityIntent);


                            } else {
                                Toast.makeText(HomeDriverActivity.this, "" + loginRoot.getMessage(), Toast.LENGTH_SHORT).show();


                            }
                        } else {
                            Toast.makeText(HomeDriverActivity.this, "root is null", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
            });
            dialog.show();

        });

    }

    private void onClick() {
        binding.onlineDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Map<String, Object> newUserMap = new HashMap<>();
                if (isChecked) {
                    onlineStatus = 1;
                    apiOnlineOffline(onlineStatus);
                    newUserMap.put("onlineStatus", "online");
                    FirebaseDatabase.getInstance().getReference().child("driverOnlineStatus").child(CommonUtils.getUserId()).setValue(newUserMap);
                    Toast.makeText(HomeDriverActivity.this, "ok", Toast.LENGTH_SHORT).show();
                } else {

                    onlineStatus = 0;
                    apiOnlineOffline(onlineStatus);
                    newUserMap.put("onlineStatus", "offline");
                    FirebaseDatabase.getInstance().getReference().child("driverOnlineStatus").child(CommonUtils.getUserId()).setValue(newUserMap);

                }
            }
        });

    }

    private void apiOnlineOffline(int status) {
        Toast.makeText(this, "ok" + App.getPrefManager().getString(AppConstants.LOGIN_TOKEN), Toast.LENGTH_SHORT).show();
        Log.d("token", "ok" + App.getPrefManager().getString(AppConstants.LOGIN_TOKEN));
        new MvvmDriver().OnlineOffline(HomeDriverActivity.this,
                App.getPrefManager().getString(AppConstants.LOGIN_TOKEN),
                CommonUtils.getUserId(), status).observe(HomeDriverActivity.this, new Observer<onlineOfflineRoot>() {
            @Override
            public void onChanged(onlineOfflineRoot loginRoot) {
                if (loginRoot != null) {
                    if (loginRoot.getStatus() == 1) {
                        Toast.makeText(HomeDriverActivity.this, "" + loginRoot.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeDriverActivity.this, "" + loginRoot.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeDriverActivity.this, "root is null", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void setNaigationDrwer() {

        binding.menu.setOnClickListener(v -> {
            binding.mainDrawer.openDrawer(GravityCompat.START);
        });

        headerLayout = binding.navigationDriver.inflateHeaderView(R.layout.menu_page);
        View headerview = binding.navigationDriver.getHeaderView(0);

        setOnClick(headerview);
        setText(headerLayout);

    }

    private void setText(View headerLayout) {

        TextView converter = (TextView) headerLayout.findViewById(R.id.invite_);
        converter.setText("Convert to User");

    }

    private void setOnClick(View headerLayout) {

        LinearLayout converToDriver = headerLayout.findViewById(R.id.converToDriver);
        converToDriver.setOnClickListener(v -> {
            Intent i = new Intent(HomeDriverActivity.this, CustomerMapActivity.class);
            startActivity(i);
        });
    }


}