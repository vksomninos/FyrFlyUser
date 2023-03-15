package com.example.fyrflyuser.adeptars;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyrflyuser.databinding.ChatListBinding;
import com.example.fyrflyuser.databinding.ItemsSearchHistoryBinding;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyViewHolder> {
    @NonNull
    @Override
    public AdapterChat.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterChat.MyViewHolder(ChatListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChat.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ChatListBinding binding ;
        public MyViewHolder(@NonNull ChatListBinding itemView) {
            super(itemView.getRoot());

            binding = itemView ;
        }
    }
}
