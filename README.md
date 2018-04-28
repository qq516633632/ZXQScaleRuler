# ZXQScaleRuler-刻度尺控件
## 控件介绍
一个优雅的简洁的刻度尺选择控件，可以用在 身高 体重 腰围等用户信息收集的UI视图上，带来非常nice的交互体验。
## 效果图   

### 水平刻度尺   

![](https://github.com/qq516633632/ZXQScaleRuler/blob/master/dmoimg/h_dmo_img.gif)   


### 垂直刻度尺

![](https://github.com/qq516633632/ZXQScaleRuler/blob/master/dmoimg/v_dmo_img.gif)   


## 使用方法
## Gradle添加引用
```
compile 'com.zhuxiaoqing.ZXQScaleRuler:ZXQScaleRuler:1.0.0'

```
## 使用水平的刻度尺
### 布局中加入水平刻度尺控件
```xml
 <com.zxq.scalruerview.HorizontalScaleRulerView
            android:id="@+id/app_ver_view"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="5dp"
            android:layout_marginBottom="10dp"
            zxq:defaultvalue="50"
            zxq:maxvalue="100"
            zxq:minvalue="30"
            zxq:textcolor="@color/colorPrimary"
            zxq:lincolor="@color/colorPrimary"
            zxq:perspanvalue="5"
            zxq:textsize="14sp"
            zxq:linheight="1dp"
            />
```
### 代码中
如果没有动态的调整 代码中只需要加入回调函数就可以了。
```
   app_ver_view.setValueChangeListener(new HorizontalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                lab_text_view.setText("体重："+value+"kg");
            }
        });

```
如果需要在代码中动态初始化刻度尺可使用如下方法
```
//find到控件后 先设置内部参数
setParam(int itemSpacing, int maxLineHeight, int middleLineHeight, int minLineHeight, int textMarginTop, int textSize)

```

```
//在重新初始化视图
initViewParam(float defaultValue, float minValue, float maxValue, int spanValue)
```

## 使用垂直刻度尺
```xml
<com.zxq.scalruerview.VertcalSralRulerView
        android:id="@+id/app_ver_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_alignParentRight="true"
        android:layout_marginRight="25dp"
        zxq:defaultvalue="160"
        zxq:maxvalue="250"
        zxq:minvalue="110"
        zxq:textcolor="@color/colorPrimary"
        zxq:lincolor="@color/colorPrimary"
        zxq:perspanvalue="5"
        zxq:textsize="14sp"
        zxq:linheight="1dp"
        />
```
## 代码中
使用方法和水平刻度尺一致

## 自定义属性说明
```xml
<declare-styleable name="VertcalSralRulerView">

        <!--字体颜色-->
        <attr name="textcolor" format="color"></attr>
        <!--刻度颜色-->
        <attr name="lincolor" format="color"></attr>
        <!--字体大小-->
        <attr name="textsize" format="dimension"></attr>
        <!--刻度宽度-->
        <attr name="linheight" format="dimension"></attr>
        <!--最大刻度长度-->
        <attr name="maxlinlenght" format="dimension"></attr>
        <!--居中刻度长度-->
        <attr name="middlinlenght" format="dimension"></attr>
        <!--最小刻度长度-->
        <attr name="minlinlenght" format="dimension"></attr>
        <!--文字距离刻度的间隔-->
        <attr name="textmarglin" format="dimension"></attr>
        <!--每个刻度的间隔-->
        <attr name="itemspacing" format="dimension"></attr>
        <!--最大值-->
        <attr name="maxvalue" format="float"></attr>
        <!--最小值-->
        <attr name="minvalue" format="float"></attr>
        <!--值的间隔-->
        <attr name="perspanvalue" format="integer"></attr>
        <!--默认选中值-->
        <attr name="defaultvalue" format="integer"></attr>

    </declare-styleable>
```  

垂直刻度尺和水平刻度尺通用




