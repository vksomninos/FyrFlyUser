package com.example.fyrflyuser.fragments.login;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.FragmentLoginBinding;
import com.example.fyrflyuser.modelClasses.LoginRoot;
import com.example.fyrflyuser.modelClasses.OtpRoot;
import com.example.fyrflyuser.mvvm.Mvvm;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;


public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    private String phone;
    String MobilePattern = "[0-9]{10}";

    Mvvm viewModel ;

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private CountryCodePicker ccp;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = binding.inflate(inflater, container, false);
        onClick();
        printHashKey();
        FacebookSdk.sdkInitialize(requireActivity());
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();


        return binding.getRoot();
    }


    private void onClick() {

        binding.facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager.logInWithReadPermissions(
                        requireActivity(),
                        Arrays.asList(
                                "email",
                                "public_profile",
                                "user_birthday"));
            }
        });




        binding.signIn.setOnClickListener(view -> {
            phone = binding.editTxtPhone.getText().toString();
            if (phone.isEmpty() || !binding.editTxtPhone.getText().toString().matches(MobilePattern)) {
                binding.editTxtPhone.setError("required 10 digits");
                binding.editTxtPhone.setTextColor(Color.RED);

                binding.editTxtPhone.requestFocus();
            }
            else {
                apiRegister(phone);
                //  Toast.makeText(requireActivity(), "ok", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void apiRegister(String phone) {
        String code = "+" + binding.ccp.getSelectedCountryCode();

        new Mvvm().LoginPhone(requireActivity(), phone).observe(requireActivity(), new Observer<LoginRoot>() {
            @Override
            public void onChanged(LoginRoot map) {
                if(map !=null)
                {
                if (map.getStatus() == 1) {


                    Toast.makeText(requireActivity(), "Otp = "+map.getDetails(), Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("code",code);
                    bundle.putString("country",binding.ccp.getSelectedCountryName());
                    bundle.putString("phone",phone);
                    bundle.putString("otp",map.getDetails());
                    bundle.putString(AppConstants.PHONE,code+phone);
                //    bundle.putString(AppConstants.COUNTRY_NAME,ccp.getSelectedCountryName());


                    Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_verifyOtpFragment, bundle);

                }

            }else {
                    Toast.makeText(requireActivity(), "root is null",Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void facebookLogin() {
        loginManager
                = LoginManager.getInstance();
        callbackManager
                = CallbackManager.Factory.create();

        loginManager
                .registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                                            @Override
                                            public void onCompleted(JSONObject object,
                                                                    GraphResponse response) {

                                                if (object != null) {
                                                    try {
                                                        String personName = object.getString("name");
                                                        String personEmail = object.getString("email");
                                                        String fbUserID = object.getString("id");

                                                        // socialLoginWithGoogleandFacebookCaller(personEmail, personName, fbUserID);


                                                        // do action after Facebook login success
                                                        // or call your API
                                                    } catch (JSONException | NullPointerException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString(
                                        "fields",
                                        "id, name, email, gender, birthday");
                                request.setParameters(parameters);
                                request.executeAsync();

                            }

                            @Override
                            public void onCancel() {
                                Log.v("LoginScreen", "---onCancel");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                // here write code when get error
                                Log.v("LoginScreen", "----onError: "
                                        + error.getMessage());
                            }
                        });

    }

    private void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }


    private void printHashKey() {

        try {

            PackageInfo info
                    = requireActivity().getPackageManager().getPackageInfo(
                    "com.example.fyrflyuser",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md
                        = MessageDigest.getInstance("mGbGfJ7nB7x1/6b4UdDude7YXZc=");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(
                                md.digest(),
                                Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }


}