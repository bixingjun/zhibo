package com.example.live.livelist;

import com.example.live.ResponseObject;
import com.example.live.model.UserInfo;
import com.example.live.utils.request.BaseRequest;

import java.io.IOException;



public class GetLiveListRequest extends BaseRequest {
    private static final String HOST = "http://liveeee.butterfly.mopaasapp.com/roomServlet?action=getList";

    public static class LiveListParam {
        public int pageIndex;

        public String toUrlParam() {
            return "&=pageIndex" + pageIndex;
        }
    }

    public String getUrl(LiveListParam param) {
        return HOST + param.toUrlParam();
    }

    @Override
    protected void onFail(IOException e) {
        sendFailMsg(-100,e.toString());
    }

    @Override
    protected void onResponseSuccess(String body) {
        LiveListResponseObj liveListresponseObject = gson.fromJson(body, LiveListResponseObj.class);
        if (liveListresponseObject == null) {
            sendFailMsg(-101, "数据格式错误");
            return ;
        }

        if (liveListresponseObject.code.equals(ResponseObject.CODE_SUCCESS)) {
            sendSuccMsg(liveListresponseObject.data);
        } else if (liveListresponseObject.code.equals(ResponseObject.CODE_FAIL)) {
            sendFailMsg(Integer.valueOf(liveListresponseObject.errCode), liveListresponseObject.errMsg);
        }
        return ;
    }

    @Override
    protected void onResponseFail(int code) {
        sendFailMsg(code,"服务器异常");
    }

}
