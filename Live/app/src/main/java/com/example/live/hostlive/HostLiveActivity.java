package com.example.live.hostlive;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.live.R;
import com.example.live.application.BaseApplication;
import com.example.live.editprofile.CustomProfile;
import com.example.live.model.ChatMsgInfo;
import com.example.live.model.Constants;
import com.example.live.model.GiftCmdInfo;
import com.example.live.model.GiftInfo;
import com.example.live.model.UserInfo;
import com.example.live.utils.request.BaseRequest;
import com.example.live.utils.request.SendGiftRequest;
import com.example.live.view.BottomControlView;
import com.example.live.view.ChatMsgListView;
import com.example.live.view.ChatView;
import com.example.live.view.DanmuView;
import com.example.live.view.GiftFullView;
import com.example.live.view.GiftRepeatView;
import com.example.live.view.TitleView;
import com.example.live.watcher.WatcherActivity;
import com.example.live.widget.HostControlDialog;
import com.example.live.widget.SizeChangeRelativeLayout;
import com.google.gson.Gson;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveConstants;
import com.tencent.livesdk.ILVLiveManager;


import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import tyrantgit.widget.HeartLayout;

public class HostLiveActivity extends AppCompatActivity {

    private SizeChangeRelativeLayout mSizeChangeLayout;
    private TitleView mTitleView;
    private AVRootView mLiveView;
    private BottomControlView mControlView;
    private ChatView mChatView;

    private ChatMsgListView mChatListView;
    private DanmuView mDanmuView;

    private Timer heartBeatTimer = new Timer();
    private Timer heartTimer = new Timer();
    private Random heartRandom = new Random();
    private HeartLayout heartLayout;
    private GiftRepeatView giftRepeatView;
    private GiftFullView giftFullView;

    private HostControlState hostControlState;
    private FlashlightHelper flashlightHelper;


