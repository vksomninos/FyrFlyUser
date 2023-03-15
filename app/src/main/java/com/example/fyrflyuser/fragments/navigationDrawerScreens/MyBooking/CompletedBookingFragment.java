package com.example.fyrflyuser.fragments.navigationDrawerScreens.MyBooking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.adeptars.BookingComplete;

public class CompletedBookingFragment extends Fragment {
        RecyclerView recyclerView;
        RelativeLayout relativeLayout;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_completed_booking, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            recyclerView=view.findViewById(R.id.recyclar);
            relativeLayout=view.findViewById(R.id.rel2);
            BookingComplete bookingComplete=new BookingComplete();
            recyclerView.setAdapter(bookingComplete);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFramlayout,new Screen238Fragment()).addToBackStack(null).commit();
                }
            });
        }
    }