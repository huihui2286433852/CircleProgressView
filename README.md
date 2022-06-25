# CircleProgress
##### 环形进度条显示效果图
这个项目是我转载了‘Xingzuo888’ 大佬的，我在上面修改了一下bug：1.渐变色状态先出现断层问题；2.中间显示文本不居中问题；3.添加了进度加粗功能


![显示内圆](https://images.gitee.com/uploads/images/2020/0902/143459_60d25077_5290883.png "inner_circle.png")

显示内圆



![显示外环](https://images.gitee.com/uploads/images/2020/0902/144623_29523ffb_5290883.png "out_ring.png")



显示外环



![显示进度条颜色渐变](https://images.gitee.com/uploads/images/2020/0902/152241_f7555e26_5290883.jpeg "progress_gradient.jpg")



显示渐变进度条及动态进度






##### 添加工程

###### **Step 1.** Add the JitPack repository to your build file 

​	Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```



###### **Step 2.** Add the dependency

```
dependencies {
            //还未修改
	        implementation 'com.github.Xingzuo888:CircleProgress:v1.0'
	}
```



##### 使用方式，更多属性请参考表格属性



###### 使用的xml代码

```
<com.example.circleprogress.CircleProgressView
        android:id="@+id/progress_true"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="50dp"
        app:cpv_background_circle_color="#40B9F9"
        app:cpv_background_circle_width="50dp"
        app:cpv_circle_color="#B052FF"
        app:cpv_circle_width="50dp"
        app:cpv_first_from_second="50"
        app:cpv_inner_circle_color="#8DDAFF"
        app:cpv_isProgressGradient="true"
        app:cpv_outside_ring_color="#FFFFFF"
        app:cpv_outside_ring_width="1dp"
        app:cpv_progressEnd="#0F031E"
        app:cpv_progressStart="#EF07D8"
        app:cpv_text_color="#FFFFFF"
        app:cpv_text_size="40"
        app:cpv_text_suffix="%"
        app:cpv_tip_text="已占用"
        app:cpv_tip_text_color="#FFFFFF"
        app:cpv_tip_text_size="26" />
```



###### 使用java代码

```
		CircleProgressView progressTrue=findViewById(R.id.progress_true);
        CircleProgressView progressFalse=findViewById(R.id.progress_false);
        progressTrue.setTextEnabled(true);
        progressFalse.setTextEnabled(true);

        progressTrue.setMaxProgress(100);
        progressFalse.setMaxProgress(100);
        progressTrue.setProgressWithAnimation(100,3000);
        progressFalse.setProgress(100);
```









###### 使用说明表

| java代码设置进度条属性   |                                                        |
| ------------------------ | ------------------------------------------------------ |
| setShowOutCircle         | 设置是否显示外环线                                     |
| setShowInnerCircle       | 设置是否显示内圆                                       |
| setProgressGradient      | 设置进度条颜色是否渐变， 渐变的进度范围是0-180,180-360 |
| setProgressStartColor    | 设置进度条开始的渐变颜色                               |
| setProgressEndColor      | 设置进度条结束的渐变颜色                               |
| setOutsideRingWidth      | 设置外环的宽度                                         |
| setOutsideRingColor      | 设置外环颜色                                           |
| setFirstFromSecond       | 设置外环到内环的距离                                   |
| setCircleWidth           | 设置圆弧的宽度                                         |
| setInnerCircleColor      | 设置内圆的颜色                                         |
| setCircleColor           | 设置圆弧的颜色                                         |
| setTextPrefix            | 设置文本的前缀内容                                     |
| setTextSuffix            | 设置文本的后缀内容                                     |
| setTextSize              | 设置文字的大小                                         |
| setTextBold              | 设置文字加粗                                         |
| setTextEnabled           | 设置文本可用（显示）状态                               |
| setStartAngle            | 设置开始绘画圆弧的位置                                 |
| setTextColor             | 设置文本字体的颜色                                     |
| setTipTextColor          | 设置提示文本字体颜色                                   |
| setTipTextSize           | 设置提示文本字体大小                                   |
| setTipText               | 设置提示文本内容                                       |
| setProgress              | 设置当前进度值                                         |
| setMaxProgress           | 设置最大进度值                                         |
| setProgressWithAnimation | 设置进度动画                                           |
| setBackgroundColor       | 设置圆弧背景颜色                                       |
| setBackgroundStrokeWidth | 设置圆弧背景的宽度                                     |



| xml文件设置进度条属性       |                          |
| --------------------------- | ------------------------ |
| cpv_text_prefix             | 设置文本的前缀内容       |
| cpv_text_suffix             | 设置文本的后缀内容       |
| cpv_progress                | 设置当前进度值           |
| cpv_max_progress            | 设置最大进度值           |
| cpv_circle_color            | 设置圆弧的颜色           |
| cpv_inner_circle_color      | 设置内圆的颜色           |
| cpv_outside_ring_color      | 设置外环颜色             |
| cpv_background_circle_color | 设置圆弧背景颜色         |
| cpv_text_color              | 设置文本字体的颜色       |
| cpv_text_size               | 设置文字的大小           |
| cpv_text_bold               | 设置文字的加粗           |
| cpv_tip_text_color          | 设置提示文本字体颜色     |
| cpv_tip_text_size           | 设置提示文本字体大小     |
| cpv_tip_text                | 设置提示文本内容         |
| cpv_circle_width            | 设置圆弧的宽度           |
| cpv_outside_ring_width      | 设置外环的宽度           |
| cpv_background_circle_width | 设置圆弧背景的宽度       |
| cpv_first_from_second       | 设置外环到内环的距离     |
| cpv_isShowOutCircle         | 设置是否显示外环线       |
| cpv_isShowInnerCircle       | 设置是否显示内圆         |
| cpv_isProgressGradient      | 设置进度条颜色是否渐变   |
| cpv_progressStart           | 设置进度条开始的渐变颜色 |
| cpv_progressEnd             | 设置进度条结束的渐变颜色 |

