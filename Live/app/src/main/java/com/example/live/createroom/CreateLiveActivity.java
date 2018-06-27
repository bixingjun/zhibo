package com.example.live.createroom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.live.R;
import com.example.live.application.BaseApplication;
import com.example.live.hostlive.HostLiveActivity;
import com.example.live.model.RoomInfo;
import com.example.live.utils.ImgUtils;
import com.example.live.utils.PicChooserHelper;
import com.example.live.utils.request.BaseRequest;
import com.tencent.TIMUserProfile;



public class CreateLiveActivity extends AppCompatActivity {
    private View mSetCoverView;
    private ImageView mCoverImg;
    private TextView mCoverTipTxt;
    private EditText mTitleEt;
    private TextView mCreateRoomBtn;
    private TextView mRoomNoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        findAllViews();
        setListeners();
        setupTitlebar();
    }

    private void findAllViews() {
        mSetCoverView = findViewById(R.id.set_cover);
        mCoverImg = (ImageView) findViewById(R.id.cover);
        mCoverTipTxt = (TextView) findViewById(R.id.tv_pic_tip);
        mTitleEt = (EditText) findViewById(R.id.title);
        mCreateRoomBtn = (TextView) findViewById(R.id.create);
        mRoomNoText = (TextView) findViewById(R.id.room_no);
    }

    private void setListeners() {
        mSetCoverView.setOnClickListener(clickListener);
        mCreateRoomBtn.setOnClickListener(clickListener);
    }

    private void setupTitlebar() {
        Toolbar titlebar = (Toolbar) findViewById(R.id.titlebar);
        titlebar.setTitle("开始我的直播");
        titlebar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(titlebar);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.create) {

                requestCreateRoom();
            } else if (id == R.id.set_cover) {

                choosePic();
            }
        }
    };

    private void requestCreateRoom() {
        CreateRoomRequest.CreateRoomParam param = new CreateRoomRequest.CreateRoomParam();
        TIMUserProfile selfProfile = BaseApplication.getApplication().getSelfProfile();
        param.userId = selfProfile.getIdentifier();
        param.userAvatar = selfProfile.getFaceUrl();
        String nickName = selfProfile.getNickName();
        param.userName = TextUtils.isEmpty(nickName) ? selfProfile.getIdentifier() : nickName;
        param.liveTitle = mTitleEt.getText().toString();
        param.liveCover = coverUrl;

        CreateRoomRequest request = new CreateRoomRequest();
        request.setOnResultListener(new BaseRequest.OnResultListener<RoomInfo>() {
            @Override
            public void onFail(int code, String msg) {
                Log.i("CreateLiveActivity",code+"");
                Toast.makeText(CreateLiveActivity.this, "请求失败：" + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(RoomInfo roomInfo) {
                Toast.makeText(CreateLiveActivity.this, "请求成功：" + roomInfo.roomId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(CreateLiveActivity.this, HostLiveActivity.class);
                intent.putExtra("roomId", roomInfo.roomId);
                startActivity(intent);

                finish();
            }
        });


        String requestUrl = request.getUrl(param);
        request.request(requestUrl);
    }


    private PicChooserHelper mPicChooserHelper;

    private void choosePic() {
        if (mPicChooserHelper == null) {
            mPicChooserHelper = new PicChooserHelper(this, PicChooserHelper.PicType.Cover);
            mPicChooserHelper.setOnChooseResultListener(new PicChooserHelper.OnChooseResultListener() {
                @Override
                public void onSuccess(String url) {

                    updateCover(url);
                }

                @Override
                public void onFail(String msg) {

                    Toast.makeText(CreateLiveActivity.this, "选择失败：" + msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        mPicChooserHelper.showPicChooserDialog();
    }

    private String coverUrl = null;

    private void updateCover(String url) {
        coverUrl = url;
        ImgUtils.load(url, mCoverImg);
        mCoverTipTxt.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPicChooserHelper != null) {
            mPicChooserHelper.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
