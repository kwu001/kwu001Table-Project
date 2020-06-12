package com.example.androidrestaurantbooking.Interface;

import com.example.androidrestaurantbooking.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner>banners);
    void onBannerLoadFailed(String message);
}
