package com.example.fyrflyuser.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;

import okhttp3.internal.http2.Header;

public class LoginActivity extends AppCompatActivity {
   public static  int SPLASH_SCREEN_TIME_OUT = 2000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(App.getPrefManager().getPreferences(AppConstants.SESSION).equals("1"))

                {
                    Intent intent = new Intent( LoginActivity.this,CustomerMapActivity.class);
                    startActivity(intent);
                }else {
                    NavController navController = Navigation.findNavController(LoginActivity.this, R.id.frameLayout);
                    navController.navigateUp();
                    navController.navigate(R.id.loginFragment);
                }

            }


        }, SPLASH_SCREEN_TIME_OUT);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}