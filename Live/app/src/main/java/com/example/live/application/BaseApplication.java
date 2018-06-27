package com.example.live.application;
/*
 *  包名: com.example.live.application
 * Created by ASUS on 2017/11/13.
 *  描述: TODO
 */

import android.app.Application;
import android.content.Context;

import com.example.live.editprofile.CustomProfile;
import com.example.live.utils.QnUploadHelper;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.TIMManager;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;

import java.util.ArrayList;
import java.util.List;



public class BaseApplication extends Application{

    private static BaseApplication app;
    private ILVLiveConfig mLiveConfig;

    private TIMUserProfile mSelfProfile;
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appContext = getApplicationContext();
        ILiveSDK.getInstance().initSdk(getApplicationContext(), 1400049110, 19086);
        mLiveConfig = new ILVLiveConfig();
        ILVLiveManager.getInstance().init(mLiveConfig);

        List<String> customInfos = new ArrayList<String>();
        customInfos.add(CustomProfile.CUSTOM_GET);
        customInfos.add(CustomProfile.CUSTOM_SEND);
        customInfos.add(CustomProfile.CUSTOM_LEVEL);
        customInfos.add(CustomProfile.CUSTOM_RENZHENG);
        TIMManager.getInstance().initFriendshipSettings(CustomProfile.allBaseInfo, customInfos);

        QnUploadHelper.init("5ECBL0h7ijC2LxT6HLQON3Q1N8Qe5Ktqa6yLzDHb",
                "iX1CHxeWWqVGS8TZZ4tw73ArrYqJQfESosx8j5WN",
                "http://ozcjfptmt.bkt.clouddn.com/",
                "image");

        LeakCanary.install(this);


    }

    public static Context getContext() {
        return appContext;
    }

    public static BaseApplication getApplication() {
        return app;
    }
    public void setSelfProfile(TIMUserProfile userProfile) {
        mSelfProfile = userProfile;
    }
    public ILVLiveConfig getLiveConfig() {
        return mLiveConfig;
    }

    public TIMUserProfile getSelfProfile() {
        return mSelfProfile;
    }
}