    private int mRoomId;
    private HeartBeatRequest mHeartBeatRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_live);

        findAllViews();
        createLive();
    }

    private void createLive() {

        hostControlState = new HostControlState();
        flashlightHelper = new FlashlightHelper();
        mRoomId = getIntent().getIntExtra("roomId", -1);
        if (mRoomId < 0) {
            Toast.makeText(this.getApplicationContext(),"房间号不正确",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ILVLiveConfig liveConfig = BaseApplication.getApplication().getLiveConfig();
        liveConfig.setLiveMsgListener(new ILVLiveConfig.ILVLiveMsgListener() {



            @Override
            public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {

            }

            @Override
            public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {

                if (cmd.getCmd() == Constants.CMD_CHAT_MSG_LIST) {
                    String content = cmd.getParam();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(content, id, userProfile.getFaceUrl());
                    mChatListView.addMsgInfo(info);
                } else if (cmd.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
                    String content = cmd.getParam();
                    ChatMsgInfo info = ChatMsgInfo.createListInfo(content, id, userProfile.getFaceUrl());
                    mChatListView.addMsgInfo(info);

                    String name = userProfile.getNickName();
                    if (TextUtils.isEmpty(name)) {
                        name = userProfile.getIdentifier();
                    }
                    ChatMsgInfo danmuInfo = ChatMsgInfo.createDanmuInfo(content, id, userProfile.getFaceUrl(), name);
                    mDanmuView.addMsgInfo(danmuInfo);
                } else if (cmd.getCmd() == Constants.CMD_CHAT_GIFT) {

                    GiftCmdInfo giftCmdInfo = new Gson().fromJson(cmd.getParam(), GiftCmdInfo.class);
                    int giftId = giftCmdInfo.giftId;
                    String repeatId = giftCmdInfo.repeatId;
                    GiftInfo giftInfo = GiftInfo.getGiftById(giftId);

                    SendGiftRequest sendGiftRequest=new SendGiftRequest();
                    sendGiftRequest.setOnResultListener(new BaseRequest.OnResultListener<UserInfo>() {
                        @Override
                        public void onFail(int code, String msg) {

                        }

                        @Override
                        public void onSuccess(UserInfo object) {

                            TIMFriendshipManager.getInstance().setCustomInfo("Tag_Profile_Custom_level", (object.level+"").getBytes(), new TIMCallBack() {
                                @Override
                                public void onError(int i, String s) {

                                }

                                @Override
                                public void onSuccess() {


                                    getSelfInfo();
                                }
                            });
                            TIMFriendshipManager.getInstance().setCustomInfo("Tag_Profile_Custom_getNums", (object.getNums+"").getBytes(), new TIMCallBack() {
                                @Override
                                public void onError(int i, String s) {

                                }

                                @Override
                                public void onSuccess() {


                                    getSelfInfo();
                                }
                            });
                        }
                    });

                    sendGiftRequest.request(sendGiftRequest.getUrl(BaseApplication.getApplication().getSelfProfile().getIdentifier(),giftInfo.expValue));



                    if (giftInfo == null) {
                        return;
                    }
                    if (giftInfo.type == GiftInfo.Type.ContinueGift) {
                        giftRepeatView.showGift(giftInfo, repeatId, userProfile);
                    } else if (giftInfo.type == GiftInfo.Type.FullScreenGift) {

                        giftFullView.showGift(giftInfo, userProfile);
                    }
                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_ENTER) {

                    mTitleView.addWatcher(userProfile);

                } else if (cmd.getCmd() == ILVLiveConstants.ILVLIVE_CMD_LEAVE) {

                    mTitleView.removeWatcher(userProfile);
                }

            }

            @Override
            public void onNewOtherMsg(TIMMessage message) {

            }
        });





        ILVLiveRoomOption hostOption = new ILVLiveRoomOption(ILiveLoginManager.getInstance().getMyUserId()).
                controlRole("LiveMaster")
                .autoFocus(true)
                .autoMic(hostControlState.isVoiceOn())
                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)
                .cameraId(hostControlState.getCameraid())
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);



        ILVLiveManager.getInstance().createRoom(mRoomId, hostOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {

                startHeartAnim();

                startHeartBeat();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

                Toast.makeText(HostLiveActivity.this, "创建直播失败！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void startHeartBeat() {
        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //发送心跳包
                if (mHeartBeatRequest == null) {
                    mHeartBeatRequest = new HeartBeatRequest();
                }
                String roomId = mRoomId + "";
                String userId = BaseApplication.getApplication().getSelfProfile().getIdentifier();
                String url = mHeartBeatRequest.getUrl(roomId, userId);
                mHeartBeatRequest.request(url);
            }
        }, 0, 4000);
    }

    private void startHeartAnim() {
        heartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                heartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        heartLayout.addHeart(getRandomColor());
                    }
                });
            }
        }, 0, 1000);
    }

    private int getRandomColor() {
        return Color.rgb(heartRandom.nextInt(255), heartRandom.nextInt(255), heartRandom.nextInt(255));
    }

    private void findAllViews() {
        mSizeChangeLayout = (SizeChangeRelativeLayout) findViewById(R.id.size_change_layout);
        mSizeChangeLayout.setOnSizeChangeListener(new SizeChangeRelativeLayout.OnSizeChangeListener() {
            @Override
            public void onLarge() {

                mChatView.setVisibility(View.INVISIBLE);
                mControlView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSmall() {

            }
        });

        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setHost(BaseApplication.getApplication().getSelfProfile());

        mLiveView = (AVRootView) findViewById(R.id.live_view);
        ILVLiveManager.getInstance().setAvVideoView(mLiveView);

        mControlView = (BottomControlView) findViewById(R.id.control_view);
        mControlView.setIsHost(true);
        mControlView.setOnControlListener(new BottomControlView.OnControlListener() {
            @Override
            public void onChatClick() {

                mChatView.setVisibility(View.VISIBLE);
                mControlView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCloseClick() {

                quitLive();
            }

            @Override
            public void onGiftClick() {

            }

            @Override
            public void onOptionClick(View view) {


                boolean beautyOn = hostControlState.isBeautyOn();
                boolean flashOn = flashlightHelper.isFlashLightOn();
                boolean voiceOn = hostControlState.isVoiceOn();

                HostControlDialog hostControlDialog = new HostControlDialog(HostLiveActivity.this);

                hostControlDialog.setOnControlClickListener(controlClickListener);
                hostControlDialog.updateView(beautyOn, flashOn, voiceOn);
                hostControlDialog.show(view);
            }

        });

        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.setOnChatSendListener(new ChatView.OnChatSendListener() {
            @Override
            public void onChatSend(final ILVCustomCmd customCmd) {
                //发送消息
                customCmd.setDestId(ILiveRoomManager.getInstance().getIMGroupId());

                ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack<TIMMessage>() {
                    @Override
                    public void onSuccess(TIMMessage data) {
                        if (customCmd.getCmd() == Constants.CMD_CHAT_MSG_LIST) {

                            String chatContent = customCmd.getParam();
                            String userId = BaseApplication.getApplication().getSelfProfile().getIdentifier();
                            String avatar = BaseApplication.getApplication().getSelfProfile().getFaceUrl();
                            ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, userId, avatar);
                            mChatListView.addMsgInfo(info);
                        } else if (customCmd.getCmd() == Constants.CMD_CHAT_MSG_DANMU) {
                            String chatContent = customCmd.getParam();
                            String userId = BaseApplication.getApplication().getSelfProfile().getIdentifier();
                            String avatar = BaseApplication.getApplication().getSelfProfile().getFaceUrl();
                            ChatMsgInfo info = ChatMsgInfo.createListInfo(chatContent, userId, avatar);
                            mChatListView.addMsgInfo(info);

                            String name = BaseApplication.getApplication().getSelfProfile().getNickName();
                            if (TextUtils.isEmpty(name)) {
                                name = userId;
                            }
                            ChatMsgInfo danmuInfo = ChatMsgInfo.createDanmuInfo(chatContent, userId, avatar, name);
                            mDanmuView.addMsgInfo(danmuInfo);
                        }
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                    }

                });
            }
        });

        mControlView.setVisibility(View.VISIBLE);
        mChatView.setVisibility(View.INVISIBLE);

        mChatListView = (ChatMsgListView) findViewById(R.id.chat_list);

        mDanmuView = (DanmuView) findViewById(R.id.danmu_view);

        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);
        giftRepeatView = (GiftRepeatView) findViewById(R.id.gift_repeat_view);
        giftFullView = (GiftFullView) findViewById(R.id.gift_full_view);
    }

    private HostControlDialog.OnControlClickListener controlClickListener = new HostControlDialog.OnControlClickListener() {
        @Override
        public void onBeautyClick() {

            boolean isBeautyOn = hostControlState.isBeautyOn();
            if (isBeautyOn) {

                ILiveRoomManager.getInstance().enableBeauty(0);
                hostControlState.setBeautyOn(false);
            } else {

                ILiveRoomManager.getInstance().enableBeauty(50);
                hostControlState.setBeautyOn(true);
            }
        }

        @Override
        public void onFlashClick() {

            boolean isFlashOn = flashlightHelper.isFlashLightOn();
            if (isFlashOn) {
                flashlightHelper.enableFlashLight(false);
            } else {
                flashlightHelper.enableFlashLight(true);
            }
        }

        @Override
        public void onVoiceClick() {

            boolean isVoiceOn = hostControlState.isVoiceOn();
            if (isVoiceOn) {

                ILiveRoomManager.getInstance().enableMic(false);
                hostControlState.setVoiceOn(false);
            } else {
                ILiveRoomManager.getInstance().enableMic(true);
                hostControlState.setVoiceOn(true);
            }
        }

        @Override
        public void onCameraClick() {
            int cameraId = hostControlState.getCameraid();
            if (cameraId == ILiveConstants.FRONT_CAMERA) {
                ILiveRoomManager.getInstance().switchCamera(ILiveConstants.BACK_CAMERA);
                hostControlState.setCameraid(ILiveConstants.BACK_CAMERA);
            } else if (cameraId == ILiveConstants.BACK_CAMERA) {
                ILiveRoomManager.getInstance().switchCamera(ILiveConstants.FRONT_CAMERA);
                hostControlState.setCameraid(ILiveConstants.FRONT_CAMERA);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        ILVLiveManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILVLiveManager.getInstance().onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        heartTimer.cancel();
        heartBeatTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        quitLive();
    }

    private void quitLive() {

        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        customCmd.setCmd(ILVLiveConstants.ILVLIVE_CMD_LEAVE);
        customCmd.setDestId(ILiveRoomManager.getInstance().getIMGroupId());
        ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        logout();
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        logout();
                    }
                });
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });


        QuitRoomRequest request = new QuitRoomRequest();
        String roomId = mRoomId +"";
        String userId = BaseApplication.getApplication().getSelfProfile().getIdentifier();
        String url = request.getUrl(roomId, userId);
        request.request(url);
    }

    private void logout() {

        finish();
    }

    private void getSelfInfo() {


        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Log.i("aaaaaa",i+"");
                Toast.makeText(HostLiveActivity.this, "获取信息失败：" + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                BaseApplication.getApplication().setSelfProfile(timUserProfile);
            }
        });
    }

}
