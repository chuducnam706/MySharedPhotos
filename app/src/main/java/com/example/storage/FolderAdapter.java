package com.example.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storage.databinding.ItemFolderBinding;

import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private List<FolderData> data = new ArrayList<>();
    private onClickItem onClickItem;

    public void setOnClickItem(onClickItem listener) {
        this.onClickItem = listener;
    }

    public void setData(List<FolderData> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFolderBinding binding = ItemFolderBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FolderData model = data.get(position);
        holder.bindView(model);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemFolderBinding binding;

        public ViewHolder(@NonNull ItemFolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FolderData item = data.get(getAbsoluteAdapterPosition());
                    onClickItem.onClick(item);
                }
            });
        }

        public void bindView(FolderData model) {
            binding.imgLogo.setImageResource(R.drawable.open_folder);
            binding.txtTitle.setText(model.getName());
            binding.txtNumber.setText(model.getImages().size() + " ảnh");


        }
    }

}
