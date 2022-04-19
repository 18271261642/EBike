package com.bonlala.blelibrary;

import android.app.Application;

public class BleApplication  extends Application {

    private static BleManager bleManager;
    private static BleApplication bleApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        bleApplication = this;
    }

    public static BleApplication getBleApplication(){
        return bleApplication;
    }

    public BleManager getBleManager() {
        if (bleManager == null) {
            bleManager = BleManager.getInstance(this);
        }

        return bleManager;
    }
}
