package com.example.shruthi.hypergaragesale;

/**
 * Created by shruthi on 10/17/16.
 */

public class BrowsePosts {
    //mDesc and mImage was added in the advanced android class - Spring 2017
    public String mTitle;
    public String mPrice;
    public String mImage;
    public String mDesc;
    public String mLatitude;
    public String mLongitude;
    public String mLocation;

    public BrowsePosts (String title, String price, String image) {
        this.mTitle = title;
        this.mPrice = price;
        this.mImage = image;
    }

    public BrowsePosts (String title, String price, String image,String desc) {
        this.mTitle = title;
        this.mPrice = price;
        this.mImage = image;
        this.mDesc=desc;
    }

    public BrowsePosts(String title,String price,String image,String desc,String latitude,String longitude,String location)
    {
        this.mTitle = title;
        this.mPrice = price;
        this.mImage = image;
        this.mDesc=desc;
        this.mLatitude=latitude;
        this.mLongitude=longitude;
        this.mLocation=location;
    }
    public String getmTitle() {
        return mTitle;
    }

    public String getmPrice() {
        return mPrice;
    }

    public String getmImage() {return mImage;}

    public String getmDesc() { return mDesc;}

    public String getmLatitude() {return mLatitude;}

    public String getmLongitude() {return mLongitude;}

    public String getmLocation() {return mLocation;}




}