package com.bonlala.ebike.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bonlala.ebike.R;
import com.bonlala.widget.view.ScrollPickerView;
import com.bonlala.widget.view.StringScrollPicker;
import com.hjq.shape.view.ShapeTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 身高选择dialog
 * Created by Admin
 * Date 2022/3/28
 */
public class HeightSelectDialog {

    public static final class Builder extends CommonDialog.Builder<Builder>{

        private SignalSelectListener signalSelectListener;

        private int selectPosition = 0;

        //选择器
        private StringScrollPicker stringScrollPicker;
        //单位 cm/kg...
        private TextView dialogHeightUnitTv;


        //选中的框框
        private ShapeTextView dialogHeightBorderTv;

        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.dialog_height_layout);
            initViews();

        }


        public Builder setSignalSelectListener(SignalSelectListener signalSelectListener) {
            this.signalSelectListener = signalSelectListener;
            return this;
        }

        private void initViews(){
            setTitle("身高");
            dialogHeightUnitTv = findViewById(R.id.dialogHeightUnitTv);
            stringScrollPicker = findViewById(R.id.dialogHeightPick);
            dialogHeightBorderTv = findViewById(R.id.dialogHeightBorderTv);

            List<String> hList = new ArrayList<>();
            for(int i = 100;i<300;i++){
                hList.add(i+"");
            }

            stringScrollPicker.setData(hList);

            stringScrollPicker.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
                @Override
                public void onSelected(ScrollPickerView scrollPickerView, int position) {
                    selectPosition = position;

                }
            });
        }


        //显示字体颜色，用于不同背景样式
        public Builder setSelectItemColor(boolean isWhite){
            if(dialogHeightUnitTv == null)
                return null;
            dialogHeightUnitTv.setTextColor(isWhite ? Color.BLACK : Color.WHITE);
            if(stringScrollPicker == null)
                return  null;
            stringScrollPicker.setColor(isWhite ? Color.BLACK : Color.WHITE ,isWhite ? Color.BLACK : Color.WHITE);
            if(isWhite){
                dialogHeightBorderTv.getShapeDrawableBuilder().setStrokeColor(Color.parseColor("#F2F2F7")).intoBackground();
            }

            setBgAlpha(isWhite);
            return this;
        }



        //是否显示右侧单位
        public Builder setUnitShow(boolean isShow,String unitTxt){
            if(dialogHeightUnitTv == null)
                return null;
            dialogHeightUnitTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
            dialogHeightUnitTv.setText(unitTxt);
            return this;
        }

        public Builder setSourceData(List<String> sorList){
            if(stringScrollPicker == null)
                return  null;
            stringScrollPicker.setData(sorList);
            return this;
        }


        public List<CharSequence> getSourceData(){
            return stringScrollPicker.getData();
        }


        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.tv_ui_confirm) {
                autoDismiss();
                if (signalSelectListener == null) {
                    return;
                }
                signalSelectListener.onSignalSelect(stringScrollPicker.getData().get(selectPosition).toString());
            } else if (viewId == R.id.tv_ui_cancel) {
                autoDismiss();

            }
        }
    }



    public interface SignalSelectListener{
        void onSignalSelect(String txt);
    }

}
