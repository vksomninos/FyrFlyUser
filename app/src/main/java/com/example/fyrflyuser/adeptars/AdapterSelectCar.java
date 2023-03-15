package com.example.fyrflyuser.adeptars;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyrflyuser.R;
import com.example.fyrflyuser.databinding.SelectCarItemsBinding;

public class AdapterSelectCar extends RecyclerView.Adapter<AdapterSelectCar.MyViewHolder> {
    int selectedItemPosition = RecyclerView.NO_POSITION;

    @NonNull
    @Override
    public AdapterSelectCar.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterSelectCar.MyViewHolder(SelectCarItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterSelectCar.MyViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SelectCarItemsBinding binding;

        public MyViewHolder(@NonNull SelectCarItemsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(int position) {
            if (position == selectedItemPosition) {
                binding.selectCarlay.setBackgroundResource(R.drawable.selected_car_layout);

            } else {
                binding.selectCarlay.setBackgroundColor(Color.WHITE);
                binding.selectCarlay.setElevation(0);


            }
            binding.selectCarlay.setOnClickListener(v -> {
                if (selectedItemPosition != getAdapterPosition()) {
                    binding.selectCarlay.setBackgroundResource(R.drawable.selected_car_layout);

                    binding.selectCarlay.setElevation(6);
                    if (selectedItemPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(selectedItemPosition);
                    }
                    selectedItemPosition = getAdapterPosition();
                } else {
                    binding.selectCarlay.setBackgroundColor(Color.WHITE);
                    binding.selectCarlay.setElevation(0);

                }


            });
        }
    }
}
