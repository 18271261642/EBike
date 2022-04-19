package com.bonlala.ebike.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bonlala.ebike.R;
import com.bonlala.widget.view.CircleProgress;
import androidx.annotation.Nullable;

/**
 * Created by Admin
 * Date 2022/4/8
 */
public class CusMapCircleView extends LinearLayout {


    //圆形进度条
    private CircleProgress cusMapLayoutCircleViewProgressView;
    //总里程
    private TextView cusMapLayoutOdoTv;
    //运行时间
    private TextView cusMapLayoutTimeTv;

    //平均速度
    private TextView cusMapLayoutSpeedTv;
    //电量值
    private TextView cusMapLayoutBatteryValueTv;
    //电量图片
    private ImageView cusMapLayoutBatteryValueImgView;

    //档位
    private TextView cusMapLayoutSearTv;




    public CusMapCircleView(Context context) {
        super(context);
    }

    public CusMapCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public CusMapCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs,defStyleAttr);
    }

    public CusMapCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context,attrs,defStyleAttr);
    }



    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CusMapCircleView,defStyleAttr,0);


        typedArray.recycle();
        
        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cus_amap_preview_layout,this,true);
        cusMapLayoutCircleViewProgressView = view.findViewById(R.id.cusMapLayoutCircleViewProgressView);
        cusMapLayoutOdoTv = view.findViewById(R.id.cusMapLayoutOdoTv);
        cusMapLayoutTimeTv = view.findViewById(R.id.cusMapLayoutTimeTv);
        cusMapLayoutSpeedTv = view.findViewById(R.id.cusMapLayoutSpeedTv);
        cusMapLayoutBatteryValueTv = view.findViewById(R.id.cusMapLayoutBatteryValueTv);
        cusMapLayoutBatteryValueImgView = view.findViewById(R.id.cusMapLayoutBatteryValueImgView);
        cusMapLayoutSearTv = view.findViewById(R.id.cusMapLayoutSearTv);

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    //设置进度条的值，最大值和最小值
    public void setCircleMaxAndCurrValue(float maxValue,float currentValue){
        if(cusMapLayoutCircleViewProgressView == null)
            return;
        cusMapLayoutCircleViewProgressView.setMaxValue(maxValue);
        cusMapLayoutCircleViewProgressView.setValue(currentValue);

        invalidate();
    }


    //设置平均速度
    public void setAvgSpeedValue(String speedValue){
        if(cusMapLayoutSpeedTv == null)
            return;
        speedValue = speedValue+"km/h";
        SpannableString spannableString = new SpannableString(speedValue);
        spannableString.setSpan(new AbsoluteSizeSpan(12,true),speedValue.length()-4,speedValue.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);


        cusMapLayoutSpeedTv.setText(spannableString);
    }



    //设置电量
    public void setBatteryValue(String batteryValue){
        if(cusMapLayoutBatteryValueTv == null)
            return;
        cusMapLayoutBatteryValueTv.setText(batteryValue+"%");
    }

    //设置运动时间
    public void setSportTime(String timeStr){
        if(cusMapLayoutTimeTv == null)
            return;
        cusMapLayoutTimeTv.setText(timeStr);
    }

    //设置运动距离
    public void setRideDistance(String distance){
        if(cusMapLayoutOdoTv == null)
            return;

        distance = distance+"km";
        SpannableString spannableString = new SpannableString(distance);
        spannableString.setSpan(new AbsoluteSizeSpan(12,true),distance.length()-2,distance.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        cusMapLayoutOdoTv.setText(distance);
    }

    //设置档位
    public void setGear(int gear){
        if(cusMapLayoutSearTv == null)
            return;
        cusMapLayoutSearTv.setText(gear+"");

    }
}
