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
	|-----libammsdk.jar				(可选)
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
            android:process=":adprocess"
            tools:ignore="InnerclassSeparator" />

        <activity android:name="com.centrixlink.SDK.InterstitialFADActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize"
            android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"
            android:hardwareAccelerated="true"
            android:process=":adprocess"
            tools:ignore="InnerclassSeparator" />
      <!-- 广告 service -->
        <service
            android:name="com.centrixlink.SDK.service.CentrixlinkService"
            android:exported="false" />


    </application>

</manifest>

```

### 3 集成说明

#### 3.1 SDK初始化
``` Java
private EventListener eventListener;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ....

    final String appID = "您申请的APPID";
    final String appKey = "申请APPID关联的APPKEY";
    
    //init SDK
    final Centrixlink centrixlink =   Centrixlink.sharedInstance();
    
    //配置预加载视频广告，目前推荐为true
    boolean isOnlyPreloadAD = true 
    centrixlink.setIsOnlyPreload(isOnlyPreloadAD);
    
    //SDK启动
    centrixlink.startWithAppID(this,appID,appKey);

}

```


#### 3.2 视频广告播放


##### 3.2.1 设置视频广告播放回调事件

``` Java
final Centrixlink centrixlink =   Centrixlink.sharedInstance();

EventListener eventListener =  new EventListener() {
	    @Override
	    public void onAdStart(String adid) {

		//视频广告播放开始（adid 为广告ID）
	    }


	    @Override
	    public void onAdPlayableChanged(boolean isAdPlayable) {
	       	//当前视频广告准备状态; true为准备好,false为未准备好

	    }

	    @Override
	    public void onAdUnavailable(String reason) {
	        //视频广告播放失败，reason为失败原因信息
	    }

	    @Override
	    public void onAdEnd(String adid, boolean wasSuccessfulView, boolean wasCallToActionClicked) {
	        //视频广告播放结束及广告播放情况（adid为视频广告ID, wasSuccessfulView为视频广告是否完整播放，wasCallToActionClicked是否点击了视频广告）
	    }

	};

 centrixlink.addEventListeners(eventListener);

```
##### 3.2.2 全屏播放视频广告

``` Java

final Centrixlink centrixlink =   Centrixlink.sharedInstance();

／**
 @param Activity activity 当前activity
 */
 
centrixlink.fetchAD(activity);

```
##### 3.2.3 插屏播放视频广告

``` Java

final Centrixlink centrixlink =   Centrixlink.sharedInstance();

／**
 播放插屏视频广告的布局
 (注意：所有参数均为百分比，取值范围:[0 1]，插屏布局依照com.android.support:percent实现)
 @param Activity activity 当前activity
 @param float topMarginPercent 上边距
 @param float leftMarginPercent 左边距
 @param float videoScale 短边占比(例如：在竖屏模式下，指的是视频广告播放窗口宽度占整个屏幕宽的比例，反之横屏模式下是指视频广告播放窗口高度占整个屏幕高的比例)
 */
 
centrixlink.fetchInterstitialAD(activity, topMarginPercent,leftMarginPercent,videoScale)

```

#### 3.3 展示开屏图片广告并设置开屏图片广告监听

``` Java

final Centrixlink centrixlink =   Centrixlink.sharedInstance();

centrixlink.startSplashAD(this, new SplashADEventListener() {

            @Override
            public void onShowSplashPresentView() {
                //开屏图片广告已经显示

            }

            @Override
            public void onShowSplashError(String string) {
                //开屏图片广告错误
            }

            @Override
            public void onSplashADClosed() {
                //开屏图片广告关闭

            }

            @Override
            public void onSplashADClicked() {
                //开屏图片广告点击事件

            }
   });
   
```

#### 3.4 每个Activty 生命周期与SDK关联处理

``` Java
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

    //重置SDK，防止内存泄漏
    centrixlink.removeEventListeners(eventListener);
    centrixlink.setDebugLogProc(null);
}

```
#### 3.5 其它接口

``` Java		
final Centrixlink centrixlink =   Centrixlink.sharedInstance();

//检查当前是否可以播放视频广告
centrixlink.isAdPlayable();

//重置已预加载视频广告
centrixlink.resetPreloadCache();


//微信朋友圈分享（分享endcard页面，此项为可选）
 IWXAPI api = WXAPIFactory.createWXAPI(this, "WX APP ID");

 api.registerApp("WX APP ID");
 centrixlink.setWXAPIObject(api);

```

#### 3.6 混淆设置
```
# 对Centrixlink SDK的混淆处理
-dontwarn com.centrixlink.**
-keep public class com.centrixlink.**  { *; }


# 对微信SDK的混淆处理
-keep class com.tencent.mm.sdk.** {
   *;
}

```
