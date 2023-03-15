package com.example.fyrflyuser.fragments.navigationDrawerScreens.MyBooking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.FragmentMyBookingBinding;
import com.example.fyrflyuser.fragments.navigationDrawerScreens.MyBooking.CancelledBookingFragment;
import com.google.android.material.tabs.TabLayout;


public class MyBookingFragment extends Fragment {
    FragmentMyBookingBinding binding;

    public MyBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTabBooking(view);
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

    private void setTabBooking(View view) {

        binding.tabBooking.addTab(binding.tabBooking.newTab().setText("Active Now"));
        binding.tabBooking.addTab(binding.tabBooking.newTab().setText("Completed"));
        binding.tabBooking.addTab(binding.tabBooking.newTab().setText("Cancelled"));

        final PagerAdapter pagerAdapter = new Adapter_Booking(getChildFragmentManager(), binding.tabBooking.getTabCount());
        binding.viewpagerBooking.setAdapter(pagerAdapter);
        binding.viewpagerBooking.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabBooking));
        binding.tabBooking.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.viewpagerBooking));
    }

    public static class Adapter_Booking extends FragmentStatePagerAdapter {
        private final int totalTabs;


        public Adapter_Booking(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            totalTabs = behavior;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ActiveBookingFragment();
                case 1:
                    return new CancelledBookingFragment();
                case 2:
                    return new CompletedBookingFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return totalTabs;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
      //  requireActivity().findViewById(R.id.actionBar).setVisibility(View.GONE);

    }

}