package com.jfsb.antwortn.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jfsb.antwortn.R;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {

    int colors[];
    String titles[];

    public SliderAdapter(int[] colors, String[] titles) {
        this.colors = colors;
        this.titles = titles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(titles[position]);
        holder.image.setBackgroundResource(R.drawable.antwort_iso);
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //View view;
        TextView title;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title_view);
            //view = itemView.findViewById(R.id.view);
        }
    }
}
