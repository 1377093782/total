##接入步骤
1 copy 文件  autoupdatesdk-release.aar

请在工程文件根目录下创建一个名为 libs 的子目录，并将广点通 SDK 的 AAR 包拷贝到 libs 目录下
请在工程build.gradle中添加如下配置 

repositories {
  flatDir {
      dirs 'libs'
  }
}
dependencies {
  implementation (name:'autoupdatesdk-release', ext:'aar')
}
# 应用包结构发生改变  版本号和版本名称写到 清单文件中
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.liuan.videowallpaper"

    android:versionCode="37"
    android:versionName="3.1.0">

# 允许http请求  android:usesCleartextTraffic="true"
  <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:usesCleartextTraffic="true"

 #app级别工程里面要需要添加


 <meta-data
            android:name="BDAPPID"
            android:value="baiduId" />
        <meta-data
            android:name="BDAPPKEY"
            android:value="BDAPPKEY" />

#  配置内容提供者
     <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">



            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/bdp_update_filepaths" />
        </provider>
         <!-- 更新所需广播-->
              <receiver
                    android:name="com.baidu.autoupdatesdk.receiver.BDBroadcastReceiver"
                    android:exported="false">
                    <intent-filter>
                        <action android:name="com.baidu.autoupdatesdk.ACTION_NEW_UPDATE" />
                        <action android:name="com.baidu.autoupdatesdk.ACTION_DOWNLOAD_COMPLETE" />
                        <action android:name="com.baidu.autoupdatesdk.ACTION_NEW_AS" />
                        <action android:name="com.baidu.autoupdatesdk.ACTION_AS_DOWNLOAD_COMPLETE" />
                    </intent-filter>
                </receiver>

 ### bdp_update_filepaths.xml  百度sdk 自带的字段


 <resources>
     <external-files-path name="extfiles" path="autoupdatecache/" />
     <external-cache-path name="extcachs" path="autoupdatecache/" />
     <cache-path name="intcachs" path="autoupdatecache/" />
 </resources>


#最后一步
主页 首页 onCreate中
 AnguoUtils.checkUpDate(this);

