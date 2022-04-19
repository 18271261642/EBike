package com.bonlala.ebike.ui.ble;

/**
 * Created by Admin
 * Date 2022/4/11
 */
public class DialBean {

    //图片预览地址
    private String previewUrl;
    //表盘文件的地址
    private String dialFileUrl;

    //是否是当前表盘，
    private boolean isCurrentDial;


    public DialBean() {
    }


    public DialBean(String previewUrl, String dialFileUrl, boolean isCurrentDial) {
        this.previewUrl = previewUrl;
        this.dialFileUrl = dialFileUrl;
        this.isCurrentDial = isCurrentDial;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getDialFileUrl() {
        return dialFileUrl;
    }

    public void setDialFileUrl(String dialFileUrl) {
        this.dialFileUrl = dialFileUrl;
    }

    public boolean isCurrentDial() {
        return isCurrentDial;
    }

    public void setCurrentDial(boolean currentDial) {
        isCurrentDial = currentDial;
    }

    @Override
    public String toString() {
        return "DialBean{" +
                "previewUrl='" + previewUrl + '\'' +
                ", dialFileUrl='" + dialFileUrl + '\'' +
                ", isCurrentDial=" + isCurrentDial +
                '}';
    }
}
