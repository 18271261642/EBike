package com.bonlala.ebike.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bonlala.base.BaseAdapter;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppAdapter;
import com.bonlala.ebike.http.api.user.CountryApi;
import com.bonlala.ebike.http.net.TestView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Admin
 * Date 2022/3/30
 */
public class SelectCountryAdapter extends AppAdapter<CountryApi.CountryItemBean> {




    public SelectCountryAdapter(@NonNull Context context) {
        super(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CountrySelectViewHolder();
    }


    @Override
    protected RecyclerView.LayoutManager generateDefaultLayoutManager(Context context) {
        return super.generateDefaultLayoutManager(context);
    }

    private class CountrySelectViewHolder extends AppAdapter<?>.ViewHolder{

        private TextView enTv,cnTv;

        public CountrySelectViewHolder() {
            super(R.layout.item_select_country_layout);

            enTv =  findViewById(R.id.itemCountryEnTv);

            cnTv = findViewById(R.id.itemCountryCnTv);

        }

        @Override
        public void onBindView(int position) {
            assert getData() != null;
            enTv.setText(getData().get(position).getValue());
            cnTv.setText(getData().get(position).getLabelName());
        }
    }
}
