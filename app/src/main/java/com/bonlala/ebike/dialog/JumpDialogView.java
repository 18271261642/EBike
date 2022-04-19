package com.bonlala.ebike.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.bonlala.ebike.R;
import com.hjq.shape.layout.ShapeLinearLayout;
import com.hjq.shape.view.ShapeTextView;
import androidx.appcompat.app.AppCompatDialog;

/**
 * 完善用户信息dialog提示
 * Created by Admin
 * Date 2022/3/28
 */
public class JumpDialogView extends AppCompatDialog implements View.OnClickListener {


    private OnJumpDialogListener onJumpDialogListener;

    public void setOnJumpDialogListener(OnJumpDialogListener onJumpDialogListener) {
        this.onJumpDialogListener = onJumpDialogListener;
    }

    private ShapeLinearLayout jumpContentLayout;

    //跳过
    private ShapeTextView jumpJumpTv;
    //继续
    private TextView jumpContinueTv;



    public JumpDialogView(Context context) {
        super(context);
    }

    public JumpDialogView(Context context, int theme) {
        super(context, R.style.edit_AlertDialog_style);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jump_layout);

        initViews();


    }

    private void initViews() {
        jumpContentLayout = findViewById(R.id.jumpContentLayout);
        assert jumpContentLayout != null;
        jumpContentLayout.getBackground().setAlpha(90);

        jumpJumpTv = findViewById(R.id.jumpJumpTv);
        jumpContinueTv = findViewById(R.id.jumpContinueTv);
        jumpJumpTv.setOnClickListener(this);
        jumpContinueTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.jumpJumpTv){    //跳过
            if(onJumpDialogListener != null)
                onJumpDialogListener.jumpClick();
        }
        if(view.getId() == R.id.jumpContinueTv){    //继续
            dismiss();
            if(onJumpDialogListener != null)
                onJumpDialogListener.continueClick();
        }
    }

    public interface OnJumpDialogListener{
        void jumpClick();
        void continueClick();
    }
}
