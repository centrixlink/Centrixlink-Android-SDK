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
            android:process=":adprocess"
            tools:ignore="InnerclassSeparator" />

        <activity android:name="com.centrixlink.SDK.ResizedVideoADActivity"
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
#### 集成版本变更指导

##### 1. 监听器变更

###### 视频广告监听器变更
| | Old Lisenter: EventListener| New Lisenter: CentrixlinkVideoADListener|
| ---|--- | --- |
| 本地是否有预加载的广告 | public void onAdPlayableChanged(boolean isAdPlayable) | public void centrixLinkHasPreloadAD(boolean isPreloadFinished) |
| 视频广告即将显示 |（无）| public void centrixLinkVideoADWillShow(Map map) |
| 视频广告成功显示 | public void onAdStart(String adid) | public void centrixLinkVideoADDidShow(Map map) |
| 视频广告显示失败 | public void onAdUnavailable(String reason) | public void centrixLinkVideoADShowFail(Map map) |
| 视频广告关闭 | public void onAdEnd(String adid, boolean wasSuccessfulView, boolean wasCallToActionClicked) | public void centrixLinkVideoADClose(Map map) |
| 视频广告触发了点击 | (无) | public void centrixLinkVideoADAction(Map map) |




###### 开屏广告监听器变更


| | Old Lisenter: SplashADEventListener| New Lisenter: SplashADEventListener|
| ---|--- | --- |
| 开屏图片广告成功显示	 | public void onShowSplashPresentView() | public void centrixlinkSplashADDidShow(Map map) |
| 开屏图片广告关闭 | public void onSplashADClosed() | public void centrixlinkSplashADClosed(Map map) |
| 开屏图片广告点击跳过按钮 | (无) | public void centrixlinkSplashADSkip(Map map) |
| 开屏图片广告触发了Action事件 | public void onSplashADClicked() | public void centrixlinkSplashADAction(Map map) |
| 开屏图片广告显示失败 | public void onShowSplashError(String string) | public void centrixlinkSplashADShowFail(AD_PlayError error) |
| 开屏图片广告播放完成 | (无) | public void centrixlinkSplashADFinished(Map map) |

##### 2.接口变更

|   | Before 2.3 | 2.3 |
| --- | --- | --- |
| 显示开屏广告 | startSplashAD(Activity activity,SplashADEventListener splashADEventListener) | public void playSplashAD(final Activity activity, SplashADEventListener listener) <br><br>自定义ViewGroup开屏广告调用 <br>public void playSplashAD(final Activity activity, ViewGroup viewGroup, SplashADEventListener listener)<br>|
| 显示视频广告 | public boolean fetchAD(Activity activity) | public boolean playAD(Activity activity, boolean isOnline) |
| 显示非全屏视频广告 | public boolean fetchInterstitialAD(Activity activity,float topMarginPercent,float leftMarginPercent,float videoScale |     public boolean playUnFullScreenAD(Activity activity, float topMarginPercent, float leftMarginPercent, float videoScale)  |
| 设置视频广告是否跟随应用方向 | 无 |     public void setEnableFollowAppOrientation(boolean isEnable) |
| 设置Debug显示开关 | 无 | public void setDebugEnable(boolean isEnable) |
| 清除缓存 | public void resetPreloadCache() | 清除缓存逻辑由SDK自动完成，无需开发者处理 |

#### 3.1 SDK初始化
``` Java
private CentrixlinkVideoADListener eventListener;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ....

    final String appID = "您申请的APPID";
    final String appKey = "申请APPID关联的APPKEY";

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
            public void centrixLinkHasPreloadAD(boolean isPreloadFinished) {
                //本地是否有预加载的广告 false表示没有，true表示有
            }

            @Override
            public void centrixLinkVideoADShowFail(Map map){
                //视频广告播放失败
                //key:"error", value:AD_PlayError
               /* AD_PlayError
              100 	广告的播放间隔时间不满足条件
              101	   本地没有可播放广告
              102	   服务端接口返回错误
              103	   服务端返回数据类型错误
              104	   服务端接口响应超时
              105	   当前正在播放其它广告
    */
            }

            @Override
            public void centrixLinkVideoADAction(Map map) {
                //视频广告触发了点击事件
                //key:"ADID", String表示触发
            }

            @Override
            public void centrixLinkVideoADClose(Map map) {
                //视频广告关闭
                //key:"ADID", value:String
                //key:"playFinished", value:boolean;true表示播放完成
                //key:"isAction", value:boolean;true表示触发
                //key:"state", value:2(int)，表示广告关闭

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

centrixlink.playAD(activity);

```
##### 3.2.3 非全屏播放视频广告

``` Java

final Centrixlink centrixlink =   Centrixlink.sharedInstance();

/**
 播放非全屏视频广告的布局
 (注意：所有参数均为百分比，取值范围:[0 1]，非全屏布局依照com.android.support:percent实现)
 @param Activity activity 当前activity
 @param float topMarginPercent 上边距
 @param float leftMarginPercent 左边距
 @param float videoScale 短边占比(例如：在竖屏模式下，指的是视频广告播放窗口宽度占整个屏幕宽的比例，反之横屏模式下是指视频广告播放窗口高度占整个屏幕高的比例)
 */

centrixlink.playUnFullScreenAD(activity, topMarginPercent, leftMarginPercent, videoScale);


```
##### 3.2.4 检查当前是否可以播放视频广告

```Java
final Centrixlink centrixlink =   Centrixlink.sharedInstance();
/*
 * 检查当前是否可以播放视频广告
 * 功能等同于预加载状态centrixLinkHasPreloadAD回调方法
*/
centrixlink.isAdPlayable();
```


##### 3.2.5 设置视频广告显示方向是否跟随应用方向

```Java
/**
 设置是否跟随应用方向

 @param boolead enable 默认值为true;
 */
  centrixlink.setEnableFollowAppOrientation(enable);

```
#### 3.3 开屏图片广告及监听设置

``` Java

final Centrixlink centrixlink =   Centrixlink.sharedInstance();

centrixlink.playSplashAD(this, new SplashADEventListener() {

            @Override
            public void centrixlinkSplashADDidShow(Map map) {
            //开屏图片广告已经显示
            //key:"ADID", value:String

            }

            @Override
            public void centrixlinkSplashADAction(Map map) {
            //开屏图片广告点击事件
            //key:"ADID", value:String


            }

            @Override
            public void centrixlinkSplashADShowFail(AD_PlayError error) {
            //开屏图片广告显示失败
            /* AD_PlayError
            200    请求超时
            201    本地无缓存
            202    接口请求错误返回
            203    要播放的开屏广告和本地预加载的不一致
            204    当前开屏广告功能被服务器关闭
            205    其他异常

            }

            @Override
            public void centrixlinkSplashADClosed(Map map) {
            //开屏图片广告关闭
            //key:"ADID", value:String
            //key:"isClicked", value: boolean
            }

            @Override
            public void centrixlinkSplashADFinished(Map map) {
            //开屏图片广告播放完成
            //key:"ADID", value:String
            }

            @Override
            public void centrixlinkSplashADSkip(Map map) {
            //开屏图片广告跳过
            //key:"ADID", value:String

            }

        });

```

#### 3.4 Activty生命周期与SDK关联处理

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
    centrixlink.setDebugCallBack(null);
}
```

#### 3.5 其它接口

``` Java

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
