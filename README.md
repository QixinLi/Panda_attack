# 基于高德地图API的熊猫打天下Android手游开发

## 一、概述
此项目主题内容如下：
<ol>
<li>云端服务器存储用户数据。</li>
<li>图片素材的搜集、界面UI的设计。</li>
<li>调用高德地图API实现地图上的选点操作。</li>
<li>游戏界面供玩家通过游戏的虚拟操作占领真实世界的POI。</li>
<li>提供商店、升级、天赋值等功能，提升游戏的可玩性。</li>
</ol>

## 二、需求分析

### 1、程序主体图片素材搜集、界面UI设计
图片素材来源：千图网、摄图网、百度图片。
素材格式：psd、jpg、png
图片处理软件：Photoshop CS6
图片导出格式：jpg、png、gif
图片需求：
    （1）应用背景图片(注册登录背景、游戏背景、其他背景)
    （2）游戏点击按钮图片(注册、登录、定位、个人信息、商店、保存、帮助等)
    （3）玩家游戏图片(熊猫、野怪、竹罐等)
    （4）游戏中动画效果gif图(扔竹子、放闪电等)

### 2、玩家数据存储
本实验使用阿里云的ECS服务器，需在服务器上搭建MySQL数据库。
安全起见，云端数据库操作需通过调用云端的jsp文件，并获取返回的JSON数据。
为调用云端的jsp文件，需在服务器上搭建Tomcat。

### 3、调用高德地图API
需要学习高德API在安卓开发中的函数、方法的使用。
申请一个高德API的key、并下载部署高德地图SDK。
高德地图整体的绘制和调用。

### 4、游戏界面的设计和实现
设计游戏的规则和玩法(本项目采用精灵宝可梦类型的游戏设计方案)
游戏功能的实现(事件监听、进程调用、GIF动画的播放等)

### 5、细节处理及其他功能的实现
个人信息、商店、帮助界面；游戏后经验、竹子的获取；各种bug修复等等

## 三、软件实现

### 1、服务器端配置、玩家数据的存储和调用
本项目用了阿里云的ECS服务器，服务器操作系统为Centos 6.3。
首先在服务器端配置MySql，并创建相应数据库和表。
通过jsp文件，实现数据库的调用。此次项目一共写了6个jsp文件，为getPlayerDetails.jsp、getUserDetails.jsp、Register.jsp、UpdateUserData.jsp、GetPoiData.jsp、occupyPOI.jsp，分别实现了获取指定玩家的资料、获取当前用户的资料、注册、更新当前用户的资料、获取选中POI的是否被占领信息、占领指定POI的功能。
配置Tomcat，并将写好的jsp项目放到Tomcat的webapps文件夹内。保证该jsp文件的远程调用。

### 2、注册、登录界面实现
考虑到安全性的问题，此项目没有直接调用云端数据库，而是采用安卓程序获取表单、控件输入的值(例如账号密码等)，将这些获取的值作为参数传入服务器端的jsp文件，服务器端的jsp文件通过所得的值，并借这些值进行数据库操作，将数据库返回的内容封装为一个json类型返回给安卓程序。
安卓程序解析该返回的json值，获取其中的参数，并将相应的数据参数赋值给myplayer静态对象，用来表示该用户的所有信息。

### 3、高德地图API的调用
首先申请一个高德地图API的key，部署高德地图SDK，并在AndroidManifest.xml中配置相关权限和API的key。
在相应页面的xml中放入com.amap.api.maps.MapView对象，用来显示高德地图。在相应的java文件中重写OnCreate、OnResume、OnPause、OnDestroy等方法。调用高德地图封装好的getPois方法获取当前位置附近的POI(信息点)，并依次添加到ListView中。玩家通过点击选中相应的ListView，进而决定攻不攻占该城池(POI)。

### 4、细节处理
通过对用户安卓手机定位、Wifi功能的开关状态来提示用户要不要打开GPS/Wifi服务。
考虑到玩家进行游戏的过程中，因为网络不稳定的原因导致数据存储失败的情况。在游戏主界面添加了手动存储的按钮，玩家在网络情况较好的情况下可以手动进行数据的保存。
考虑到程序运行兼容性的问题，将目标SDK调整到21。并且，每个界面的控件布局均在后端java代码中调用自己封装的setMargins方法动态设定。

## 四、总结
此次项目实践遇到了许多问题也解决了许多问题。
在服务器端数据库调用方面，起初往表里Insert数据时，插入中文数据会显示乱码或者问号。解决方法是将服务器端Centos系统语言更换为中文，并将数据库的字符编码更换为UTF-8，最重要的是在调用数据库时，要将传过来的中文字符用UTF-8解码器解码再进行查询语句的执行。
高德地图API的调用，通过翻看官方文档以及学习官方Demo，后解决了不少问题。例如不能自动定位，定位针不能移动等。
最大的难点其实还在App程序的兼容性上，在模拟器或自己手机上测试通过的程序，在其他手机上可能还会有问题。我们组，之前一直使用模拟器测试，但到真机测试的环节是，却发现部分机型（华为、oppo）之类的手机无法获取GPS权限，导致高德地图无法定位。最后通过调低目标API解决的问题。
此次项目实践其实大的难点并不多，主要都是一些小细节问题，搞清楚一个就能搞清楚所有相似类型的问题。此外，与自己本身的基础也有很大的关系。在实践之前，我对服务器、数据库的操作已经相对熟练，对html、jsp、java语言也有一定的基础，之前也有过高德地图API的使用经验。因此写起代码来还算得心应手，不至于遇到技术难点。
由于本次由我来负责游戏环境大框架的编写，因此我也需要对每个人的分工进行详细的指示，共同引用的数据结构也要一模一样。在开工前，我们小组三个人也一起抽空开过一个小会，共同商议程序的结构、内容。三个人共同开发同一款游戏，最重要的就是相互之间的熟悉与默契。个人不单单要考虑个人的任务，也同时要考虑小组其他成员的工作。通过此次项目开发，经常单打独斗的我也深刻体会到团队合作的重要性，众人拾柴火焰高。
我们小组此次合作完成的《熊猫打天下》这款游戏最大的特色是真实玩家通过虚拟对战占领真实的地点。游戏方式新颖，发展前景很广。后期可以考虑给游戏主体加上更多的玩法、模式。还可以与美团等软件合作，如若玩家占领的POI是一个咖啡馆，即可通过美团获得该咖啡馆的优惠券等等。
此次项目锻炼了自己的动手能力，让我亲身体会了一个完整的安卓项目开发的全过程。此次项目基本完成初期设定的需求及目标。
