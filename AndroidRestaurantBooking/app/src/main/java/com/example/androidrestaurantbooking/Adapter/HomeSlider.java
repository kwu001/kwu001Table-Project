package com.example.androidrestaurantbooking.Adapter;

import com.example.androidrestaurantbooking.Model.Banner;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class HomeSlider extends SliderAdapter {

   List<Banner>bannerList;
    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
      imageSlideViewHolder.bindImageSlide(bannerList.get(position).getImage());
    }
}
