package com.bonlala.ebike.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import com.bonlala.ebike.R;
import com.bonlala.widget.view.ScrollPickerView;
import com.bonlala.widget.view.StringScrollPicker;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class DoubleDialogVIew extends Dialog implements View.OnClickListener, ScrollPickerView.OnSelectedListener {

    private StringScrollPicker stringScrollPicker1;
    private StringScrollPicker stringScrollPicker2;
    private TextView doubleDialogTitleTv;
    private TextView doubleDialogCancelTv,doubleDialogSureTv;
    //单位
    private TextView doubleDialogUnitTv;
    private OnDoubleSelectListener onSelectListener;


    public void setOnSelectListener(OnDoubleSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    //左侧选中的下标
    private int selectLeftPosition = 0;
    //右侧选中的下标
    private int selectRightPosition = 0;

    public DoubleDialogVIew(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_double_layout);


        initViews();

    }

    private void initViews() {
        stringScrollPicker1 = findViewById(R.id.sinalPicker1);
        stringScrollPicker2 = findViewById(R.id.sinalPicker2);
        doubleDialogTitleTv = findViewById(R.id.doubleDialogTitleTv);
        doubleDialogCancelTv = findViewById(R.id.doubleDialogCancelTv);
        doubleDialogSureTv = findViewById(R.id.doubleDialogSureTv);
        doubleDialogUnitTv = findViewById(R.id.doubleDialogUnitTv);

        stringScrollPicker1.setIsCirculation(true);

        stringScrollPicker1.setAlignment(Layout.Alignment.ALIGN_CENTER);
        stringScrollPicker1.setOnSelectedListener(this);
        stringScrollPicker2.setOnSelectedListener(this);

        doubleDialogCancelTv.setOnClickListener(this);
        doubleDialogSureTv.setOnClickListener(this);

    }


    public void setTitleTxt(String txt){
        doubleDialogTitleTv.setText(txt);
    }

    //设置左侧数据源
    public void setContentScrollData1(ArrayList<String> arrayList){
        stringScrollPicker1.setData(arrayList);
    }

    //设置右侧数据源
    public void setContentScrollData2(ArrayList<String> arrayList){
        stringScrollPicker2.setData(arrayList);
    }


    //设置默认选中
    public void setDefaultPosition(int left,int right){
        stringScrollPicker1.setSelectedPosition(left);
        stringScrollPicker2.setSelectedPosition(right);
    }


    public void setUnitShow(boolean isShow,String txt){
        doubleDialogUnitTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
        doubleDialogUnitTv.setText(txt);
    }


    public void setSureBtnClick(){
        doubleDialogSureTv.performClick();
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.doubleDialogCancelTv){
            dismiss();
        }

        if(view.getId() == R.id.doubleDialogSureTv){
            dismiss();
            if(onSelectListener != null){
                onSelectListener.backSelectData(stringScrollPicker1.getData().get(selectLeftPosition).toString(),stringScrollPicker2.getData().get(selectRightPosition).toString());
            }
        }
    }

    @Override
    public void onSelected(ScrollPickerView scrollPickerView, int position) {
        if(scrollPickerView.getId() == R.id.sinalPicker1){
            this.selectLeftPosition = position;
        }

        if(scrollPickerView.getId() == R.id.sinalPicker2){
            this.selectRightPosition = position;
        }

    }


    public interface OnDoubleSelectListener{
        void backSelectData(String leftData,String rightData);
    }
}
