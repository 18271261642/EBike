package com.bonlala.ebike.dialog;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.bonlala.ebike.R;
import com.bonlala.ebike.http.net.TestView;

/**
 * Created by Admin
 * Date 2022/3/30
 */
public class SexDialogView {

    public static final class Builder extends CommonDialog.Builder<Builder>{


        private TextView manTx,womenTv;
        private TextView sexContentTv;

        private OnSexSelectListener onSexSelectListener;



        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.dialog_sex_select_layout);

            setCommBtnLayoutVisible(false);

            initViews();

        }

        private void initViews() {
            manTx = findViewById(R.id.dialogSexMainTv);
            womenTv = findViewById(R.id.dialogSexWomenTv);
            sexContentTv = findViewById(R.id.sexContentTv);
            setTitle("性别");

            manTx.setOnClickListener(this);
            womenTv.setOnClickListener(this);

        }

        public Builder setOnSexSelectListener(OnSexSelectListener onSexSelectListener) {
            this.onSexSelectListener = onSexSelectListener;
            return this;
        }


        public Builder setIsWhiteBg(boolean isWhite){
            setBgAlpha(isWhite);
            return this;
        }

        //设置按钮
        public Builder setLeftAndRightTxt(String leftTxt,String rightTxt){
            manTx.setText(leftTxt);
            womenTv.setText(rightTxt);
            return this;
        }

        public Builder setContentTxt(String txt){
            sexContentTv.setText(txt);
            return this;
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.dialogSexMainTv){
                dismiss();
                if(onSexSelectListener != null)
                    onSexSelectListener.selectSex(0);
            }

            if(view.getId() == R.id.dialogSexWomenTv){
                dismiss();
                if(onSexSelectListener != null)
                    onSexSelectListener.selectSex(1);
            }
        }
    }

    public interface OnSexSelectListener{
        void selectSex(int sex); //0-男;1-女
    }
}
