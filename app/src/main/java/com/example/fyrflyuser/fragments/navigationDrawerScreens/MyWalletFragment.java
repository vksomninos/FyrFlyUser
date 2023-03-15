package com.example.fyrflyuser.fragments.navigationDrawerScreens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.FragmentMyWalletBinding;

public class MyWalletFragment extends Fragment {

    FragmentMyWalletBinding binding ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyWalletBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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

    @Override
    public void onResume() {
        super.onResume();


    }

}