package com.example.androidrestaurantbooking.Interface;

import com.example.androidrestaurantbooking.Model.Banner;

import java.util.List;

public interface IMenuLoadListener {
    void onmenurLoadSuccess(List<Banner> banners);
    void onmenuLoadFailed(String message);
}
