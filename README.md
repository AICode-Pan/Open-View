## 简介
    通过Canvas或者继承View，写的一些常用的控件，记录一下。有需要的朋友可以自取。

###  PersicopeLayout [效果](https://aicode-pan.oss-cn-beijing.aliyuncs.com/video/1592570155492374.mp4)
点赞动画，通过贝塞尔曲线绘制悬浮路径，图片复用。
<br/>

### BlurView [效果](https://aicode-pan.oss-cn-beijing.aliyuncs.com/video/20200620110203.MP4)
仿IOS Blur控件。因为Android手机计算能力问题，Android没有类似控件。其实实现原理很简单，先裁剪View获取相应位
置Bitmap，再对Bitmap进行压缩处理和模糊处理，最后绘制出来就好了。为了使效果更像IOS的，我加了一个透明白色蒙板。
<br/>

### IndicatorView [效果](https://aicode-pan.oss-cn-beijing.aliyuncs.com/video/1592570074332320.mp4)
指示器，用于画廊，导览，开屏广告，viewpager
<br/>

### SlideSelectView [效果](https://aicode-pan.oss-cn-beijing.aliyuncs.com/video/1592570074324749.mp4)
滑动选择器
<br/>