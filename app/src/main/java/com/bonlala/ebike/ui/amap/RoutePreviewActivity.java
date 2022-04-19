package com.bonlala.ebike.ui.amap;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.bikenavi.params.BikeRouteNodeInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.bonlala.blelibrary.CusLocalBean;
import com.bonlala.blelibrary.OnCusLocationListener;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.ui.adapter.RoutePlanAdapter;
import com.bonlala.ebike.ui.bean.RouteLinBean;
import com.bonlala.ebike.ui.bean.SearLocalBean;
import com.google.gson.Gson;
import com.hjq.shape.view.ShapeButton;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

/**
 * 路线规划
 * Created by Admin
 * Date 2022/4/9
 */
public class RoutePreviewActivity extends AppActivity implements OnGetRoutePlanResultListener {


    //开始的位置信息
    private SearLocalBean startBean;
    //结束
    private SearLocalBean endBean;


    //当前位置
    private TextView mapPlanLocalTv;
    //目的地位置
    private TextView mapPlanGoalTv;
    //开始导航
    private ShapeButton routeStartBtn;

    //地图
    private MapView routePlanMapView;
    //地图
    private BaiduMap baiduMap;
    //定位服务
    private LocationService locationService;

    private BikeNaviLaunchParam bikeParam;

    // 搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private BikingRouteResult mBikingRouteResult = null;
    private RouteLine mRouteLine = null;

    private OverlayManager routeOverlay = null;


