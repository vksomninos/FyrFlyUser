package com.example.fyrflyuser.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.fragments.driverPart.DriverHomeFragment;
import com.example.fyrflyuser.fragments.navigationDrawerScreens.AboutUsFragment;
import com.example.fyrflyuser.fragments.navigationDrawerScreens.ConvertToDriversFragment;
import com.example.fyrflyuser.fragments.navigationDrawerScreens.MyBooking.MyBookingFragment;
import com.example.fyrflyuser.fragments.navigationDrawerScreens.MyWalletFragment;
import com.example.fyrflyuser.fragments.navigationDrawerScreens.ProfileFragment;
import com.example.fyrflyuser.fragments.navigationDrawerScreens.PushNotificationsFragment;
import com.example.fyrflyuser.modelClasses.UserDetailsRoot;
import com.example.fyrflyuser.mvvm.Mvvm;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
// TODO:  this activity only used for holding all navigation drawers screens
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragments();
    }


    private void setFragments() {

        String fragmnetType = getIntent().getExtras().getString("FragmentType");
        String driver_status = getIntent().getExtras().getString("driver_status");

        if(fragmnetType.equalsIgnoreCase("1"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame , new ProfileFragment()).addToBackStack(null).commit();
        }else  if(fragmnetType.equalsIgnoreCase("2"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame , new MyBookingFragment()).addToBackStack(null).commit();

        }else  if(fragmnetType.equalsIgnoreCase("3"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame , new MyWalletFragment()).addToBackStack(null).commit();

        }else if(fragmnetType.equalsIgnoreCase("4") || driver_status.equalsIgnoreCase("approved"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame , new ConvertToDriversFragment()).addToBackStack(null).commit();

        }else if(fragmnetType.equalsIgnoreCase("4") || driver_status.equalsIgnoreCase("pending")){

            Toast.makeText(this, "Driver Request is Pending ", Toast.LENGTH_SHORT).show();
        }else if(fragmnetType.equalsIgnoreCase("4") || driver_status.isEmpty()){

            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame , new ConvertToDriversFragment()).addToBackStack(null).commit();
        }else  if(fragmnetType.equalsIgnoreCase("7"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame , new PushNotificationsFragment()).addToBackStack(null).commit();
        }else  if(fragmnetType.equalsIgnoreCase("8"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame , new AboutUsFragment()).addToBackStack(null).commit();
        }

    }
}