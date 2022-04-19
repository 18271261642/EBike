package com.bonlala.ebike.ui.amap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.amap.api.maps.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bonlala.base.BaseAdapter;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.ui.adapter.MapSearchAdapter;
import com.bonlala.ebike.ui.bean.SearLocalBean;
import com.bonlala.ebike.utils.BikeUtils;
import com.bonlala.widget.view.ClearEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

/**
 * 高德地图搜索位置页面
 * Created by Admin
 * Date 2022/4/7
 */
public class AmapSearchPlaceActivity extends AppActivity implements OnGetSuggestionResultListener {

    private static final String TAG = "AmapSearchPlaceActivity";

    //搜索框
    private ClearEditText searchInputEdit;
    //搜索图片按钮
    private ImageView mapSearchImgView;

    private List<SearLocalBean> resultList;
    private MapSearchAdapter mapSearchAdapter;
    private RecyclerView searchPlaceRecyclerView;

    private SuggestionSearch mSuggestionSearch = null;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_place_layout;
    }

    @Override
    protected void initView() {
        findViews();

        resultList = new ArrayList<>();
        mapSearchAdapter = new MapSearchAdapter(this,new LatLng(22.777897,113.848335));
        mapSearchAdapter.setOnItemClickListener(onItemClickListener);
        searchPlaceRecyclerView.setAdapter(mapSearchAdapter);


        searchInputEdit.setOnInputAfterListener(new ClearEditText.OnInputAfterListener() {
            @Override
            public void afterTextChanged(Editable d) {
                if(BikeUtils.isEmpty(d.toString()))
                    return;
                searchPlace(d.toString());
            }
        });
    }

    @Override
    protected void initData() {
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }


    @Override
    protected boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    protected boolean isStatusBarDarkFont() {
        return super.isStatusBarDarkFont();
    }


    private void findViews(){
        searchInputEdit = findViewById(R.id.searchInputEdit);
        mapSearchImgView = findViewById(R.id.mapSearchImgView);
        searchPlaceRecyclerView = findViewById(R.id.searchPlaceRecyclerView);
        findViewById(R.id.mapBackImgView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setOnClickListener(mapSearchImgView);

    }


    @Override
    public void onClick(View view) {
        if(view == mapSearchImgView){   //搜索
            String inputStr = searchInputEdit.getText().toString();
            if(BikeUtils.isEmpty(inputStr))
                return;
            searchPlace(inputStr);
        }
    }


    private void searchPlace(String inputStr){
        String defaultCity = null;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("map_bundle");
        if(bundle != null){
            defaultCity = bundle.getString("city_key");

        }
        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(inputStr) // 关键字
                .citylimit(false)
                .city(defaultCity == null ? "深圳" : defaultCity));// 城市

    }



    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if(suggestionResult == null)
            return;
        resultList.clear();
        Timber.e("------搜索返回="+new Gson().toJson(suggestionResult));
        List<SuggestionResult.SuggestionInfo> suggestionInfoList = suggestionResult.getAllSuggestions();
        for(SuggestionResult.SuggestionInfo suggestionInfo : suggestionInfoList){
            SearLocalBean searLocalBean = new SearLocalBean(suggestionInfo.getKey(),suggestionInfo.getAddress(),"0",suggestionInfo.getPt().latitude,suggestionInfo.getPt().longitude);
            resultList.add(searLocalBean);
        }

        mapSearchAdapter.setData(resultList);
    }


    private final BaseAdapter.OnItemClickListener onItemClickListener = new BaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("map_bundle");
            if(bundle != null){
                SearLocalBean searLocalBean = mapSearchAdapter.getItem(position);
                int code = bundle.getInt("local_code");
                Bundle sBundle = new Bundle();
                sBundle.putDouble("lat",searLocalBean.getLat());
                sBundle.putDouble("lon",searLocalBean.getLon());
                sBundle.putString("desc",searLocalBean.getName());
                intent.putExtra("mapBundle",sBundle);

                intent.putExtra("aa","---bb");
                setResult(code,intent);
                finish();
            }

        }
    };
}
