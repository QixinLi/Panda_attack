package com.lqx.l7246.pandaattack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by l7246 on 2017/10/30.
 */

public class MapChoosePOI extends Activity implements AMap.OnCameraChangeListener, AMap.OnMapLoadedListener, AMapLocationListener, PoiSearch.OnPoiSearchListener, View.OnClickListener{
    private long exitTime=0;
    private ListView listView;
    private AMap aMap;
    private MapView mapView=null;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker locationMarker, checkinMarker;
    private LatLonPoint searchLatlonPoint;
    private List<PoiItem> resultData;
    private SearchResultAdapter searchResultAdapter;
    private WifiManager mWifiManager;
    private PoiSearch poisearch;
    private Circle mcircle;
    private LatLng checkinpoint,mlocation;
    private Button locbtn,quitbt;
    private ImageView checkinbtn;
    private ImageView home_bt,shop_bt,save_bt,help_bt;
    private boolean isItemClickAction, isLocationAction;
    public int width,height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        WindowManager wm = this.getWindowManager();
        this.width = wm.getDefaultDisplay().getWidth();
        this.height = wm.getDefaultDisplay().getHeight();

        setContentView(R.layout.map_poi_panel);

        mapView = (MapView) findViewById(R.id.map);
        listView=(ListView)findViewById(R.id.listview);
        checkinbtn=(ImageView)findViewById(R.id.checkinbtn);
        quitbt=(Button)findViewById(R.id.quitbt);
        home_bt=(ImageView)findViewById(R.id.home_bt);
        shop_bt=(ImageView)findViewById(R.id.shop_bt);
        save_bt=(ImageView)findViewById(R.id.save_bt);
        help_bt=(ImageView)findViewById(R.id.help_bt);
        setMargins(mapView,0,0,width-height,0);
        setMargins(listView,height,0,width/10,0);
        setMargins(checkinbtn,width-width/10,height-width/10,0,0);
        setMargins(quitbt,0,0,(int)(width*0.934),(int)(height*0.889));
        setMargins(home_bt,width-width/10,0,0,height-width/10);
        setMargins(shop_bt,width-width/10,width/10,0,height-width/10*2);
        setMargins(save_bt,width-width/10,width/10*2,0,height-width/10*3);
        setMargins(help_bt,width-width/10,width/10*3,0,height-width/10*4);

        locbtn=(Button) findViewById(R.id.locbtn);
        setMargins(locbtn,0,(int)(height*0.889),(int)(width*0.934),0);

        quitbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MapChoosePOI.this).setTitle("您确认注销当前账户？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                                Intent intent=new Intent(MapChoosePOI.this,SignIn.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("算了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                            }
                        }).show();
            }
        });
        home_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapChoosePOI.this,UserDetails.class);
                startActivity(intent);
            }
        });

        shop_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapChoosePOI.this,Shop.class);
                startActivity(intent);
            }
        });

        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(STATIC.UpdateUserData())
                {
                    Toast.makeText(MapChoosePOI.this, "玩家数据存储成功", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MapChoosePOI.this, "存储失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        help_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapChoosePOI.this,Help.class);
                startActivity(intent);
            }
        });

        mapView.onCreate(savedInstanceState);
        resultData = new ArrayList<>();
        init();
        //初始化定位
        initLocation();
        //开始定位
        openGPSSettings();
    }


    @SuppressLint("WifiManagerLeak")
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLoadedListener(this);

        mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

        listView = (ListView) findViewById(R.id.listview);
        searchResultAdapter = new SearchResultAdapter(MapChoosePOI.this);
        searchResultAdapter.setData(resultData);
        listView.setAdapter(searchResultAdapter);

        listView.setOnItemClickListener(onItemClickListener);

        locbtn = (Button)findViewById(R.id.locbtn);
        locbtn.setOnClickListener(this);

        checkinbtn.setOnClickListener(this);
    }

    /**
     * 列表点击监听
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != searchResultAdapter.getSelectedPosition()) {
                PoiItem poiItem = (PoiItem) searchResultAdapter.getItem(position);
                LatLng curLatlng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                isItemClickAction = true;
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(curLatlng));
                searchResultAdapter.setSelectedPosition(position);
                searchResultAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 初始化定位，设置回调监听
     */
    private void initLocation() {
        //初始化client
        mlocationClient = new AMapLocationClient(this.getApplicationContext());
        // 设置定位监听
        mlocationClient.setLocationListener(this);
    }

    /**
     * 设置定位参数
     * @return 定位参数类
     */
    private AMapLocationClientOption getOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setLocationCacheEnable(false);//设置是否返回缓存中位置，默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        return mOption;
    }

    /**
     * 开始定位
     */
    private void startLocation(){
        checkWifiSetting();
        //设置定位参数
        mlocationClient.setLocationOption(getOption());
        // 启动定位
        mlocationClient.startLocation();
    }

    /**
     * 销毁定位
     *
     */
    private void destroyLocation(){
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
            mlocationClient = null;}
    }

    /**
     * 地图移动过程回调
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /**
     * 地图移动结束回调
     * 在这里判断移动距离有无超过500米
     * @param cameraPosition
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

        if (!isItemClickAction && !isLocationAction){
            searchResultAdapter.setSelectedPosition(-1);
            searchResultAdapter.notifyDataSetChanged();
            STATIC.chosenpoi="";
        }
        if (isItemClickAction)
            isItemClickAction = false;
        if (isLocationAction)
            isLocationAction = false;

        if (mcircle != null) {
            if (mcircle.contains(cameraPosition.target)){
                checkinpoint = cameraPosition.target;
            } else{
                Toast.makeText(MapChoosePOI.this, "世界这么大，赶紧去看看", Toast.LENGTH_SHORT).show();
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlocation ,16f ));
            }
        }else {
            startLocation();
            Toast.makeText(MapChoosePOI.this, "重新定位中。。。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 地图加载完成回调
     */
    @Override
    public void onMapLoaded() {
        addMarkerInScreenCenter();
    }

    /**
     * 添加选点marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f,0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
    }
    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            startLocation();
            return;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
            builder.setTitle("熊猫博士提示您："); //设置标题
            builder.setMessage("开启GPS会提升定位准确性"); //设置内容
            builder.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
            builder.setPositiveButton("去开启", new DialogInterface.OnClickListener() { //设置确定按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //关闭dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
                }
            });
            builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //参数都设置完成了，创建并显示出来
            builder.create().show();

        }

    }
    /**
     * 检查wifi，并提示用户开启wifi
     */
    private void checkWifiSetting() {
        if (mWifiManager.isWifiEnabled()) {
            return;
        }
        if(!STATIC.isCheckWifi)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
            builder.setTitle("熊猫博士提示您："); //设置标题
            builder.setMessage("使用WIFI会提升定位准确性"); //设置内容
            builder.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
            builder.setPositiveButton("去开启", new DialogInterface.OnClickListener() { //设置确定按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //关闭dialog
                    Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent); // 打开系统设置界面
                }
            });
            builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            STATIC.isCheckWifi=true;
            //参数都设置完成了，创建并显示出来
            builder.create().show();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        destroyLocation();
    }

    /**
     * 返回定位结果的回调
     * @param aMapLocation 定位结果
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null
                && aMapLocation.getErrorCode() == 0) {
            mlocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            searchLatlonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            checkinpoint = mlocation;
            isLocationAction = true;
            searchResultAdapter.setSelectedPosition(0);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlocation, 16f));
            if (mcircle != null) {
                mcircle.setCenter(mlocation);
            } else {
                mcircle = aMap.addCircle(new CircleOptions().center(mlocation).radius(500).strokeWidth(5));
            }
            if (searchLatlonPoint != null) {
                resultData.clear();
                //resultData.add(new PoiItem("ID", searchLatlonPoint,"我的位置", searchLatlonPoint.toString()));
                doSearchQuery(searchLatlonPoint);
                searchResultAdapter.notifyDataSetChanged();
            }
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr",errText);
        }
    }

    /**
     * 搜索周边poi
     * @param centerpoint
     */
    private void doSearchQuery(LatLonPoint centerpoint) {
        PoiSearch.Query query = new PoiSearch.Query("","","");
        query.setPageSize(20);
        query.setPageNum(0);
        poisearch = new PoiSearch(this,query);
        poisearch.setOnPoiSearchListener(this);
        poisearch.setBound(new PoiSearch.SearchBound(centerpoint, 500, true));
        poisearch.searchPOIAsyn();
    }

    /**
     * 搜索Poi回调
     * @param poiResult 搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {

        if (resultCode == AMapException.CODE_AMAP_SUCCESS){
            if (poiResult != null && poiResult.getPois().size() > 0){
                List<PoiItem> poiItems = poiResult.getPois();
                resultData.addAll(poiItems);
                searchResultAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MapChoosePOI.this, "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MapChoosePOI.this, "搜索失败"+resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ID搜索poi的回调
     * @param poiItem 搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int resultCode) {

    }

    /**
     * Button点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.locbtn:
                startLocation();
                break;
            case R.id.checkinbtn:
                checkin();
                break;
            default:
                break;
        }
    }

    /**
     * 顶点签到，将签到点标注在地图上
     */
    private void checkin() {
        if(STATIC.chosenpoi.equals(""))
        {
            Toast.makeText(MapChoosePOI.this, "未选择所攻打的城池", Toast.LENGTH_SHORT).show();
        }
        else
        {
            switch (STATIC.searchPOI())
            {
                //无人占领
                case 0:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MapChoosePOI.this);  //先得到构造器
                    builder1.setTitle("熊猫博士提示您："); //设置标题
                    builder1.setMessage("该城池暂时无人占领，确定继续发起进攻？"); //设置内容
                    builder1.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
                    builder1.setPositiveButton("就是干！", new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //关闭dialog
                            STATIC.ismeetplayer=false;
                            Random ra=new Random();
                            int tempr=ra.nextInt(10)+1;
                            if(tempr<=7)
                            {
                                STATIC.enemyplayer=new Player("0","土著猪",0,100,10,20,10,0,0,0,0,4,0);
                                Toast.makeText(MapChoosePOI.this, "你遇到了当地的土著", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                STATIC.enemyplayer=new Player("0","土著猪酋长",0,188,10,25,15,0,0,1,1,5,0);
                                Toast.makeText(MapChoosePOI.this, "你遇到了当地的酋长", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent=new Intent(MapChoosePOI.this,Game.class);
                            startActivity(intent);
                        }
                    });
                    builder1.setNegativeButton("我认怂..", new DialogInterface.OnClickListener() { //设置取消按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    //参数都设置完成了，创建并显示出来
                    builder1.create().show();
                    break;
                //自己占领
                case 1:
                    Toast.makeText(MapChoosePOI.this, "你已拥有该城池", Toast.LENGTH_SHORT).show();
                    break;
                //他人占领
                case 2:
                    if(STATIC.getEnemyData())
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MapChoosePOI.this);  //先得到构造器
                        builder2.setTitle("熊猫博士提示您："); //设置标题
                        builder2.setMessage("该城池由玩家 "+STATIC.enemyplayer.name+"(Lv"+STATIC.enemyplayer.level+") "+"所拥有，确定继续发起进攻？"); //设置内容
                        builder2.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
                        builder2.setPositiveButton("就是干！", new DialogInterface.OnClickListener() { //设置确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //关闭dialog
                                STATIC.ismeetplayer=true;
                                Intent intent=new Intent(MapChoosePOI.this,Game.class);
                                startActivity(intent);
                            }
                        });
                        builder2.setNegativeButton("我认怂..", new DialogInterface.OnClickListener() { //设置取消按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        //参数都设置完成了，创建并显示出来
                        builder2.create().show();
                    }
                    else
                    {
                        Toast.makeText(MapChoosePOI.this, "请检查您的网络或防火墙", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    Toast.makeText(MapChoosePOI.this, "请检查您的网络或防火墙", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }



        }
    }

    public void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(MapChoosePOI.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    //退出登录Logout
                    finish();
                    MyApplication.getInstance().exit();
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
