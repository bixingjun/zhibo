package com.example.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.live.R;
import com.example.live.model.ChatMsgInfo;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Administrator.
 */

public class DanmuView extends LinearLayout {

    private static final String TAG = DanmuView.class.getSimpleName();
    private DanmuItemView item0, item1, item2, item3;

    private List<ChatMsgInfo> msgInfoList = new LinkedList<ChatMsgInfo>();

    public DanmuView(Context context) {
        super(context);
        init();
    }

    public DanmuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanmuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_danmu, this, true);
        findAllViews();
    }

    private void findAllViews() {
        item0 = (DanmuItemView) findViewById(R.id.danmu0);
        item1 = (DanmuItemView) findViewById(R.id.danmu1);
        item2 = (DanmuItemView) findViewById(R.id.danmu2);
        item3 = (DanmuItemView) findViewById(R.id.danmu3);

        item0.setVisibility(INVISIBLE);
        item1.setVisibility(INVISIBLE);
        item2.setVisibility(INVISIBLE);
        item3.setVisibility(INVISIBLE);

        item0.setOnAvaliableListener(avaliableListener);
        item1.setOnAvaliableListener(avaliableListener);
        item2.setOnAvaliableListener(avaliableListener);
        item3.setOnAvaliableListener(avaliableListener);
    }

    private  DanmuItemView.OnAvaliableListener avaliableListener = new DanmuItemView.OnAvaliableListener() {
        @Override
        public void onAvaliable() {
            ChatMsgInfo chatMsgInfo=null;
            synchronized (this) {
                msgInfoList.remove(0);
            }

                if (msgInfoList.size() > 0) {
                    Log.d(TAG, "显示缓存消息");

                    addMsgInfo(chatMsgInfo);
                }
            }

    };


    public void addMsgInfo(ChatMsgInfo danmuInfo) {
        synchronized (this) {
            DanmuItemView avaliableItemView = getAvaliableItemView();
            if (avaliableItemView == null) {

                msgInfoList.add(danmuInfo);

            } else {

                avaliableItemView.showMsgInfo(danmuInfo);
            }
        }
    }

    private DanmuItemView getAvaliableItemView() {

        if (item0.getVisibility() != VISIBLE) {
            return item0;
        }
        if (item1.getVisibility() != VISIBLE) {
            return item1;
        }
        if (item2.getVisibility() != VISIBLE) {
            return item2;
        }
        if (item3.getVisibility() != VISIBLE) {
            return item3;
        }
        return null;
    }
}
