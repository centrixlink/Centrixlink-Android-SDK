package com.centrixlink.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.centrixlink.SDK.AD_PlayError;
import com.centrixlink.SDK.Centrixlink;
import com.centrixlink.SDK.SplashADEventListener;

import java.util.Map;

/**
 * Created by wang.junzheng on 2017/6/8.
 */

public class SplashActivity extends Activity {


    private void onShowFinished(){

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Centrixlink centrixlink = Centrixlink.sharedInstance();


        String   appID = "APPID";
        String   appKey = "APPKEY";

        setContentView(new View(this));
        centrixlink.startWithAppID(this, appID, appKey);

        ViewGroup viewGroup = (ViewGroup)getWindow().getDecorView();
        centrixlink.playSplashAD(this,viewGroup ,new SplashADEventListener() {

            @Override
            public void centrixlinkSplashADDidShow(Map map) {
                Log.i("demo", "centrixlinkSplashADDidShow: "+map.toString() );
            }

            @Override
            public void centrixlinkSplashADAction(Map map) {
                Log.i("demo", "centrixlinkSplashADAction: "+map.toString() );
            }

            @Override
            public void centrixlinkSplashADShowFail(AD_PlayError error) {
                Log.i("demo", "centrixlinkSplashADShowFail: "+error.getDesc() );

                onShowFinished();

            }

            @Override
            public void centrixlinkSplashADClosed(Map map) {
                Log.i("demo", "centrixlinkSplashADClosed: "+map.toString() );
                onShowFinished();

            }

            @Override
            public void centrixlinkSplashADSkip(Map map) {
                Log.i("demo", "centrixlinkSplashADSkip: "+map.toString() );
            }

        });


    }
}
