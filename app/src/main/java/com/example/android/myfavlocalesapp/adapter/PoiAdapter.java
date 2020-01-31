package com.example.android.myfavlocalesapp.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.myfavlocalesapp.model.PlacesResponse;

import java.util.List;

public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.PoiAdapterViewHolder>{
    List <PlacesResponse> PoiList;


    @NonNull
    @Override
    public PoiAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PoiAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return PoiList.size();
    }

    class PoiAdapterViewHolder extends RecyclerView.ViewHolder{

        public PoiAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
