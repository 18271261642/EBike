package com.bonlala.ebike.ui.ble;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bonlala.ebike.R;
import com.bonlala.ebike.app.AppActivity;
import com.bonlala.ebike.app.AppApplication;
import com.bonlala.ebike.http.api.device.DeviceVersionApi;
import com.bonlala.ebike.http.api.user.LoginPresent;
import com.bonlala.ebike.http.api.user.LoginView;
import com.goodix.ble.gr.toolbox.app.libfastdfu.DfuProgressCallback;
import com.goodix.ble.gr.toolbox.app.libfastdfu.EasyDfu2;
import com.goodix.ble.libcomx.ILogger;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnDownloadListener;
import com.hjq.http.model.HttpMethod;
import com.hjq.toast.ToastUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import androidx.core.app.ActivityCompat;
import timber.log.Timber;

/**
 * 固件升级
 * Created by Admin
 * Date 2022/4/10
 */
public class DeviceDfuActivity extends AppActivity implements LoginView<DeviceVersionApi.DeviceVersionInfo>, ILogger {

    private LoginPresent loginPresent;

    private TextView dfuCurrVersionTv,dfuBatteryTv,dufOtaTv,dufTxtTv;
    private TextView otaLastVersionTv,otaFileSizeTv,otaRemarkTv,otaTxtDescTv;
    private TextView dfuStartBtn;

    private String otaUrl;
    private String otaFile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dfu_layout;
    }

    @Override
    protected void initView() {
        findViews();

    }

    @Override
    protected void initData() {
        loginPresent = new LoginPresent();
        loginPresent.attachView(this);
        otaFile = Environment.getExternalStorageDirectory().getPath()+"/Download/";
        loginPresent.getPresentDeviceVersion(this,new DeviceVersionApi().setDeviceVersion("C078",1),0x00);
    }


    private void findViews(){

        dfuStartBtn = findViewById(R.id.dfuStartBtn);

        otaLastVersionTv = findViewById(R.id.otaLastVersionTv);
        otaFileSizeTv = findViewById(R.id.otaFileSizeTv);
        otaRemarkTv = findViewById(R.id.otaRemarkTv);


        dfuBatteryTv = findViewById(R.id.dfuBatteryTv);
        dfuCurrVersionTv = findViewById(R.id.dfuCurrVersionTv);

        setOnClickListener(dfuStartBtn);

    }


    @Override
    public void onClick(View view) {
        if(view == dfuStartBtn){
            downloadFile();
        }
    }

    @Override
    public void not200CodeMsg(String msg) {

    }

    @Override
    public void onSuccessData(Object data, int tagCode) {
        Timber.e("-----固件信息="+data.toString());
        if(data == null)
            return;
        showDesc(data);
    }

    @Override
    public void onFailedData(String error) {

    }


    private void showDesc(Object oj){
        try {
            DeviceVersionApi.DeviceVersionInfo deviceVersionInfo = (DeviceVersionApi.DeviceVersionInfo) oj;
            this.otaUrl = deviceVersionInfo.getFileUrl();
            otaLastVersionTv.setText("最新版本: V "+deviceVersionInfo.getVersionName());
            otaFileSizeTv.setText("安装包大小: "+(deviceVersionInfo.getFileSize()/1000)+"kb");
            otaRemarkTv.setText(deviceVersionInfo.getRemark());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void downloadFile(){
        if(otaUrl == null){
            Toast.makeText(getActivity(),"文件下载地址为空!",Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isWrite = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if(!isWrite){
            Toast.makeText(getActivity(),"未获取权限!",Toast.LENGTH_SHORT).show();
            return;
        }
        EasyHttp.download(this)
                .method(HttpMethod.GET)
                .file(otaFile+"c078.bin")
                .url(otaUrl)
                .listener(new OnDownloadListener() {
                    @Override
                    public void onStart(File file) {

                    }

                    @Override
                    public void onProgress(File file, int progress) {
                        dfuStartBtn.setText("下载中:"+progress+"%");
                    }

                    @Override
                    public void onComplete(File file) {
                        dfuStartBtn.setText("下载完成");
                        startDfuOperate(file);
                    }

                    @Override
                    public void onError(File file, Exception e) {

                    }

                    @Override
                    public void onEnd(File file) {

                    }
                }).start();
    }


    private void startDfuOperate(File dfuF){
        BluetoothDevice bluetoothDevice = AppApplication.getBleOperateManager().getConnDevice();
        if(bluetoothDevice == null){
            ToastUtils.show("设备未连接!");
            return;
        }
        Timber.e("-----bleu="+bluetoothDevice.getAddress()+" "+bluetoothDevice.getName());
        //将file转换成InputStream对象
        try {
            InputStream inputStream = new FileInputStream(dfuF);
            Timber.e("------input="+(inputStream == null));

            EasyDfu2 easyDfu2 = new EasyDfu2();
            easyDfu2.setLogger(this);
            easyDfu2.setListener(new DfuProgressCallback() {
                @Override
                public void onDfuStart() {

                    Timber.e("--easy------onDfuStart----");
                    dfuStartBtn.setClickable(false);
                    dfuStartBtn.setText("开始升级");
                }

                @Override
                public void onDfuProgress(int i) {

                    dfuStartBtn.setClickable(false);
                    Timber.e("--easy------onDfuProgress----="+i);
                    dfuStartBtn.setText("升级中:"+i+"%");
                }

                @Override
                public void onDfuComplete() {
                    dfuStartBtn.setClickable(true);
                    Timber.e("--easy------onDfuComplete----");
                    dfuStartBtn.setText("升级完成");
                }

                @Override
                public void onDfuError(String s, Error error) {
                    dfuStartBtn.setClickable(true);
                    Timber.e("--easy----onDfuError------="+s+" error="+error.getMessage());
                    dfuStartBtn.setText("升级失败，请重新升级");
                }
            });
            easyDfu2.startDfuInCopyMode(getActivity(),bluetoothDevice,inputStream,Integer.valueOf(String.valueOf(1100000),16));
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void v(String s, String s1) {

    }

    @Override
    public void d(String s, String s1) {

    }

    @Override
    public void i(String s, String s1) {

    }

    @Override
    public void w(String s, String s1) {

    }

    @Override
    public void e(String s, String s1) {

    }

    @Override
    public void e(String s, String s1, Throwable throwable) {

    }
}
