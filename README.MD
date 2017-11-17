# Centrixlink Android SDK集成文档



### 版本支持及依赖

* Centrixlink 支持 Android 4.0以上系统版本（API LEVEL 14+）
* 第三方库依赖 Google Play Services (可选), libammsdk.jar	(可选)
* 申请：[App ID、App Key](https://dashboard.centrixlink.com/login)

### 集成说明

### 1 将下载的Centrixlxink SDK库文件拷贝到项目应用的libs目录下

```
  libs
	|-----Centrixlink_[version].jar
	|-----libammsdk.jar				(集成微信分享功能，可选)
```

### 2 更新 AndroidManifest.xml
```	xml
<manifest>

	...
	<!-- 权限 -->
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />


	<application>

		...

    <!-- 广告 Activity -->
        <activity android:name="com.centrixlink.SDK.FullScreenADActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize"
            android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
            tools:ignore="InnerclassSeparator" />

            
    </application>

</manifest>

```




### 3 集成说明


#### 3.1 SDK初始化
``` Java
private CentrixlinkVideoADListener eventListener;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ....
    //实际开发中请务必使用自己申请的AppID 和 AppKey 
    final String appID = "FQ11tkfWJ4";
    final String appKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6WzCw9MWQjwUH76KR+cjr/CdCyQ1UqQRqsMFSRmhM2XHPpXUp4v+vAL984P8xhZ/QLOMIULcLfuegqrzEm0lobJy/dLMy+e18ucR/z1lr6gXnItTwqliJfmNFQOOpYGs8OprucdYqtBl7M4keVDBPYOpVkBTSGr6HxKquZyA9tQIDAQAB";

    //init SDK
    final Centrixlink centrixlink =   Centrixlink.sharedInstance();

    //SDK启动
    centrixlink.startWithAppID(this,appID,appKey);

}


```


#### 3.2 视频广告


##### 3.2.1 视频广告播放回调事件设置

``` Java
final Centrixlink centrixlink =   Centrixlink.sharedInstance();
CentrixlinkVideoADListener eventListener =  new CentrixlinkVideoADListener() {

            @Override
            public void centrixLinkVideoADWillShow(Map map){
                //视频广告即将播放
                //key:"ADID",value:String
            }

            @Override
            public void centrixLinkVideoADDidShow(Map map) {
                //视频广告播放开始
                //key:"ADID",value:String
            }

            @Override
            public void centrixLinkAdPlayability(boolean isPreloadFinished) {
                //是否可以播放广告
            }

            @Override
            public void centrixLinkVideoADShowFail(Map map){
            //视频广告播放失败
            //key:"error", value:AD_PlayError
            /* AD_PlayError
                100 	广告的播放间隔时间不满足条件
                101	本地没有可播放广告
                105	当前正在播放其它广告
                106   处于静默状态
                107   本地广告资源不可用
                108   当前用户播放超限
            */
            }

            @Override
            public void centrixLinkVideoADAction(Map map) {
                //视频广告触发了点击事件
                //key:"ADID", value:String
            }

            @Override
            public void centrixLinkVideoADClose(Map map) {
                //视频广告关闭
                //key:"ADID", value:String
                //key:"playFinished", value:boolean;true表示播放完成
                //key:"isClick", value:boolean;true表示触发

            }

        };
        centrixlink.addEventListeners(eventListener);

```
##### 3.2.2 全屏播放视频广告

``` Java

final Centrixlink centrixlink =   Centrixlink.sharedInstance();

/**
 @param Activity activity 当前activity
 */
 
/**你可以使用之前的接口进行播放:**/
centrixlink.playAD(activity);


/**如果你需要配置广告显示的方向,声音开闭,点击下载后是否自动关闭广告界面可以使用AdConfig 对应参数进行配置
*  并且如果你需要进行Server to Server 的透传并配置透传参数，你可以使用AdConfig提供的默
*  认参数，或者使用Bundle来配置你的自定义参数**/
AdConfig config = new AdConfig();
config.setCentrixlinkOrientations(AdConfig.ORIENTATIONS_DEFAULT);//设置你的广告的播放方向：AdConfig.ORIENTATIONS_LANDSCAPE 横向，AdConfig.ORIENTATIONS_PORTRAIT 竖向，AdConfig.ORIENTATIONS_DEFAULT会默认跟随你在后台设置的方向
config.setCentrixlinkIECAutoClose(false);//设置广告跳转按钮被点击后是否会自动关闭广告界面
config.setOptionKeyExtra1("****");//你可以使用保留的八个自定义参数 optionKeyExtra1-optionKeyExtra8来配置你的自定义参数
Bundle bundle = new Bundle();//或者你可以使用bundle 通过 key value的形式传入你的自定义参数,我们支持所有类型参数和list类型的参数
bundle.putString("Your parameter name","Your parameter value");
bundle.putInt("Your parameter name1",1);
ArrayList<String> list = new ArrayList<>();
list.add("1");
bundle.putStringArrayList("Your parameter name2",list);
config.setBundle(bundle);
centrixlink.playAD(mActivity,config);

```

##### 3.2.3 检查当前是否可以播放视频广告

```Java
final Centrixlink centrixlink =   Centrixlink.sharedInstance();
/*
 * 检查当前是否可以播放视频广告
*/
centrixlink.isAdPlayable();
```




#### 3.3 Activty生命周期与SDK关联处理

``` Java


@Override
protected void onDestroy() {
    super.onDestroy();
    final Centrixlink centrixlink =   Centrixlink.sharedInstance();

    //重置SDK，防止内存泄漏
    centrixlink.removeEventListeners(eventListener);
    centrixlink.setDebugCallBack(null);
}
```

#### 3.4 其它接口

``` Java

//微信朋友圈分享（分享endcard页面，此项为可选）
 IWXAPI api = WXAPIFactory.createWXAPI(this, "WX APP ID");

 api.registerApp("WX APP ID");
 centrixlink.setWXAPIObject(api);

```

#### 3.5 混淆设置
```
# 对Centrixlink SDK的混淆处理
-dontwarn com.centrixlink.**
-keep public class com.centrixlink.**  { *; }


# 对微信SDK的混淆处理
-keep class com.tencent.mm.sdk.** {
   *;
}

```


