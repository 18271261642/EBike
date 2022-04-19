package com.bonlala.ebike.ui.ble;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bonlala.base.BaseAdapter;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppAdapter;
import com.bonlala.ebike.http.GlideApp;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hjq.shape.view.ShapeView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 表盘adapter
 * Created by Admin
 * Date 2022/4/11
 */
public class DialAdapter extends AppAdapter<DialBean> {


    public DialAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    protected RecyclerView.LayoutManager generateDefaultLayoutManager(Context context) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3);
        return gridLayoutManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DialViewHolder();
    }

    private class DialViewHolder extends AppAdapter<?>.ViewHolder{

        private ImageView itemDialImg;
        private ShapeView itemDialCheckView;

        public DialViewHolder() {
            super(R.layout.item_dial_layout);
            itemDialImg = findViewById(R.id.itemDialImgView);
            itemDialCheckView = findViewById(R.id.itemDialCheckView);
        }

        @Override
        public void onBindView(int position) {
            DialBean dialBean = getItem(position);
            if(dialBean == null)
                return;
            // 显示圆角的 ImageView
            GlideApp.with(getContext())
                    .load(Integer.parseInt(dialBean.getPreviewUrl()))
                    .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners((int) getResources().getDimension(R.dimen.dp_10))))
                    .into(itemDialImg);
            itemDialCheckView.setVisibility(dialBean.isCurrentDial() ? View.VISIBLE : View.GONE);

        }
    }
}
