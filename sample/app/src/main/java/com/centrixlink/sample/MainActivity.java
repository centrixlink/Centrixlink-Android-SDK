package com.centrixlink.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.centrixlink.SDK.Centrixlink;
import com.centrixlink.SDK.CentrixlinkVideoADListener;
import com.centrixlink.SDK.DebugLogCallBack;

import java.util.Map;

public class MainActivity extends Activity {

    private CentrixlinkVideoADListener eventListener;

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


        String   appID = "APPID";
        String   appKey = "APPKEY";


        final Activity mActivity = this;

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
        button.setEnabled(false);
        button1.setEnabled(false);

        EditText appIDview = (EditText) findViewById(R.id.appid);
        appIDview.setText(appID);

        EditText appkeyview = (EditText) findViewById(R.id.appkey);
        appkeyview.setText(appKey);

        centrixlink.setDebugCallBack(new DebugLogCallBack() {
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


        CheckBox autoRotateADDirection = (CheckBox) findViewById(R.id.autoRoration);

        autoRotateADDirection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                centrixlink.setEnableFollowAppOrientation(b);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(centrixlink.hasPreloadAD()){
                    centrixlink.playAD(mActivity);

                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(centrixlink.hasPreloadAD()){
                    centrixlink.playUnFullScreenAD(mActivity,0.2f,0.2f,0.8f);
                }
            }
        });

        CentrixlinkVideoADListener eventListener =  new CentrixlinkVideoADListener() {
            @Override
            public void centrixLinkVideoADWillShow(Map map) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);
                outMessage(logView, "centrixLinkVideoADWillShow: " + map.toString(), Log.INFO);
            }

            @Override
            public void centrixLinkVideoADDidShow(Map map) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);

                outMessage(logView, "centrixLinkVideoADDidShow: " + map.toString(), Log.INFO);
            }

            @Override
            public void centrixLinkHasPreloadAD(boolean isPreloadFinished) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);

                final Button button = (Button) findViewById(R.id.fullscreen);

                final Button button1 = (Button) findViewById(R.id.interscreen);

                button.setEnabled(isPreloadFinished);
                button1.setEnabled(isPreloadFinished);

                if (isPreloadFinished) {
                    outMessage(logView, "centrixLinkHasPreloadAD: " + "AD resouce is ready", Log.INFO);
                } else {
                    outMessage(logView, "centrixLinkHasPreloadAD: " + "AD resouce not ready", Log.INFO);
                }

            }

            @Override
            public void centrixLinkVideoADShowFail(Map map) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);

                outMessage(logView, "centrixLinkVideoADShowFail: " + map.toString(), Log.INFO);
            }

            @Override
            public void centrixLinkVideoADAction(Map map) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);
                outMessage(logView, "centrixLinkVideoADAction: " + map.toString(), Log.INFO);
            }

            @Override
            public void centrixLinkVideoADClose(Map map) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);

                outMessage(logView, "centrixLinkVideoADClose: " + map.toString(), Log.INFO);
            }

        };

        centrixlink.addEventListeners(eventListener);

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
        centrixlink.removeEventListeners(eventListener);
        eventListener = null;
        centrixlink.setDebugCallBack(null);

    }
}
