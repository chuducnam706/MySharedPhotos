package com.example.storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.storage.databinding.ItemFileBinding;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private List<FolderModel> data = new ArrayList<>();
    private onClickItem clickItem;

    public void setClickItem(onClickItem clickItem) {
        this.clickItem = clickItem;
    }

    public void setData(List<FolderModel> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFileBinding binding = ItemFileBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FolderModel model = data.get(position);
        holder.bindView(model);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ItemFileBinding binding;

        public ViewHolder(ItemFileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FolderModel model = data.get(getAbsoluteAdapterPosition());
                    clickItem.onClick(model);
                }
            });
        }

        public void bindView(FolderModel model){
            binding.txtTitlePath.setText(model.getTitle());
            Glide.with(binding.getRoot().getContext()).load(model.getData()).into(binding.imgStorage);
        }

    }
}
