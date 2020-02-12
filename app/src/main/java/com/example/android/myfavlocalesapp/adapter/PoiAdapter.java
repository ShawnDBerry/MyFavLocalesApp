package com.example.android.myfavlocalesapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.myfavlocalesapp.R;
import com.example.android.myfavlocalesapp.model.GoogleLocation;
import com.example.android.myfavlocalesapp.model.Result;
import com.example.android.myfavlocalesapp.util.Constants;

import java.util.List;

public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.PoiAdapterViewHolder>{
    List <Result> poiList;
    Context applicationContext;

    public PoiAdapter(List<Result> poiList) {
        this.poiList = poiList;
    }

    @NonNull
    @Override
    public PoiAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        applicationContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poi_list_item, parent, false);
        return new PoiAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoiAdapterViewHolder holder, int position) {

        holder.poiName.setText(poiList.get(position).getName());


        try {
            Glide.with(applicationContext).load(
                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                            + poiList.get(position).getPhotos().get(0)
                            .getPhotoReference() + "&key=" + Constants.API_KEY)
                    .into(holder.poiImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }

    class PoiAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView poiImage;
        TextView poiName;


        public PoiAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            poiName = itemView.findViewById(R.id.poi_name);
            poiImage = itemView.findViewById(R.id.poi_imageview);

        }
    }
}
