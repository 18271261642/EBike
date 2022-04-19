package com.bonlala.ebike.http.net;

/**
 * Created by Admin
 * Date 2022/3/29
 */
public interface CommNetView<T> {

    void onSuccessData(T data, int tagCode);

    void onFailedData(String error);
}
