package com.example.india11.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.india11.Model.SliderModel;
import com.example.india11.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {
    private Context context;
    private List<SliderModel> sliderModelList = new ArrayList<>();

    public ImageSliderAdapter(Context context, List<SliderModel> sliderModelList) {
        this.context = context;
        this.sliderModelList = sliderModelList;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout,null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        final SliderModel sliderModel = sliderModelList.get(position);
        Glide.with(context).load(sliderModel.getImageUrl()).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }

    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageView;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            this.itemView = itemView;
        }
    }
}
