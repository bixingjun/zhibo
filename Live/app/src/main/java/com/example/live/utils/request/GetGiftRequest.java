package com.example.live.utils.request;
/*
 *  包名: com.example.live.utils.request
 * Created by ASUS on 2018/1/2.
 *  描述: TODO
 */

import com.example.live.model.GetGiftResponseObj;
import com.example.live.model.SendGiftResponseObj;
import com.example.live.model.UserInfo;

import java.io.IOException;

public class GetGiftRequest extends BaseRequest {
    private static final String HOST = "http://liveeee.butterfly.mopaasapp.com/userServlet?action=getGift";



    public String getUrl(String userId,int exp) {
        return HOST + "&userId=" + userId +
                "&exp=" + exp;

    }

    @Override
    protected void onFail(IOException e) {
        sendFailMsg(-100,e.toString());
    }

    @Override
    protected void onResponseSuccess(String body) {
        GetGiftResponseObj getGiftResponseObj = gson.fromJson(body, GetGiftResponseObj.class);
        sendSuccMsg(getGiftResponseObj.data);
        return;
    }

    @Override
    protected void onResponseFail(int code) {
        sendFailMsg(code,"服务器异常");
    }
}
