package com.example.androidrestaurantbooking.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidrestaurantbooking.Common.Common;
import com.example.androidrestaurantbooking.Interface.IBannerLoadListener;
import com.example.androidrestaurantbooking.Interface.IMenuLoadListener;
import com.example.androidrestaurantbooking.Model.Banner;
import com.example.androidrestaurantbooking.R;
import com.example.androidrestaurantbooking.Service.RestaurantImageLoadingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;


public class HomeFragment extends Fragment implements IMenuLoadListener, IBannerLoadListener {

    private Unbinder unbinder;
    @BindView(R.id.layout_user_information)
    LinearLayout layout_user_information;
    @BindView(R.id.text_user_name)
    TextView text_user_name;
    @BindView(R.id.banner_slider)
    Slider banner_slider;
    @BindView(R.id.recycle_restaurant)
    RecyclerView recycle_restaurant;

    //firestore
    CollectionReference bannerRef, menuRef;

    //Interface
    IBannerLoadListener iBannerLoadListener;
     IMenuLoadListener iMenuLoadListener;





    public HomeFragment() {
        // Required empty public constructor
        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        menuRef = FirebaseFirestore.getInstance().collection("menu");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home, container, false);
    unbinder = ButterKnife.bind(this,view);

    //Init
        Slider.init(new RestaurantImageLoadingService());
        iBannerLoadListener = this;
        iMenuLoadListener = this;

        //Check whether Login or not
        if(AccountKit.getCurrentAccessToken() != null)
        {
            setUserInformation();
            loadBanner();
        }

    return view;
    }
    private void loadBanner()
        {
            bannerRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          List<Banner> banners = new ArrayList<>();
                          if(task.isSuccessful())
                              for(QueryDocumentSnapshot bannerSnapShot:task.getResult())
                              {
                                  Banner banner = bannerSnapShot.toObject(Banner.class);
                                  banners.add(banner);

                              }
                          iBannerLoadListener.onBannerLoadSuccess(banners);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    iBannerLoadListener.onBannerLoadFailed(e.getMessage());
                }
            });

    }
    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        text_user_name.setText(Common.currentUser.getName());
    }

    @Override
    public void onmenurLoadSuccess(List<Banner> banners) {

    }

    @Override
    public void onmenuLoadFailed(String message) {

    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {

    }

    @Override
    public void onBannerLoadFailed(String message) {

    }
}