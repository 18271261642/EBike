package com.bonlala.ebike.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bonlala.base.BaseAdapter;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppAdapter;
import com.bonlala.ebike.ui.bean.RouteLinBean;

import java.text.DecimalFormat;

import androidx.annotation.NonNull;

/**
 * 路线规划选择路线adapter
 * Created by Admin
 * Date 2022/4/13
 */
public class RoutePlanAdapter extends AppAdapter<RouteLinBean> {


    private final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    public RoutePlanAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new RoutePlanViewHolder();
    }

    private class RoutePlanViewHolder extends AppAdapter<?>.ViewHolder{

        private TextView routePosition;
        private TextView routeTimeTv;
        private TextView routeDistanceTv;


        public RoutePlanViewHolder() {
            super(R.layout.item_route_plan_layout);
            routePosition = findViewById(R.id.itemRouteNameTv);
            routeTimeTv = findViewById(R.id.itemRouteDuringTv);
            routeDistanceTv = findViewById(R.id.itemRouteDistanceTv);
        }

        @Override
        public void onBindView(int position) {
            RouteLinBean routeLinBean = getItem(position);
            if(routeLinBean == null)return;
            routePosition.setText("路线 "+(position+1));
            routeTimeTv.setText(routeLinBean.getRouteMinute() / 60 +" 分");
            int dis = routeLinBean.getRouteDistance();
            String disStr = "";
            if(dis > 999){
               disStr = dis+" 米";
            }else{
                disStr = decimalFormat.format(Float.valueOf(dis) / 1000) +" 千米";
            }

            routeDistanceTv.setText(disStr);

        }
    }
}
