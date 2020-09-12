package com.example.firestorequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private OnItemClickedInList onItemClickedInList;

    private List<ListQuizModel> listQuizModels;

    public ListAdapter(OnItemClickedInList onItemClickedInList){
        this.onItemClickedInList = onItemClickedInList;
    }

    public void setListQuizModels(List<ListQuizModel> listQuizModels) {
        this.listQuizModels = listQuizModels;

    }

    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAdapter.ListViewHolder holder, final int position) {
        holder.listTitle.setText(listQuizModels.get(position).getName());
        String desc = listQuizModels.get(position).getDesc();
        if(desc.length() > 150){
            desc = desc.substring(0,150);
        }

        holder.listDesc.setText(desc + "...");
        holder.listDiff.setText(listQuizModels.get(position).getLevel());
        Glide.with(holder.itemView).load(listQuizModels.get(position).getImage()).into(holder.listImage);

        holder.listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickedInList.getPositionOfList(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listQuizModels == null){
            return 0;
        } else {
            return listQuizModels.size();
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        private ImageView listImage;
        private TextView listTitle;
        private TextView listDesc;
        private TextView listDiff;
        private Button listBtn;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            listImage = itemView.findViewById(R.id.list_image);
            listTitle = itemView.findViewById(R.id.list_title);
            listDesc = itemView.findViewById(R.id.list_desc);
            listDiff = itemView.findViewById(R.id.list_diff);
            listBtn = itemView.findViewById(R.id.list_btn);
        }
    }

    public interface OnItemClickedInList{
        void getPositionOfList(int position);
    }
}