    private RecyclerView routeRecyclerView;
    private List<RouteLinBean> linBeanList;
    private RoutePlanAdapter routePlanAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_route_plan_layout;
    }

    @Override
    protected void initView() {
        LocationClient.setAgreePrivacy(true);
        findViews();

    }

    @Override
    protected void initData() {
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        if(startBean == null)
            startBean = new SearLocalBean();
        if(endBean == null)
            endBean = new SearLocalBean();

        linBeanList = new ArrayList<>();
        routePlanAdapter = new RoutePlanAdapter(this);
        routeRecyclerView.setAdapter(routePlanAdapter);

    }




    private void findViews() {
        mapPlanLocalTv = findViewById(R.id.mapPlanLocalTv);
        mapPlanGoalTv = findViewById(R.id.mapPlanGoalTv);
        routePlanMapView = findViewById(R.id.routePlanMapView);
        routeStartBtn = findViewById(R.id.routeStartBtn);
        routeRecyclerView = findViewById(R.id.routeRecyclerView);


        baiduMap = routePlanMapView.getMap();

        //不显示缩放按钮
        routePlanMapView.showZoomControls(false);
        //开启地图的定位图层
        baiduMap.setMyLocationEnabled(true);

        findViewById(R.id.routePlanBackLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setOnClickListener(mapPlanLocalTv, mapPlanGoalTv,routeStartBtn);

        locationService  = new LocationService(getApplicationContext());
        locationService.registerListener(new MyCusListener());
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.requestLocation();
        locationService.start();
    }

    @Override
    protected boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }



    private void searchRouteLin(){
        baiduMap.clear();
        if(startBean.getLat() == 0)
            return;
        // 设置起终点信息 起点参数
        PlanNode startNode = PlanNode.withLocation(new LatLng(startBean.getLat(),startBean.getLon()));
        // 终点参数
        PlanNode endNode = PlanNode.withLocation(new LatLng(endBean.getLat(),endBean.getLon()));
        // 步行路线规划参数
        BikingRoutePlanOption bikingRoutePlanOption = new BikingRoutePlanOption().from(startNode).to(endNode);
        // 骑行类型（0：普通骑行模式，1：电动车模式）默认是普通模式
        bikingRoutePlanOption.ridingType(0);
        // 发起骑行路线规划检索
        mSearch.bikingSearch(bikingRoutePlanOption);


    }




    private void MotoRoutePlan(){
        BNRoutePlanNode sNode = new BNRoutePlanNode.Builder()
                .latitude(startBean.getLat())
                .longitude(startBean.getLon())
//                .name("百度大厦")
//                .description("百度大厦")
                .build();
        BNRoutePlanNode eNode = new BNRoutePlanNode.Builder()
                .latitude(endBean.getLat())
                .longitude(endBean.getLon())
//                .name("北京天安门")
//                .description("北京天安门")
                .build();
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        BaiduNaviManagerFactory.getRoutePlanManager().routePlanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_ROAD_FIRST,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(RoutePreviewActivity.this.getApplicationContext(),
                                        "算路开始", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(RoutePreviewActivity.this.getApplicationContext(),
                                        "算路成功", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(RoutePreviewActivity.this.getApplicationContext(),
                                        "算路失败", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(RoutePreviewActivity.this.getApplicationContext(),
                                        "算路成功准备进入导航", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(RoutePreviewActivity.this,
//                                        BdMotoNaviActivity.class);

                                Intent intent = new Intent(RoutePreviewActivity.this,
                                        BdAnalogActivity.class);

                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });

    }




    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, AmapSearchPlaceActivity.class);

        bundle.putString("city_key",startBean.getCity());
        if (view == mapPlanLocalTv) { //当前位置
            bundle.putInt("local_code", 0x00);

            intent.putExtra("map_bundle",bundle);
            startActivityForResult(intent, onActivityCallback);
        }

        if (view == mapPlanGoalTv) {  //选择位置
            bundle.putInt("local_code", 0x01);
            intent.putExtra("map_bundle",bundle);
            startActivityForResult(intent, onActivityCallback);
        }

        if(view == routeStartBtn){

            MotoRoutePlan();
//            initNavite();
        }
    }



    private void initNavite(){




        /*构造导航起终点参数对象*/
        BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
        bikeStartNode.setLocation(new LatLng(startBean.getLat(),startBean.getLon()));
        BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
        bikeEndNode.setLocation(new LatLng(endBean.getLat(),endBean.getLon()));
        bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);


        // 获取导航控制类
        // 引擎初始化
        BikeNavigateHelper.getInstance().initNaviEngine(this, new IBEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                //骑行导航初始化成功之后的回调
                routePlanWithBikeParam();
            }

            @Override
            public void engineInitFail() {
                //骑行导航初始化失败之后的回调
            }
        });

    }


    /**
     * 发起骑行导航算路
     */
    private void routePlanWithBikeParam() {
        BikeNavigateHelper.getInstance().routePlanWithRouteNode(bikeParam, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Timber.d("BikeNavi onRoutePlanStart");
//                Intent intent = new Intent();
//                intent.setClass(BNaviMainActivity.this, BNaviGuideActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onRoutePlanSuccess() {
                Timber.d( "BikeNavi onRoutePlanSuccess");
                Intent intent = new Intent();
//                intent.setClass(BNaviMainActivity.this, BNaviGuideActivity.class);
//                startActivity(intent);

                startActivity(BNaviLinActivity.class);
             //   setResult(0x00,intent);

            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError error) {
                Timber.d("BikeNavi onRoutePlanFail");
            }

        });
    }


    private final OnActivityCallback onActivityCallback = new OnActivityCallback() {
        @Override
        public void onActivityResult(int resultCode, @Nullable Intent data) {
            Timber.e("----resultCode="+resultCode);
            if(data == null)
                return;
            Bundle mapBundle = data.getBundleExtra("mapBundle");
            if(mapBundle == null)
                return;
            String desc = mapBundle.getString("desc");
            double lat = mapBundle.getDouble("lat");
            double lon = mapBundle.getDouble("lon");


            if (resultCode == 0x00) {     //当前
                mapPlanLocalTv.setText(desc);
            }
            if (resultCode == 0x01) {     //目标
                endBean.setLat(lat);
                endBean.setLon(lon);
                mapPlanGoalTv.setText(desc);

                searchRouteLin();
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        routePlanMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        routePlanMapView.onPause();
        locationService.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        routePlanMapView.onDestroy();
        if (mSearch != null) {
            // 释放检索对象
            mSearch.destroy();
        }
        baiduMap.clear();
        locationService.unregisterListener(new MyCusListener());
    }



    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    //骑行路线回调
    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        linBeanList.clear();
        if(bikingRouteResult == null || bikingRouteResult.getRouteLines() == null){
            ToastUtils.show("路线规划有误!");
            return;
        }
        Timber.e("======规划结果=%s", bikingRouteResult.getRouteLines().size());
        Timber.e("-------骑行规划返回=%s", new Gson().toJson(bikingRouteResult));

        if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {

            if (bikingRouteResult.getRouteLines().size() >= 1) {
                mRouteLine = bikingRouteResult.getRouteLines().get(0);

               // mRouteLine.setSteps(bikingRouteResult.getRouteLines());
                for(RouteLine routeLine : bikingRouteResult.getRouteLines()){
                    RouteLinBean routeLinBean = new RouteLinBean();
                    routeLinBean.setRouteMinute(routeLine.getDuration());
                    routeLinBean.setRouteDistance(routeLine.getDistance());
                    linBeanList.add(routeLinBean);
                }

                routePlanAdapter.setData(linBeanList);

                Timber.e("------耗时="+mRouteLine.getTitle()+"\n"+mRouteLine.getDuration()+"\n"+mRouteLine.getDistance());

                BikingRouteOverlay overlay = new MyBikingRouteOverlay(baiduMap);
                routeOverlay = overlay;
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(bikingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }

        }


    }


    private class MyCusListener extends BDAbstractLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Timber.e("------定位="+bdLocation.getAddrStr()+" "+bdLocation.getLatitude());

            if (bdLocation == null || routePlanMapView == null) {
                return;
            }

            startBean.setLat(bdLocation.getLatitude());
            startBean.setLon(bdLocation.getLongitude());

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            baiduMap.setMyLocationData(locData);

            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }



    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        private MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            //return BitmapDescriptorFactory.fromResource(R.drawable.ic_home_device_set);

            return null;

        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            //return BitmapDescriptorFactory.fromResource(R.drawable.ic_home_device_navite);
            return null;
        }
    }
}
