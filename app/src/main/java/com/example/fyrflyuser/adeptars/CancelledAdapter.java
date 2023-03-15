package com.example.fyrflyuser.adeptars;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.CancelledBinding;
import com.example.fyrflyuser.databinding.SelectCarItemsBinding;


public class CancelledAdapter extends RecyclerView.Adapter<CancelledAdapter.holder> {
    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CancelledAdapter.holder(CancelledBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class holder extends RecyclerView.ViewHolder {
        public holder(@NonNull CancelledBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
