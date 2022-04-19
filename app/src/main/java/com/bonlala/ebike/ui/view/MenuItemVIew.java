package com.bonlala.ebike.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonlala.ebike.R;

import androidx.annotation.Nullable;

public class MenuItemVIew extends LinearLayout {

    private TextView leftTv;
    private TextView rightTv;
    private ImageView rightImg;


    //输入框
    private EditText inputEdit;

    //左侧内容
    private String titleTxt;
    //右侧文字
    private String rightTxt;
    private boolean isShowRightImg;
    //是否显示输入框
    private boolean isShowEdit;


    public MenuItemVIew(Context context) {
        super(context);
    }

    public MenuItemVIew(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MenuItemVIew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs,defStyleAttr);
    }

    public MenuItemVIew(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context,attrs,defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItemVIew,defStyleAttr,0);
        titleTxt = typedArray.getString(R.styleable.MenuItemVIew_menu_title_txt);
        rightTxt = typedArray.getString(R.styleable.MenuItemVIew_menu_right_txt);
        isShowRightImg = typedArray.getBoolean(R.styleable.MenuItemVIew_menu_right_img,true);
        isShowEdit = typedArray.getBoolean(R.styleable.MenuItemVIew_menu_edit_show,false);
        typedArray.recycle();


        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cus_menu_item_layout,this,true);
        leftTv = view.findViewById(R.id.cusMenuItemLeftTv);
        rightTv = view.findViewById(R.id.cusMenuItemRightTv);
        rightImg = view.findViewById(R.id.cusMenuItemRightImg);
        inputEdit = view.findViewById(R.id.cusMenuEditTv);


    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initData();

    }

    private void initData(){
        if(titleTxt != null)
            leftTv.setText(titleTxt);

        if(rightTxt != null)
            rightTv.setText(rightTxt);
        rightImg.setVisibility(isShowRightImg ? VISIBLE :INVISIBLE);
        inputEdit.setVisibility(isShowEdit ? VISIBLE : GONE);
    }

    //设置左侧文字
    public void setTitleTxt(String titleTxt) {
        this.titleTxt = titleTxt;
        if(leftTv != null)
            leftTv.setText(titleTxt);
    }

    //设置右侧文字
    public void setRightTxt(String rightTxt) {
        this.rightTxt = rightTxt;
        if(rightTv != null)
            rightTv.setText(rightTxt);

    }

    //是否显示右侧图片
    public void setShowRightImg(boolean showRightImg) {
        isShowRightImg = showRightImg;
        if(rightImg != null)
            rightImg.setVisibility(showRightImg ? VISIBLE : INVISIBLE);
    }

    public void setShowEdit(boolean showEdit) {
        isShowEdit = showEdit;
        if(inputEdit != null)
            inputEdit.setVisibility(showEdit ? VISIBLE : GONE);
    }

    public void setEditContent(String content){
        if(inputEdit != null)
            inputEdit.setText(content+"");
        postInvalidate();
    }


    public String getInputContent(){
        if(inputEdit == null)
            return null;
        return inputEdit.getText().toString();
    }

    public void setEditFocus(OnEditInput onEdit){
        inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(onEdit != null)
                    onEdit.getInputData(editable.toString());
            }
        });

    }

    public interface OnEditInput{
        void getInputData(String str);
    }
}
