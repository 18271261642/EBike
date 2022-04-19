package com.bonlala.ebike.ui.activity;

import android.content.Intent;
import android.os.Environment;
import android.view.View;

import com.bonlala.base.BaseAdapter;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.http.HttpData;
import com.bonlala.ebike.http.api.user.CountryApi;
import com.bonlala.ebike.ui.adapter.SelectCountryAdapter;
import com.bonlala.ebike.utils.GetJsonDataUtil;
import com.google.gson.Gson;
import com.hjq.http.EasyConfig;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

/**
 * 选择国家
 * Created by Admin
 * Date 2022/3/30
 */
public class SelectCountryActivity extends AppActivity implements BaseAdapter.OnItemClickListener {

    private List<CountryApi.CountryItemBean> list;
    private SelectCountryAdapter countryAdapter;
    private RecyclerView countryRecyclerView;

    private String filePath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_country_layout;
    }

    @Override
    protected void initView() {

        filePath = Environment.getExternalStorageDirectory().getPath()+"/Download/";

        countryRecyclerView = findViewById(R.id.countryRecyclerView);

        list = new ArrayList<>();
        countryAdapter = new SelectCountryAdapter(this);
        countryAdapter.setOnItemClickListener(this);
        countryRecyclerView.setAdapter(countryAdapter);
    }

    @Override
    protected void initData() {
        getCountryData();
    }


    private void getCountryData(){
        EasyHttp.get(this).api(new CountryApi().getApi()).request(new OnHttpListener<HttpData<List<CountryApi.CountryItemBean>>>() {

            @Override
            public void onSucceed(HttpData<List<CountryApi.CountryItemBean>> result) {
                List<CountryApi.CountryItemBean> tmpList = result.getData();

               // new GetJsonDataUtil().writeTxtToFile(new Gson().toJson(tmpList),filePath,"country.json");

                if(tmpList == null)
                    return;
                countryAdapter.setData(tmpList);

            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        Intent intent = new Intent();
        assert countryAdapter.getData() != null;
        intent.putExtra("select_country_id",countryAdapter.getData().get(position).getId());
        intent.putExtra("select_country",countryAdapter.getData().get(position).getLabelName());
        setResult(0x00,intent);
        finish();
    }
}
