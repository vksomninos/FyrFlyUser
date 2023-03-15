package com.example.fyrflyuser.fragments.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.activites.LoginActivity;
import com.example.fyrflyuser.adeptars.AdapterSearchHistory;
import com.example.fyrflyuser.databinding.FragmentSearchDestinationBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class SearchDestinationFragment extends BottomSheetDialogFragment {
    FragmentSearchDestinationBinding binding;
    AdapterSearchHistory adapter ;

    public SearchDestinationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      onClicks();
        initRV();

    }

    private void onClicks() {
        binding.from.setOnClickListener(v -> {

         //   Navigation.findNavController(v).navigate(R.id.searchFragment);
            NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.searchFragment);


        });

        binding.to.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(requireActivity(), R.id.frameLayoutHome);
            navController.navigateUp();
            navController.navigate(R.id.searchFragment);
        });

    }

    private void initRV() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        adapter = new AdapterSearchHistory();
        binding.itemsSearchHistory.setNestedScrollingEnabled(true);
        binding.itemsSearchHistory.setLayoutManager(mLayoutManager);
        binding.itemsSearchHistory.setItemViewCacheSize(50);
        binding.itemsSearchHistory.setAdapter(adapter);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}