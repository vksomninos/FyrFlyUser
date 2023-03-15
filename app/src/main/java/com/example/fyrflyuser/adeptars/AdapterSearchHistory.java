package com.example.fyrflyuser.adeptars;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyrflyuser.databinding.ItemsSearchHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearchHistory extends RecyclerView.Adapter<AdapterSearchHistory.MyViewHolder> {

    Context context ;


    @NonNull
    @Override
    public AdapterSearchHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterSearchHistory.MyViewHolder(ItemsSearchHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemsSearchHistoryBinding binding ;

        public MyViewHolder(@NonNull ItemsSearchHistoryBinding itemView) {
            super(itemView.getRoot());

            binding = itemView ;

        }

    }
}
