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
import com.example.fyrflyuser.adeptars.CancelledAdapter;
import com.example.fyrflyuser.databinding.FragmentCancelledBookingBinding;


public class CancelledBookingFragment extends Fragment {
     FragmentCancelledBookingBinding  binding ;
    RecyclerView recyclar;
    RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =binding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclar=view.findViewById(R.id.recyclars);
        relativeLayout=view.findViewById(R.id.rel2);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFramlayout,new Screen243Fragment()).addToBackStack(null).commit();
            }
        });
        CancelledAdapter cancelledAdapter=new CancelledAdapter();
        recyclar.setAdapter(cancelledAdapter);
    }
}