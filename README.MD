#Centrixlink Android SDK集成文档#

###版本支持及库依赖###

* Centrixlink 支持 Android 4.0以上系统版本（API LEVEL 14+）
* 第三方库依赖 Google Play Services (可选)

###集成说明###
	

###1. 将下载的Centrixlxink SDK库文件拷贝到您项目应用的libs目录下###

```
  libs
	|-----Centrixlink_[version].jar
	
```
###2. 更新 AndroidManifest.xml###
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
            android:hardwareAccelerated="true"
            tools:ignore="InnerclassSeparator" />
    <activity android:name="com.centrixlink.SDK.InterstitialFADActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize|screenLayout|smallestScreenSize"
            android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"
            android:hardwareAccelerated="true"
            tools:ignore="InnerclassSeparator" />


    </application>

</manifest>


```

###3. 接口代码 ###

####1. SDK初始化####
``` Java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ....

	 //init SDK
	final Centrixlink centrixlink =   Centrixlink.sharedInstance();

	final String appID = "您申请的APPID";
	final String appKey = "申请APPID关联的APPKEY";


	//设置广告播放回调事件
	EventListener eventListener =  new EventListener() {
	    @Override
	    public void onAdStart() {

			//广告播放开始
	    }


	    @Override
	    public void onAdPlayableChanged(boolean isAdPlayable) {
	       	//当前广告准备状态; true已准备好,false暂时未准备完毕

	    }

	    @Override
	    public void onAdUnavailable(String reason) {
	        //播放失败原因，reason 为失败原因信息
	    }

	    @Override
	    public void onAdEnd(boolean wasSuccessfulView, boolean wasCallToActionClicked) {
	   		//广告播放结束及广告播放情况（wasSuccessfulView 是否完整播放，wasCallToActionClicked 是否点击了广告）
	    }

	};
	centrixlink.setEventListenner(eventListener);

	//配置预加载离线广告；目前最推荐true
	boolean isOnlyPreloadAD = true 

	centrixlink.setIsOnlyPreload(isOnlyPreloadAD);

	//SDK 启动
	centrixlink.startWithAppID(this,appID,appKey);

}

```

####2. 每个Activty 生命周期与SDK关联处理####

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
 	centrixlink.setEventListenner(null);
    centrixlink.setDebugLogProc(null);
}

```

####3. 广告播放####

``` Java

final Centrixlink centrixlink =   Centrixlink.sharedInstance();
//播放全屏广告（推荐）
centrixlink.fetchAD();

//播放插屏广告
centrixlink.fetchInterstitialAD();

```

####4. 其它接口 ####
``` Java
		
final Centrixlink centrixlink =   Centrixlink.sharedInstance();

//检查当前是否可以播放广告
centrixlink.isAdPlayable();

//重置已缓存的广告
centrixlink.resetPreloadCache();

//强制更新广告列表
centrixlink.preloadADListRequest();


```


####5. 混淆设置####
```
# 对Centrixlink的混淆处理
-dontwarn com.centrixlink.**
-keep public class com.centrixlink.**  { *; }
```





