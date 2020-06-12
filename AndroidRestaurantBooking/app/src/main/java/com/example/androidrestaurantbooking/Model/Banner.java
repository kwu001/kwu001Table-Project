package com.example.androidrestaurantbooking.Model;

public class Banner {
    //Restaurant & banner
    private  String image;

    public Banner(){

    }

    public Banner(String image){

        this.image = image;

    }

    public  String getImage(){
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
