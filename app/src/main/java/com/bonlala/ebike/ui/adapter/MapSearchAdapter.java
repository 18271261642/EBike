package com.bonlala.ebike.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppAdapter;
import com.bonlala.ebike.ui.bean.SearLocalBean;
import java.text.DecimalFormat;
import androidx.annotation.NonNull;

/**
 * 地址搜索返回adapter
 * Created by Admin
 * Date 2022/4/7
 */
public class MapSearchAdapter extends AppAdapter<SearLocalBean> {

    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    //当前位置的经纬度
    private LatLng localLatLng;


    public MapSearchAdapter(@NonNull Context context, LatLng localLatLng) {
        super(context);
        this.localLatLng = localLatLng;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MapSearchViewHolder();
    }


    private class MapSearchViewHolder extends  AppAdapter<?>.ViewHolder{

        private TextView name,districtTv,distanceTv;

        public MapSearchViewHolder() {
            super(R.layout.item_map_search_layout);

            name = findViewById(R.id.itemMapSearchNameTv);
            districtTv = findViewById(R.id.itemMapSearchDescTv);
            distanceTv = findViewById(R.id.itemMapSearchDistanceTv);
        }

        @Override
        public void onBindView(int position) {
            if(getData() == null)
                return;
            SearLocalBean searLocalBean = getData().get(position);

            name.setText(searLocalBean.getName());
            districtTv.setText(searLocalBean.getDistrict());
            float dis = AMapUtils.calculateLineDistance(localLatLng,new LatLng(searLocalBean.getLat(),searLocalBean.getLon()));
            distanceTv.setText(decimalFormat.format(dis/1000)+"km");
        }
    }
}
