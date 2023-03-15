package com.example.fyrflyuser.fragments.login;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.FragmentVerifyOtpBinding;
import com.example.fyrflyuser.modelClasses.OtpRoot;
import com.example.fyrflyuser.mvvm.Mvvm;
import com.example.fyrflyuser.utils.App;
import com.example.fyrflyuser.utils.AppConstants;

public class VerifyOtpFragment extends Fragment {

    FragmentVerifyOtpBinding binding;

    public VerifyOtpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = binding.inflate(inflater, container, false);

        setOnClicks();
        setData();

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            binding.countryNameAndCode.setText(bundle.getString("country") + " " + "(" + bundle.getString("code") + ")");
            binding.phoneNumber.setText(bundle.getString("phone"));
        }


        //Toast.makeText(requireContext(), "code "+bundle.getString("country"), Toast.LENGTH_SHORT).show();
    }

    private void setOnClicks() {


        binding.verifyOtpBtn.setOnClickListener(v -> {

            Toast.makeText(requireContext(), "otp" + getArguments().getString("otp"), Toast.LENGTH_SHORT).show();

            Toast.makeText(requireContext(), "ok", Toast.LENGTH_SHORT).show();

            new Mvvm().otpVerify(requireActivity(), getArguments().getString("phone"), binding.enterOtp.getText().toString()).observe(requireActivity(), new Observer<OtpRoot>() {
                @Override
                public void onChanged(OtpRoot map) {

                    if (map != null) {
                        if (map.getStatus() == 1) {

                            Toast.makeText(requireActivity(), "Otp = " + map.getMessage(), Toast.LENGTH_SHORT).show();
                            App.getPrefManager().saveString(AppConstants.SESSION, "1");

                            App.getPrefManager().saveString(AppConstants.USER_ID, map.getDetails().getId());
                            App.getPrefManager().saveString(AppConstants.LOGIN_TOKEN, map.getDetails().getAuth_token());

                            App.getPrefManager().saveModel(AppConstants.USER_INFO, map.getDetails());
                            Navigation.findNavController(getView()).navigate(R.id.action_verifyOtpFragment_to_customerMapActivity);
                        } else {
                            binding.enterOtp.setError("Otp not matched");
                            Toast.makeText(requireContext(), "1" + map.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Root is null", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        });
    }

}