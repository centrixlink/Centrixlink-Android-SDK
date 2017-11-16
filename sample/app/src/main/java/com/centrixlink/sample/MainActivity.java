package com.centrixlink.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.centrixlink.SDK.AdConfig;
import com.centrixlink.SDK.Centrixlink;
import com.centrixlink.SDK.CentrixlinkVideoADListener;
import com.centrixlink.SDK.DebugLogCallBack;

import java.util.Map;

public class MainActivity extends Activity {

    private CentrixlinkVideoADListener eventListener;

    private AdConfig config;

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
        String   appID = "FQ11tkfWJ4";
        String   appKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6WzCw9MWQjwUH76KR+cjr/CdCyQ1UqQRqsMFSRmhM2XHPpXUp4v+vAL984P8xhZ/QLOMIULcLfuegqrzEm0lobJy/dLMy+e18ucR/z1lr6gXnItTwqliJfmNFQOOpYGs8OprucdYqtBl7M4keVDBPYOpVkBTSGr6HxKquZyA9tQIDAQAB";

        centrixlink.startWithAppID(this, appID, appKey);



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

        button.setEnabled(false);

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




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(centrixlink.isAdPlayable()){

                    centrixlink.playAD(MainActivity.this);

//                    /**如果你需要配置广告方向,声音开闭,点击下载后是否自动关闭广告界面可以使用AdConfig 进行配置
//                     *  并且如果你需要进行Server to Server 的透传并配置透传参数，你可以使用AdConfig提供的默
//                     *  认参数，或者使用Bundle来配置你的自定义参数**/
//                    if(config == null){
//                        config = centrixlink.getDefaultAdConfig();
//                    }
//
//                    config.setCentrixlinkOrientations(AdConfig.ORIENTATIONS_DEFAULT);
//                    config.setCentrixlinkIECAutoClose(false);
////                    config.setSoundType(AdConfig.SOUNDTYPE_OPENED);
//                    config.setPlaydOptionKeyExtraInfoDictionary("serfsffff");
//                    Bundle bundle = new Bundle();
//                    bundle.putString("test1"," i am developer");
//                    bundle.putInt("testId",10086);
//                    bundle.putChar("testChar", (char) 12);
//                    bundle.putStringArray("StringArray",new String[]{"IA","BA","CA"});
//                    bundle.putLongArray("LongArray",new long[]{100L,200L,300L});
//                    ArrayList<String> stringList = new ArrayList<>();
//                    stringList.add("aaaa");
//                    stringList.add("bbbb");
//                    stringList.add("cccc");
//                    bundle.putStringArrayList("stringList",stringList);
//                    ArrayList<Integer> integers = new ArrayList<>();
//                    integers.add(1);
//                    integers.add(2);
//                    integers.add(3);
//                    bundle.putIntegerArrayList("integers",integers);
//                    config.setBundle(bundle);
//                    centrixlink.playAD(MainActivity.this,config);
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
            public void centrixLinkAdPlayability(boolean isPreloadFinished) {
                final TextView logView = (TextView) findViewById(R.id.logTextView);

                final Button button = (Button) findViewById(R.id.fullscreen);


                button.setEnabled(isPreloadFinished);

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
    protected void onDestroy() {
        super.onDestroy();
        final Centrixlink centrixlink =   Centrixlink.sharedInstance();
        centrixlink.removeEventListeners(eventListener);
        eventListener = null;
        centrixlink.setDebugCallBack(null);

    }
}
