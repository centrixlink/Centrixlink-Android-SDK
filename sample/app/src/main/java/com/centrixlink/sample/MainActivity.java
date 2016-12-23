package com.centrixlink.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.centrixlink.SDK.Centrixlink;
import com.centrixlink.SDK.EventListener;
import com.centrixlink.SDK.LogProcListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MainActivity extends AppCompatActivity {

    /*
    *
    * 微信分享
    * */
    private IWXAPI api;

    private void outMessage(final TextView textView, String message, int level)
    {
        textView.append("#######################################\n");

        switch (level)
        {
            case Log.DEBUG:{
                textView.append("DEBUG: "+message+"\n");
            }break;
            case Log.ERROR:{
                textView.append("ERROR: "+message+"\n");
            }break;
            case Log.INFO:{
                textView.append("INFO: "+message+"\n");
            }break;
            case Log.VERBOSE:{
                textView.append("VERBOSE: "+message+"\n");

            }break;
            case Log.WARN:{
                textView.append("WARN: "+message+"\n");
            }break;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Centrixlink centrixlink =   Centrixlink.sharedInstance();

        final String appID = "APPID";
        final String appKey = "APPKEY";

        centrixlink.startWithAppID(this,appID,appKey);





        //开启微信分享支持（可选，推荐添加）
        /*
         *  "WX APP ID" 填写微信开放平台申请通过的APPID
         */
        /*
        api = WXAPIFactory.createWXAPI(this, "WX APP ID");
        api.registerApp("WX APP ID");
        centrixlink.setWXAPIObject(api);
        */
        final Button button =(Button) findViewById(R.id.fullscreen);

        final Button button1 =(Button) findViewById(R.id.interscreen);
        final Button button2 =(Button) findViewById(R.id.cleanPreload);
        Button button3 =(Button) findViewById(R.id.updatePreload);
        button.setEnabled(false);
        button1.setEnabled(false);

        EditText appIDview = (EditText) findViewById(R.id.appid);
        appIDview.setText(appID);

        EditText appkeyview = (EditText) findViewById(R.id.appkey);
        appkeyview.setText(appKey);

        centrixlink.setDebugLogProc(new LogProcListener() {
            @Override
            public void onLogMessage(final String message, final int level) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run(){
                        final TextView logView = (TextView) findViewById(R.id.logTextView);

                        outMessage(logView, message, level);
                    }
                });
            }
        });


        CheckBox onlyPreloadCheck = (CheckBox) findViewById(R.id.onlyPreload);

        onlyPreloadCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                centrixlink.setIsOnlyPreload(b);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centrixlink.fetchAD();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centrixlink.fetchInterstitialAD();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centrixlink.resetPreloadCache();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centrixlink.preloadADListRequest();
            }
        });
        EventListener eventListener =  new EventListener() {
            @Override
            public void onAdStart(String adid) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);

                outMessage(logView,"onAdStart: "+adid, Log.INFO );
            }


            @Override
            public void onAdPlayableChanged(boolean isAdPlayable) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);

                final Button button =(Button) findViewById(R.id.fullscreen);

                final Button button1 =(Button) findViewById(R.id.interscreen);

                button.setEnabled(isAdPlayable);
                button1.setEnabled(isAdPlayable);

                if (isAdPlayable)
                {
                    outMessage(logView,"onAdPlayableChanged: "+ "AD resouce is ready", Log.INFO );
                }else
                {
                    outMessage(logView,"onAdPlayableChanged: "+ "AD resouce not ready", Log.INFO );
                }

            }

            @Override
            public void onAdUnavailable(String reason) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);

                outMessage(logView,"onAdUnavailable: "+ reason, Log.INFO );
            }

            @Override
            public void onAdEnd(String adid, boolean wasSuccessfullView, boolean wasCallToActionClicked) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);

                if (wasSuccessfullView)
                {
                    outMessage(logView,"wasSuccessfulView: "+adid, Log.INFO );

                }else {
                    outMessage(logView,"no wasSuccessfulView: "+adid, Log.INFO );

                }
                outMessage(logView,"onAdEnd: "+adid, Log.INFO );
            }

        };
        centrixlink.setEventListenner(eventListener);

    }


    @Override
    protected void onPause() {
        super.onPause();
        Centrixlink.sharedInstance().onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Centrixlink.sharedInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final Centrixlink centrixlink =   Centrixlink.sharedInstance();

        centrixlink.setEventListenner(null);
        centrixlink.setDebugLogProc(null);

    }
}
