package com.example.live.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.live.R;

public class SplashActivity extends AppCompatActivity {

    private TextView splash;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {


        handler.sendEmptyMessageDelayed(100,3000);
        splash = findViewById(R.id.splash);
        Typeface fontType = Typeface.createFromAsset(getAssets(), "fonts/FONT.TTF");
        splash.setTypeface(fontType);



    }
}
