package com.bonlala.ebike.ui.ble;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.utils.MmkvUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Admin
 * Date 2022/3/22
 */
public class ShowLogActivity extends AppCompatActivity {

    private TextView showLogTv;
    private TextView clearLogTv;

    private TextView bdLotTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log_layout);

        showLogTv = findViewById(R.id.showLogTv);
        clearLogTv = findViewById(R.id.clearLogTv);
        bdLotTv = findViewById(R.id.bdLotTv);

        getLog();

        clearLogTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppApplication.getBleOperateManager().clearAllWriteData();
                getLog();
            }
        });

    }


    private void getLog(){
        showLogTv.setText(AppApplication.getBleOperateManager().getAllWriteDeviceData());

        bdLotTv.setText((String)MmkvUtils.getSaveParams("bd_map",""));
    }
}
