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
import com.hjq.shape.view.ShapeTextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class SinalDialogVIew extends Dialog implements View.OnClickListener, ScrollPickerView.OnSelectedListener {

    private StringScrollPicker stringScrollPicker;
    private TextView titleTv;
    private ShapeTextView sureBtn;

    private TextView cancelBtn;
    //单位
    private TextView dialogUnitTv;
    private OnSelectListener onSelectListener;


    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    //选中的下标
    private int selectPosition = 0;

    public SinalDialogVIew(@NonNull Context context) {
        super(context);
    }

    public SinalDialogVIew(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sinal_layout);


        initViews();

    }

    private void initViews() {
        stringScrollPicker = findViewById(R.id.sinalPicker);
        titleTv = findViewById(R.id.dialogTitleTv);
        cancelBtn = findViewById(R.id.dialogCancelTv);
        sureBtn = findViewById(R.id.dialogSureTv);
        dialogUnitTv = findViewById(R.id.dialogUnitTv);

        stringScrollPicker.setIsCirculation(false);

        stringScrollPicker.setAlignment(Layout.Alignment.ALIGN_CENTER);
        stringScrollPicker.setOnSelectedListener(this);

        cancelBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);

    }


    public void setTitleTxt(String txt){
        titleTv.setText(txt);
    }


    public void setContentScrollData(ArrayList<String> arrayList){
        stringScrollPicker.setData(arrayList);
    }


    public void setUnitShow(boolean isShow,String txt){
        dialogUnitTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
        dialogUnitTv.setText(txt);
    }


    //设置默认选中
    public void setDefaultSelect(int position){
        stringScrollPicker.setSelectedPosition(position);
    }


    public void setSureBtnClick(){
        sureBtn.performClick();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.dialogCancelTv){
            dismiss();
        }

        if(view.getId() == R.id.dialogSureTv){
            dismiss();
            if(onSelectListener != null){
                if(selectPosition>stringScrollPicker.getData().size())
                    return;
                onSelectListener.backSelectData(stringScrollPicker.getData().get(selectPosition).toString());
            }
        }
    }

    @Override
    public void onSelected(ScrollPickerView scrollPickerView, int position) {
        this.selectPosition = position;
    }


    public interface OnSelectListener{
        void backSelectData(String data);
    }
}
