package com.example.fyrflyuser.fragments.navigationDrawerScreens;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyrflyuser.R;
import com.example.fyrflyuser.activites.CustomerMapActivity;
import com.example.fyrflyuser.activites.LoginActivity;
import com.example.fyrflyuser.activites.MainActivity;
import com.example.fyrflyuser.databinding.FragmentProfileBinding;
import com.example.fyrflyuser.modelClasses.LoginRoot;
import com.example.fyrflyuser.modelClasses.ProfileRoot;
import com.example.fyrflyuser.modelClasses.UserDetailsRoot;
import com.example.fyrflyuser.mvvm.Mvvm;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;
import com.example.fyrflyuser.utils.CommonUtils;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setOnClicks();
        getDetal();
        backPressed(view);

    }

    private void backPressed(View sView) {

        sView.setFocusableInTouchMode(true);

        sView.requestFocus();

        sView.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    if (i == KeyEvent.KEYCODE_BACK) {
                     requireActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void getDetal() {

        new Mvvm().userDetails(requireActivity(),
                App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN),
                CommonUtils.getUserId()).observe(requireActivity(),
                new Observer<UserDetailsRoot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(UserDetailsRoot profileRoot) {
                        if (profileRoot != null) {

                            if (profileRoot.getStatus() == 1) {
                                Glide.with(getContext()).load(profileRoot.getDetails().getProfileImage()).placeholder(R.drawable.home_icon).into(binding.userPro);

                                binding.userName.setText(profileRoot.getDetails().getFirstname()
                                        + " " + profileRoot.getDetails().getLastname());
                                binding.emailEdt.setText(profileRoot.getDetails().getEmail());
                                binding.phoneEdt.setText(profileRoot.getDetails().getPhone());
                                binding.userType.setText(profileRoot.getDetails().getUserType());
                            } else {
                                Toast.makeText(requireContext(), "get Profile Fail", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                        }
                    }
                });
    }

    private void setOnClicks() {

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_updateProfileFragment);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame , new UpdateProfileFragment()).addToBackStack(null).commit();
            }
        });

        binding.logOutRel.setOnClickListener(v -> {

            new Mvvm().logOut(requireActivity(),
                    App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN), App.getPrefManager().getPreferences(AppConstants.LOGIN_TOKEN)).observe(requireActivity(), new Observer<LoginRoot>() {
                @Override
                public void onChanged(LoginRoot loginRoot) {
                    if (loginRoot != null) {
                        if (loginRoot.getStatus() == 1) {
                            Toast.makeText(requireContext(), "" + loginRoot.getMessage(), Toast.LENGTH_SHORT).show();
                            App.getPrefManager().clearPreferences();
                            Intent intent = new Intent( requireContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(requireContext(), "" + loginRoot.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    } else {
                        Toast.makeText(requireContext(), "root is null", Toast.LENGTH_SHORT).show();


                    }
                }
            });

        });
    }

    private void logout() {


    }

    @Override
    public void onResume() {
        super.onResume();

    //    requireActivity().findViewById(R.id.actionBar).setVisibility(View.GONE);

    }


}