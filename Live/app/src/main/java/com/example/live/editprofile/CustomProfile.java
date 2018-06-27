package com.example.live.editprofile;

import com.tencent.TIMFriendshipManager;

public class CustomProfile {

    private static final String PREFIX = "Tag_Profile_Custom_";
    public static final String CUSTOM_RENZHENG = PREFIX + "renzheng";
    public static final String CUSTOM_LEVEL = PREFIX + "level";
    public static final String CUSTOM_GET = PREFIX + "getNums";
    public static final String CUSTOM_SEND = PREFIX + "sendNums";


    public static final long allBaseInfo =
            TIMFriendshipManager.TIM_PROFILE_FLAG_BIRTHDAY |
            TIMFriendshipManager.TIM_PROFILE_FLAG_FACE_URL |
            TIMFriendshipManager.TIM_PROFILE_FLAG_GENDER |
            TIMFriendshipManager.TIM_PROFILE_FLAG_LANGUAGE |
            TIMFriendshipManager.TIM_PROFILE_FLAG_LOCATION |
            TIMFriendshipManager.TIM_PROFILE_FLAG_NICK |
            TIMFriendshipManager.TIM_PROFILE_FLAG_SELF_SIGNATURE |
            TIMFriendshipManager.TIM_PROFILE_FLAG_REMARK |
            TIMFriendshipManager.TIM_PROFILE_FLAG_GROUP;
}
