# RangeSeekBar
自定义Seekbar，傻瓜式导入

# V1.0使用步骤
### 1、配置gradle
project的build.gradle配置
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

app的build.gradle配置
```
implementation 'com.github.TanZhibin:RangeSeekBar:1.0'
```
### 2、xml使用
```
     <czb.com.seekbarlib.RangeSeekBar
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            app:rsb_bg_res="@drawable/seekbar_bg_shape"
            app:rsb_main_color="#ff0000"
            app:rsb_max="200"
            app:rsb_mode="doubleBar"
            app:rsb_progress="50"
            app:rsb_radius="30"
            app:rsb_thumb="@drawable/seekbar_thumb_selector"
            app:rsb_vice_color="#aa888888" />
```
### 3、JAVA使用
添加拖动监听
```
bar1.setOnProgressListener(new RangeSeekBar.OnProgressListener() {
            @Override
            public void onProgress(int progress, float percent) {
                int p = (int) (percent * 100);
                tv_progress1.setText(p + "");
            }

            @Override
            public void onStopTrackingTouch(int progress, float percent) {

            }

            @Override
            public void onOrigin() {

            }
        });
```

### 4、xml属性说明

- app:rsb_main_color="#ff0000"   //进度条主色   
- app:rsb_vice_color="#aa888888" //进度副色，进度条默认背景颜色
- app:rsb_max="200"              //进度最大值
- app:rsb_progress="50"          //初始进度值
- app:rsb_mode="doubleBar"       //模式，singleBar表示为单向进度条  doubleBar双向进度条
- app:rsb_thumb="@drawable/seekbar_thumb_selector"  //滑块drawable，支持selector drawable，默认圆形滑块
- app:rsb_thumb_color="#ff0000"   //圆形滑块颜色，默认等于主色  
- app:rsb_bg_res="@drawable/seekbar_bg_shape"       //进度条背景drawable
- app:rsb_stroke_width="30"      //进度条粗细
- app:rsb_radius="30"            //中心圆点半径大小
- app:rsb_center_indicator="false"    //当模式为双向进度条，是否显示中心刻度线









