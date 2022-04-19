package com.bonlala.ebike.ui.amap;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRouteGuidanceListener;
import com.baidu.mapapi.bikenavi.adapter.IBTTSPlayer;
import com.baidu.mapapi.bikenavi.model.BikeNaviDisplayOption;
import com.baidu.mapapi.bikenavi.model.BikeRouteDetailInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.walknavi.model.RouteGuideKind;
import com.baidu.navisdk.adapter.IBNRouteGuideManager;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.ebike.utils.LogcatHelper;
import com.bonlala.ebike.utils.MmkvUtils;
import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

/**
 * 导航中页面
 * Created by Admin
 * Date 2022/4/13
 */
public class BNaviLinActivity extends Activity {

    private static final String TAG = "BNaviLinActivity";

    private BikeNavigateHelper mNaviHelper;
    private FrameLayout bmMapViewLayout;
    private BaiduMap baiduMap;
    //状态图片
    private ImageView naviStatusImgView;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initMapView();


    }



    private void initMapView(){
        LogcatHelper.getInstance(this).start();
        View parentView = LayoutInflater.from(this).inflate(R.layout.activity_bd_navi_layout,null,true);
        bmMapViewLayout = parentView.findViewById(R.id.bmMapViewLayout);
        naviStatusImgView = parentView.findViewById(R.id.naviStatusImgView);
        mNaviHelper = BikeNavigateHelper.getInstance();

        View view = mNaviHelper.onCreate(BNaviLinActivity.this);

        if(bmMapViewLayout != null){
            bmMapViewLayout.addView(view);
        }
        setContentView(parentView);

        mNaviHelper.setTTsPlayer(new IBTTSPlayer() {
            /**
             * 获取导航过程中语音,进行播报
             * @param s 播报语音文本
             * @param b 是否抢占播报
             */
            @Override
            public int playTTSText(String s, boolean b) {
                //调用语音识别SDK的语音回调进行播报
                return 0;
            }
        });
        mNaviHelper.setRouteGuidanceListener(this,ibRouteGuidanceListener);
        mNaviHelper.startBikeNavi(this);
        BikeNaviDisplayOption bikeNaviDisplayOption = new BikeNaviDisplayOption();
        bikeNaviDisplayOption.setTopGuideLayout(1);
        mNaviHelper.setBikeNaviDisplayOption(bikeNaviDisplayOption);
        MapView mapView = mNaviHelper.getNaviMap();
        if(mapView != null){
            mapView.showZoomControls(false);
            baiduMap = mapView.getMap();
            initMapSets();
        }
    }



    private void initMapSets(){
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,null);
        baiduMap.setMyLocationConfiguration(myLocationConfiguration);
        baiduMap.getUiSettings().setZoomGesturesEnabled(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogcatHelper.getInstance(this).stop();
        stringBuilder.append(BikeUtils.getCurrDate2()+"\n");
        MmkvUtils.setSaveParams("bd_map",stringBuilder.toString());
        mNaviHelper.quit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNaviHelper.resume();
    }

    private StringBuilder stringBuilder = new StringBuilder();

    private final IBRouteGuidanceListener ibRouteGuidanceListener = new IBRouteGuidanceListener() {

        //诱导图标更新
        @Override
        public void onRouteGuideIconUpdate(Drawable drawable) {
            Log.e(TAG,"------------");
            naviStatusImgView.setImageDrawable(drawable);

        }

        //诱导类型枚举  转向
        @Override
        public void onRouteGuideKind(RouteGuideKind routeGuideKind) {
            Log.e(TAG,"-----onRouteGuideKind-------="+routeGuideKind);
            stringBuilder.append("onRouteGuideKind转向="+routeGuideKind+"\n");
        }

        //诱导信息
        @Override
        public void onRoadGuideTextUpdate(CharSequence charSequence, CharSequence charSequence1) {
            Log.e(TAG,"-----onRoadGuideTextUpdate-------="+charSequence+" ="+charSequence1);
            stringBuilder.append("onRoadGuideTextUpdate=1="+charSequence.toString()+" 2=" +charSequence1.toString()+"\n");
        }

        //总的剩余距离
        @Override
        public void onRemainDistanceUpdate(CharSequence charSequence) {
            Log.e(TAG,"------onRemainDistanceUpdate------"+charSequence.toString());
            stringBuilder.append("onRemainDistanceUpdate总的剩余距离="+charSequence.toString()+"\n");
        }

        //总的剩余时间
        @Override
        public void onRemainTimeUpdate(CharSequence charSequence) {
            Log.e(TAG,"------onRemainTimeUpdate------="+charSequence.toString());
            stringBuilder.append("onRemainTimeUpdate="+charSequence.toString()+"\n");
        }

        //GPS状态发生变化，来自诱导引擎的消息
        @Override
        public void onGpsStatusChange(CharSequence charSequence, Drawable drawable) {
            Log.e(TAG,"------onGpsStatusChange------");
            stringBuilder.append("onGpsStatusChange="+charSequence.toString()+"\n");
        }

        //已经开始偏航
        @Override
        public void onRouteFarAway(CharSequence charSequence, Drawable drawable) {
            Log.e(TAG,"------onRouteFarAway------");
            stringBuilder.append("onGpsStatusChange="+charSequence.toString()+"\n");
        }

        //偏航规划中
        @Override
        public void onRoutePlanYawing(CharSequence charSequence, Drawable drawable) {
            Log.e(TAG,"-----onRoutePlanYawing-------="+charSequence.toString());
            stringBuilder.append("onRoutePlanYawing="+charSequence.toString()+"\n");
        }

        //重新算路成功
        @Override
        public void onReRouteComplete() {
            Log.e(TAG,"------onReRouteComplete------");
            stringBuilder.append("onReRouteComplete="+"\n");
        }

        //到达目的地
        @Override
        public void onArriveDest() {
            Log.e(TAG,"------onArriveDest------");
            stringBuilder.append("onArriveDest="+"\n");
        }

        //震动
        @Override
        public void onVibrate() {
            Log.e(TAG,"------------");
            stringBuilder.append("onVibrate="+"\n");
        }

        //获取骑行导航路线详细信息类
        @Override
        public void onGetRouteDetailInfo(BikeRouteDetailInfo bikeRouteDetailInfo) {
            Log.e(TAG,"-------onGetRouteDetailInfo----="+new Gson().toJson(bikeRouteDetailInfo));
            stringBuilder.append("onGetRouteDetailInfo="+new Gson().toJson(bikeRouteDetailInfo)+"\n");
        }
    };

}
