package com.example.fyrflyuser.fragments.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.adeptars.AdapterSearchHistory;
import com.example.fyrflyuser.adeptars.AdapterSelectCar;
import com.example.fyrflyuser.databinding.FragmentSelectCarBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class SelectCarFragment extends BottomSheetDialogFragment {

    FragmentSelectCarBinding selectCarBinding;
    AdapterSelectCar adapter;

    private LinearLayout mBottomSheetLayout;
    private BottomSheetBehavior sheetBehavior;
    private View header_Arrow_Image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        selectCarBinding = FragmentSelectCarBinding.inflate(inflater, container, false);

        return selectCarBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBottomSheetLayout = view.findViewById(R.id.bottom_sheet_layout);
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        header_Arrow_Image = view.findViewById(R.id.viewpull);
        setOnClick();
        setAdapter();
        Toast.makeText(requireContext(), "onViewCrted", Toast.LENGTH_SHORT).show();

        header_Arrow_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                header_Arrow_Image.setRotation(slideOffset * 180);
            }
        });


    }

    private void setOnClick() {

        selectCarBinding.confirmRide.setOnClickListener(v -> {
            selectCarBinding.confirmSelectedRide2.setVisibility(View.VISIBLE);
            selectCarBinding.confirmRide.setVisibility(View.GONE);

        });
    }

    private void setAdapter() {
        adapter = new AdapterSelectCar();

        selectCarBinding.selectCarRv.setAdapter(adapter);
    }
}