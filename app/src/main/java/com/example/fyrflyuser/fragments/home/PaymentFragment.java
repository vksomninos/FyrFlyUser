package com.example.fyrflyuser.fragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.FragmentPaymentBinding;


public class PaymentFragment extends Fragment {


    FragmentPaymentBinding binding ;
    public PaymentFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(inflater, container, false);


        return  binding.getRoot();
    }
}