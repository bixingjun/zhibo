package com.example.live.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.live.application.BaseApplication;


import jp.wasabeef.glide.transformations.CropCircleTransformation;



public class ImgUtils {

    public static void load(String url, ImageView targetView) {
        Glide.with(BaseApplication.getContext())
                .load(url)
                .into(targetView);
    }

    public static void load(int resId, ImageView targetView) {
        Glide.with(BaseApplication.getContext())
                .load(resId)
                .into(targetView);
    }

    public static void loadRound(String url, ImageView targetView) {
        Glide.with(BaseApplication.getContext())
                .load(url)
                .bitmapTransform(new CropCircleTransformation(BaseApplication.getContext()))
                .into(targetView);
    }

    public static void loadRound(int resId, ImageView targetView) {
        Glide.with(BaseApplication.getContext())
                .load(resId)
                .bitmapTransform(new CropCircleTransformation(BaseApplication.getContext()))
                .into(targetView);
    }
}
