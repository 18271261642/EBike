package com.bonlala.ebike.ui.ble;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bonlala.ebike.R;
import com.bonlala.ebike.ui.bean.SearchDeviceBean;
import com.hjq.shape.view.ShapeTextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 搜索设备adapter
 * Created by Admin
 * Date 2022/4/9
 */
public class SearchDeviceAdapter extends RecyclerView.Adapter<SearchDeviceAdapter.ScanDeviceViewHolder> {



    private Context mContext;
    private List<SearchDeviceBean> deviceList;

    private OnSearchItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnSearchItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SearchDeviceAdapter(Context mContext, List<SearchDeviceBean> deviceList) {
        this.mContext = mContext;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public ScanDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_device_layout,viewGroup,false);
        return new ScanDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScanDeviceViewHolder scanDeviceViewHolder, int i) {
        scanDeviceViewHolder.nameTv.setText(deviceList.get(i).getBluetoothDevice().getName());
       // scanDeviceViewHolder.macTv.setText(deviceList.get(i).getAddress());
//        scanDeviceViewHolder.itemSearchBindTv.setText(deviceList.get(i).getRssi()+"");

        scanDeviceViewHolder.itemSearchBindTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    int position = scanDeviceViewHolder.getLayoutPosition();
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    class ScanDeviceViewHolder extends RecyclerView.ViewHolder{

        TextView nameTv;
        ShapeTextView itemSearchBindTv;

        public ScanDeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.itemSearchNameTv);
            itemSearchBindTv = itemView.findViewById(R.id.itemSearchBindTv);
        }
    }


    public interface OnSearchItemClickListener{
        void onItemClick(int position);
    }


}
